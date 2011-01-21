package com.apache.hadoop.hbase.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.RowLock;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.tableindexed.IndexSpecification;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTable;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTableAdmin;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTableDescriptor;
import org.apache.hadoop.hbase.client.transactional.TransactionManager;
import org.apache.hadoop.hbase.client.transactional.TransactionState;
import org.apache.hadoop.hbase.client.transactional.TransactionalTable;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.ColumnCountGetFilter;
import org.apache.hadoop.hbase.filter.ColumnPaginationFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SkipFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.apache.hadoop.hbase.filter.RowPaginationFilter;

/**
 * HBase examples.
 * @author ncanis@gmail.com
 */
public class HBaseClient extends AbstractHBase{
	protected static final String TABLE = "test3";
	protected static final String INDEXED_TABLE = "indexed_test2";
	
	private static final String COL_FAMILY_USER ="user";
	private static final byte[] COL_FAMILY_USER_B = COL_FAMILY_USER.getBytes();
	private static final String COL_FAMILY_SCORE = "score";
	private static final byte[] COL_FAMILY_SCORE_B = COL_FAMILY_SCORE.getBytes();
	
	public HBaseClient() {
		super();
	}

	/**
	 * insert data
	 * 
	 * @throws IOException
	 */
	public void insert() throws IOException {
		// To add to a row, use Put. A Put constructor takes the name of the row
		// you want to insert into as a byte array. In HBase, the Bytes class
		// has
		// utility for converting all kinds of java types to byte arrays. In the
		// below, we are converting the String "myLittleRow" into a byte array
		// to
		// use as a row key for our update. Once you have a Put instance, you
		// can
		// adorn it by setting the names of columns you want to update on the
		// row,
		// the timestamp to use in your update, etc.If no timestamp, the server
		// applies current time to the edits.

		HTable table = new HTable(config, TABLE);
		// row key
		int i =0;
		for (i = 1; i <= 30; i++) {
			Put p = new Put(("key-"+i).getBytes());
			p.add(COL_FAMILY_USER_B, ("name").getBytes(), ("macula" + i).getBytes());
			p.add(COL_FAMILY_USER_B, ("age").getBytes(), (i + "").getBytes());

			p.add(COL_FAMILY_SCORE_B, ("math").getBytes(), (i + "").getBytes());
			p.add(COL_FAMILY_SCORE_B, ("science").getBytes(), (i + "").getBytes());

			table.put(p);
		}
		table.flushCommits();
		
		log.debug(i+" data inserted.");
	}

	/**
	 * list
	 * 
	 * @throws IOException
	 */
	public void list() throws IOException {
		HTable table = new HTable(config, TABLE);
		// Scan scan = new Scan(Bytes.toBytes("1"), Bytes.toBytes("2"));
		Scan scan = new Scan();
		scan.addColumn(COL_FAMILY_USER_B, "name".getBytes());
		scan.addColumn(COL_FAMILY_USER_B, "age".getBytes());

		ResultScanner scanner = table.getScanner(scan);
		for (Result r : scanner) {
			
			String name = Bytes.toString(r.getValue(COL_FAMILY_USER_B, "name".getBytes()));
			String age = Bytes.toString(r.getValue(COL_FAMILY_USER_B, "age".getBytes()));
			log.debug("### Searched name={},age={}", name, age);
		}

		scanner.close();
	}

	/**
	 * @throws IOException
	 */
	public void getRow(byte[] rowKey) throws IOException {
		HTable table = new HTable(config, TABLE);
		// row key
		Get g = new Get(rowKey);
		Result r = table.get(g);

		String name = Bytes.toString(r.getValue(COL_FAMILY_USER_B, "name".getBytes()));
		String age = Bytes.toString(r.getValue(COL_FAMILY_USER_B, "age".getBytes()));
		log.debug("### Get name={},age={}", name, age);
	}
	
	/**
	 * Row Locking
	 * @throws Exception 
	 */
	public void rowLock() throws Exception {
		HTable table = new HTable(config, TABLE);
		
		byte[] rowKey = "key-1000".getBytes();
		RowLock lock = table.lockRow(rowKey);
		try{
			// Check if the record exists
            Get get = new Get(rowKey, lock);
            Result result = table.get(get);
            if (!result.isEmpty()) {
                throw new Exception("Row with this key already exists.");
            }

            // Row does not exist yet, create it
            Put put = new Put(rowKey, lock);
            put.add(COL_FAMILY_USER_B, Bytes.toBytes("name"), ("macula1000").getBytes());
            put.add(COL_FAMILY_USER_B, Bytes.toBytes("age"), "1000".getBytes());
            table.put(put);
		}catch(Exception e){
			log.error("lock",e);
		}finally{
			table.unlockRow(lock);
		}
	}
	/**
	 * get region servers.
	 * 
	 * @return
	 * @throws IOException
	 */
	public int getNumberOfRegions() throws IOException {
		HTable table = new HTable(config, TABLE);
		byte[][] startKeys = table.getStartKeys();

		log.debug("## Regions ={}", startKeys.length);
		return startKeys.length;
	}

