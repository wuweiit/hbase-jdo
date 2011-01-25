package com.apache.hadoop.hbase.client.jdo.query;

import java.util.Arrays;


/**
 * insertion test
 * @author ncanis
 *
 */
public class InsertQueryTest extends JDOTest{
	private int rand(){
		return (int)(Math.random()*1000);
	}
	public void testInsert() throws Exception {
//		initTable(HBaseOrder.ASC);
		
		TestUserBean bean = insert();
		
		// select bean information from table.
		SelectQuery sQuery = dbo.createSelectQuery(bean.getTableName());
		TestUserBean bean2 = (TestUserBean)sQuery.select(bean.getRow(),TestUserBean.class);
		log.debug("select inserted data from table. data={}",bean2);
		
		assertEquals(bean.getFamily(),bean2.getFamily());
		assertEquals(bean.getId(),bean2.getId());
		assertEquals(bean.getName(),bean2.getName());		
		assertTrue(Arrays.equals(bean.getRow(),bean2.getRow()));
	}
	
	public void testInsertStress() {
		insertBox(1000000);
	}
	
	public TestUserBean insert(){
		TestUserBean bean = new TestUserBean();
		bean.set(TABLE,FAMILY);
		bean.setId("testid-"+rand());
		bean.setName("testname-"+rand());
		
		// insert bean.
		InsertQuery query = dbo.createInsertQuery(TABLE);
		query.insert(bean);
//		log.debug("inserted data {} in table. created row key={}",bean,Bytes.toLong(bean.getRow()));
		return bean;
	}
	
	private void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private volatile int runCount = 0;
	public void insertBox(int count) {
		for(int i=0;i<count;i++) {
			long start = System.currentTimeMillis();
			insert();
			runCount++;
			long period = System.currentTimeMillis()-start;
			log.debug("inserted data. period={} ms, total={}",
					new Object[]{period,runCount});
		}
		start();
		SelectQuery query = dbo.createSelectQuery(TABLE);
		
		
		RowCountReceiver receiver = new RowCountReceiver() {
			@Override
			public void receive(int totalCount) {
				log.debug("received count. total={} received.",totalCount);
			}
		};
		log.debug("total row count={} ",query.getTotalRowCount(receiver,100));
		end("get total count");
	}
	

}
