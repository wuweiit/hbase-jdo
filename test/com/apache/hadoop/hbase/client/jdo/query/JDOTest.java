package com.apache.hadoop.hbase.client.jdo.query;

import junit.framework.TestCase;

import com.apache.hadoop.hbase.client.jdo.AbstractHBaseDBO;
import com.apache.hadoop.hbase.client.jdo.HBaseDBOImpl;
import com.apache.hadoop.hbase.client.jdo.IHBaseLog;

/**
 * main test.
 * 
 * you can crate table.
 * @author ncanis
 *
 */
public class JDOTest extends TestCase implements IHBaseLog{
	protected static final String TABLE = "user_info2";
	protected static final String FAMILY = "user";
	protected static final String COL_ID = "id";
	protected static final String COL_NAME = "name";
	protected static final String[] COLS = {COL_ID,COL_NAME};
	protected AbstractHBaseDBO dbo = new HBaseDBOImpl();
	
	private long start;
	@Override
	protected void setUp() throws Exception {
//		initTable(HBaseOrder.ASC);
	}

	protected final void initTable(HBaseOrder order) throws Exception {
		if(dbo.isTableExist(TABLE)) {
			dbo.deleteTable(TABLE);
		}
		// create table
		if(dbo.isTableExist(TABLE)==false) {
			assertTrue(dbo.createTableIfNotExist(TABLE,order, FAMILY));
		}
		
		if(dbo.getIndexSpec(TABLE,COL_ID)==null){
			assertTrue(dbo.addIndexExistingTable(TABLE, FAMILY,COL_ID));
		}
		
		if(dbo.getIndexSpec(TABLE,COL_NAME)==null){
			assertTrue(dbo.addIndexExistingTable(TABLE, FAMILY,COL_NAME));
		}
	}
	
	public void test(){
		// do nothing
	};
	
	public final void start(){
		this.start = System.currentTimeMillis();
	}
	
	public String end(){
		return (System.currentTimeMillis()-start)+"ms";
	}
	
	public void end(String tag){
		log.debug("{} finished. time={}",tag,end());
	}
}
