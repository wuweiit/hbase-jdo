package com.apache.hadoop.hbase.client.jdo;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTable;

import junit.framework.TestCase;


/**
 * @author ncanis
 *
 */
public class HBasePoolManagerTest extends TestCase{
	public void testPool() throws Exception {
		HBasePoolManager pool = HBasePoolManager.get(new HBaseConfiguration());
		IndexedTable table = pool.borrow("user_info");
		assertEquals(pool.getPool().getNumActive(),1);
		
		pool.release(table);
		
		assertEquals(pool.getPool().getNumActive(),0);
		assertEquals(pool.getPool().getNumIdle(),1);
	}
}
