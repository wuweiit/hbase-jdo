package com.apache.hadoop.hbase.client.jdo.query;

import java.util.Hashtable;
import java.util.List;


/**
 * test update.
 * @author ncanis
 *
 */
public class UpdateQueryTest extends JDOTest{
	public void testUpdate() throws Exception {
		SelectQuery sQuery = dbo.createSelectQuery(TABLE);
		HBaseParam param = new HBaseParam();
		param.addColumn(COLS);
		param.setPage(null,20);
		param.addSearchOption(COL_ID,"testid",QSearch.LIKE);
		List<TestUserBean> list = (List<TestUserBean>)sQuery.search(FAMILY,param, TestUserBean.class);
		assertTrue(list.size()>0);
		
		log.debug("got {} data from {}",list.size(), TABLE);
		
		// select column.
		TestUserBean bean = list.get(0);
		
		// update row column
		UpdateQuery uQuery = dbo.createUpdateQuery(TABLE);
		String newValue= "updated";
		assertTrue(uQuery.update(bean.getRow(),bean.getFamily(),COL_ID,newValue.getBytes()));
		
		bean = (TestUserBean)sQuery.select(bean.getRow(),TestUserBean.class);
		
		log.debug("update column={}",bean);
		assertEquals(bean.getId(),newValue);
		
		Hashtable<String,byte[]> cols = new Hashtable<String,byte[]>();
		cols.put(COL_ID,newValue.getBytes());
		cols.put(COL_NAME,newValue.getBytes());
		assertTrue(uQuery.update(bean.getRow(),bean.getFamily(),cols));
		
		bean = (TestUserBean)sQuery.select(bean.getRow(),TestUserBean.class);
		
		log.debug("update columns={}",bean);
		assertEquals(bean.getId(),newValue);
		assertEquals(bean.getName(),newValue);
	}
}
