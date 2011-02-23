package com.apache.hadoop.hbase.client.jdo.query;

import java.util.List;

import org.apache.hadoop.hbase.util.Bytes;

import com.apache.hadoop.hbase.client.jdo.util.HUtil;



/**
 * selection test.
 * @author ncanis
 *
 */
public class SelectQueryTest extends JDOTest{
	public void testSelect() throws Exception {
		SelectQuery sQuery = dbo.createSelectQuery(TABLE);
		
		// search general.
		start();
		HBaseParam param = new HBaseParam();
		param.addColumn(COLS);
		param.setPage(null,20);
		param.addSearchOption(COL_ID,"testid",QSearch.LIKE);
		List<TestUserBean> list = (List<TestUserBean>)sQuery.search(FAMILY,param, TestUserBean.class);
		assertTrue(list.size()>0);
		log.debug("got {} data from {}",list.size(), TABLE);
		end("search with no option");
		
		// select column.
		start();
		TestUserBean bean = list.get(0);
		log.debug("Time={}",HUtil.makeSimpleDate(bean.getTimeCalendar()));
		
		String id = (String)sQuery.selectColumn(bean.getRow(),FAMILY,COL_ID,String.class);
		assertNotNull(id);
		log.debug("got id={} data from {} row",id,Bytes.toLong(bean.getRow()));
		end("select column");
		
		// select row.
		start();
		TestUserBean bean2 = (TestUserBean)sQuery.select(bean.getRow(), TestUserBean.class);
		assertNotNull(bean2);
		log.debug("got data={} from {} row",bean2,Bytes.toLong(bean.getRow()));
		end("select row");
		
		start();
		RowCountReceiver receiver = new RowCountReceiver() {
			@Override
			public void receive(int totalCount) {
				log.debug("received count. total={} received.",totalCount);
			}
		};
		int count = sQuery.getTotalRowCount(receiver,10000);
		log.debug("got total row count={}",count);
		end("total row count");
		
		// confirm column value existing.
		start();
		assertTrue(sQuery.existColumnValue(bean.getFamily(),COL_ID,"testid-74".getBytes()));
		end("existColumnValue");
		
		// search with start row.
		start();
		HBaseParam param2 = new HBaseParam();
		param2.addColumn(COLS);
		param2.setPage(bean.getRow(),20);
		param2.addSearchOption(COL_ID,"testid",QSearch.LIKE);
		List<TestUserBean> list2 = (List<TestUserBean>)sQuery.search(FAMILY,param, TestUserBean.class);
		assertTrue(list2.size()>0);
		end("search with start row");
	}
	
//	public void baseSearch() throws IOException {
//		long start = System.currentTimeMillis();
//		IndexedTable table = new IndexedTable(new HBaseConfiguration(),TABLE.getBytes());
//		table.setScannerCaching(1000);
//		// 53sec
//		
////		RowFilter filter = new RowFilter(CompareOp.GREATER, new BinaryPrefixComparator("999996".getBytes()));
//		
//		
////		Filter filter = new SingleColumnValueFilter(
////				Bytes.toBytes(family[0]), 
////				Bytes.toBytes(cols[0]),
////				CompareOp.EQUAL, 
////				Bytes.toBytes("pid-999996"));
//		
//		/*
//		 * index key = value + base row key
//		 */
//		byte[] startRow = null;
//		byte[] endRow = null;
////		
////		
////		filter = rowFilter;
//		/*
//		 * String indexId, 
//		 * final byte[] indexStartRow,  
//		 * final byte[] indexStopRow,
//      	   byte[][] indexColumns, 
//      	   final Filter indexFilter,
//      	   final byte[][] baseColumns
//		 */
//		ResultScanner scanner = table.getIndexedScanner(
//				COL_ID, // index id
//				startRow, // start row
//				endRow,  // end row
//		        
//		        new byte[][] {  // index columns.
//					Bytes.toBytes(FAMILY+":"+COL_ID)					
//				},
//				null, // filter
//		        new byte[][] {  //base columns.
//					(FAMILY+":"+COL_ID).getBytes()
//				}
//		);
//		
//		toList("search", scanner);
//		
//		long end = System.currentTimeMillis();
//		log.debug("### searching end time={}ms ###",(end-start));
//	}
//	
//	private final void toList(String tag, ResultScanner rs) throws IOException {
//		boolean isExist = false;
//		
//		for (Result r : rs) {
//			if (isExist == false)
//				isExist = true;
//			List<KeyValue> list = r.list();
//			StringBuilder sb = new StringBuilder();
//			for(KeyValue kv : list) {
//				sb.append("col=").append(Bytes.toString(kv.getQualifier()));
//				sb.append(" value=").append(Bytes.toString(kv.getValue())).append(",");
//			}
//			log.debug("rowkey={}, {}",Bytes.toString(r.getRow()),sb.toString());
//		}
//		if (isExist == false)
//			log.debug("### " + tag + " data doens't exist.");
//
//		rs.close();
//	}
}