	/**
	 * get page list.
	 * 
	 * @throws IOException
	 */
	public void pageList() throws IOException {
		byte[] start = "0".getBytes();
		byte[] end = "5".getBytes();

		HTable table = new HTable(config, TABLE);
		Scan scan = new Scan(start, end);
		scan.addColumn(COL_FAMILY_USER_B, "name".getBytes());
		scan.addColumn(COL_FAMILY_USER_B, "age".getBytes());
		toList("pageList",table.getScanner(scan));
	}

	/**
	 * index test
	 * 
	 * @throws IOException
	 */
	public void indexTest() throws IOException {
		IndexedTableAdmin admin = new IndexedTableAdmin(config);

		HTableDescriptor desc = new HTableDescriptor(INDEXED_TABLE);
		
		desc.addFamily(new HColumnDescriptor("user"));
		desc.addFamily(new HColumnDescriptor("score"));

		try{
			admin.disableTable(INDEXED_TABLE);
			admin.removeIndex(INDEXED_TABLE.getBytes(), "name");
			admin.removeIndex(INDEXED_TABLE.getBytes(), "age");
		}catch(Exception e){
			log.warn("There is no index tables.",e);
		}
		

		IndexedTableDescriptor itd = new IndexedTableDescriptor(desc);
		itd.addIndex(new IndexSpecification("name", Bytes.toBytes("user:name")));
		itd.addIndex(new IndexSpecification("age", Bytes.toBytes("user:age")));

		if (admin.tableExists(INDEXED_TABLE)) {
			admin.disableTable(INDEXED_TABLE);
			admin.deleteTable(INDEXED_TABLE);			
		}

		admin.createIndexedTable(itd);

		IndexedTable table = new IndexedTable(config, Bytes.toBytes(INDEXED_TABLE));
		for (int i = 20; i>0; i--) {
			int code = getRand(1000);
			int code2 = getRand(1000);
			Put p = new Put(Bytes.toBytes(i));
			p.add(COL_FAMILY_USER_B, ("name").getBytes(), ("macula" + code).getBytes());
			p.add(COL_FAMILY_USER_B, ("age").getBytes(), (code2 + "").getBytes());

			p.add(COL_FAMILY_SCORE_B, ("math").getBytes(), (code + "").getBytes());
			p.add(COL_FAMILY_SCORE_B, ("science").getBytes(), (code2 + "").getBytes());

			table.put(p);
		}
		table.flushCommits();

		ResultScanner scanner = table.getIndexedScanner("age",
		        HConstants.EMPTY_START_ROW,null, null, null, new byte[][] {
		        Bytes.toBytes("user:name"),
		        Bytes.toBytes("user:age") });
		
		toList("IndexTest",scanner);
		table.close();
	}
	
	private int getRand(int range){
		return (int)(Math.random()*range);
	}

	private void doFilter(String tag, Filter filter) throws IOException {
		HTable table = new HTable(config, TABLE);
		Scan scan = new Scan();
		scan.addColumn(COL_FAMILY_USER_B, "name".getBytes());
		scan.addColumn(COL_FAMILY_USER_B, "age".getBytes());
		scan.setFilter(filter);		
		toList(tag, table.getScanner(scan));
	}
	
	/**
	 * @throws IOException
	 */
	public void pageFilter() throws IOException {
		doFilter("PageFilter",new PageFilter(2));
	}
	
	/**
	 */
	public void prefixFilter() throws IOException {
		PrefixFilter filter = new PrefixFilter("key-1".getBytes());
		doFilter("prefixFilter", filter);
	}
	
	/**
	 * @throws IOException
	 */
	public void firstKeyFilter() throws IOException {
		FirstKeyOnlyFilter filter = new FirstKeyOnlyFilter();
		doFilter("firstKeyFilter", filter);
	}
	
	/**
	 * @throws IOException
	 */
	public void singleColumnFilter() throws IOException {
		SingleColumnValueFilter filter = new SingleColumnValueFilter(
				COL_FAMILY_USER_B,"name".getBytes(),CompareOp.EQUAL,new SubstringComparator("macu"));
		doFilter("singleColumnFilter", filter);
	}
	
	/**
	 * @throws IOException
	 */
	public void skipFilter() throws IOException {
		ValueFilter valueFilter = new ValueFilter(CompareOp.NOT_EQUAL,new BinaryComparator("5".getBytes()));
		SkipFilter skipFilter = new SkipFilter(valueFilter);
		doFilter("SkipFilter", skipFilter);
	}
	
	/**
	 * @throws IOException
	 */
	public void columnCountGetFilter() throws IOException {
		ColumnCountGetFilter ccf = new ColumnCountGetFilter(1);
		doFilter("columnCountGetFilter", ccf);
	}
	
	public void pageRowFilter() throws IOException {
		log.debug("RowPaginationFilter");
		RowPaginationFilter filter = new RowPaginationFilter(3,5);
		doFilter("RowPaginationFilter", filter);
	}

