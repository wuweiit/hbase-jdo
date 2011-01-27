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
	protected static final String TABLE = "member";
	protected static final String FAMILY = "user";
	protected static final String COL_ID = "id";
	protected static final String COL_NAME = "name";
	protected static final String COL_AGE = "age";
	protected static final String COL_MAIL = "mail";
	protected static final String COL_ADDRESS = "address";
	protected static final String COL_SEX = "sex";
	protected static final String COL_NICK = "nick";
	protected static final String COL_HEIGHT = "height";
	
	protected static final String[] COLS = {COL_ID,COL_NAME,COL_AGE,COL_MAIL,COL_ADDRESS,COL_SEX,COL_NICK,COL_HEIGHT};
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
