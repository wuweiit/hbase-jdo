package com.apache.hadoop.hbase.client.jdo;

import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTable;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * IndexedTable Pool Manager
 * used apache commons pool library. 
 * @author ncanis
 *
 */
public class HBasePoolManager implements IHBaseLog{
	private static HBasePoolManager mgr;
	private GenericKeyedObjectPool pool;
	private HBaseConfiguration config;
	
	public HBasePoolManager(HBaseConfiguration config){		
		this.config = config;
		pool = new GenericKeyedObjectPool(new HTableFactory());
		
		// you can configure this in hbase-site.xml
		pool.setMaxActive(config.getInt("hbase.jdo.maxActive",100));
		pool.setMaxWait(config.getInt("hbase.jdo.maxWait",5000));
		pool.setMinIdle(config.getInt("hbase.jdo.minIdle",3));	
	}
	
	public static HBasePoolManager get(HBaseConfiguration config){
		synchronized(HBasePoolManager.class) {
			if(mgr==null) {
				mgr = new HBasePoolManager(config);
			}
			return mgr;
		}
	}
	
	public GenericKeyedObjectPool getPool() {
		return pool;
	}

	/**
	 * borrow IndexedTable object.
	 * @param table
	 * @return
	 * @throws Exception
	 */
	public IndexedTable borrow(String tableName) throws Exception{
		IndexedTable table = (IndexedTable)pool.borrowObject(tableName);		
		table.setAutoFlush(true);
		return table;
	}
	
	/**
	 * return borrowed object.
	 * @param table
	 * @throws Exception
	 */
	public void release(IndexedTable table) throws Exception{		
		pool.returnObject(Bytes.toString(table.getTableName()),table);
	}
	
	/**
	 * table factory.
	 * @author ncanis
	 *
	 */
	class HTableFactory extends BaseKeyedPoolableObjectFactory{
		@Override
		public Object makeObject(Object table) throws Exception {
			return new IndexedTable(config,table.toString().getBytes());
		}
	}
}