	public void pageColumnFilter() throws IOException {
		log.debug("ColumnPaginationFilter");
		ColumnPaginationFilter filter = new ColumnPaginationFilter(1,0);
		doFilter("ColumnPaginationFilter", filter);
	}
	
	/**
	 * @throws IOException
	 */
	public void filterList() throws IOException {
		List<Filter> list = new ArrayList<Filter>();
		SingleColumnValueFilter svf1 = new SingleColumnValueFilter(
				COL_FAMILY_USER_B,"name".getBytes(),CompareOp.EQUAL,new SubstringComparator("macu"));
		SingleColumnValueFilter svf2 = new SingleColumnValueFilter(
				COL_FAMILY_USER_B,"age".getBytes(),CompareOp.EQUAL,new BinaryComparator("5".getBytes()));
		list.add(svf1);
		list.add(svf2);
		
		// and:MUST_PASS_ALL  , or:MUST_PASS_ONE
		FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL,list);
		doFilter("filterList", filterList);
	}
	
	public void qualifierFilter() throws IOException {
		QualifierFilter filter = new QualifierFilter(CompareOp.EQUAL,new BinaryComparator("ddd".getBytes()));
		doFilter("qualifierFilter", filter);
	}
	
	private void testRecreateRowKey() throws IOException {
		byte[] key = Bytes.toBytes("key-50");
		HTable table = new HTable(config, TABLE);
		
		Put p = new Put(key);
		p.add(COL_FAMILY_USER_B, ("name").getBytes(), ("macula500").getBytes());
		p.add(COL_FAMILY_USER_B, ("age").getBytes(), ("500").getBytes());
		table.put(p);
		
		Delete delete = new Delete(key);
		table.delete(delete);
		
		Get get = new Get(key);
		boolean exist = table.exists(get);
		Result result = table.get(get);
		log.debug("after delete : result= key={}, exist={}",result, exist);
		
		p = new Put(key);
		p.add(COL_FAMILY_USER_B, ("name").getBytes(), ("macula400").getBytes());
		p.add(COL_FAMILY_USER_B, ("age").getBytes(), ("400").getBytes());
		table.put(p);
		
		get = new Get(key);
		exist = table.exists(get);
		result = table.get(get);
		log.debug("after recreate same row key : result= {},exist={}",result, exist);
	}
	/**
	 * 
	 * 
	 * @throws IOException
	 */
	public void testTransaction() throws IOException {
		byte[] old_key = Bytes.toBytes("key-30");
		byte[] new_key = Bytes.toBytes("key-100");
		
		TransactionManager tran = new TransactionManager(config);
		TransactionState state=null;
		TransactionalTable table = null;
		try{
			
			// table locking.
			state = tran.beginTransaction();
			table = new TransactionalTable(config,TABLE);
			table.setAutoFlush(false);
			
			Delete del = new Delete(old_key); // delete row with all families.
			table.delete(state,del);
			
			Get get = new Get(old_key);
			Result result = table.get(state,get);
			log.debug("result={}, exist={}",result);
			
			if(result!=null) {
				throw new Exception("error invoked.");
			}
			
			Put p = new Put(new_key);
			p.add(COL_FAMILY_USER_B, ("name").getBytes(), ("macula500").getBytes());
			p.add(COL_FAMILY_USER_B, ("age").getBytes(), ("500").getBytes());

			table.put(state,p);
						
			tran.tryCommit(state); // prepare & do commit.
			log.debug("transaction test finished.");
		}catch(Exception e){
			e.printStackTrace();
			if(tran!=null) tran.abort(state);
			log.debug("transaction commit failed.");
		} finally{
			if(table!=null) table.close();
		}
		
		getRow(old_key);
		getRow(new_key);
	}
	
	/**
	 * @throws IOException
	 */
	public void file() throws IOException{
		// you must use hadoop library directly.
		
//		HTable table = new HTable(config, TABLE);
//		Put p = new Put(Bytes.toBytes("key-1"));
//		p.add("user".getBytes(), ("file").getBytes(),null);
	}
	public static void main(String[] args) throws Exception {
		/*
		 * 
		 */
		HBaseClient client = new HBaseClient();
//		client.closeTransaction();
//		client.getRow(Bytes.toBytes("key-30"));
//		client.deleteTable(TABLE);
//		client.createTable(TABLE,COL_FAMILY_USER, COL_FAMILY_SCORE);
//		client.insert();
//		client.rowLock();
		// client.list();
		// client.getNumberOfRegions();
		// client.pageList();
		
//		client.filterList();
//		client.columnCountGetFilter();
//		client.firstKeyFilter();
//		client.pageFilter();
//		client.singleColumnFilter();
//		client.skipFilter();
//		client.prefixFilter();
//		client.qualifierFilter();
		
		client.indexTest();
		
//		client.testTransaction();
//		client.testRecreateRowKey();
//		client.pageFilter();
//		client.pageColumnFilter();
//		client.pageRowFilter();
	}
}
