package com.apache.hadoop.hbase.client.jdo.query;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.RowLock;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTable;

import com.apache.hadoop.hbase.client.jdo.AbstractHBaseDBO;
import com.apache.hadoop.hbase.client.jdo.IHBaseLog;
import com.apache.hadoop.hbase.client.jdo.SequenceGenerator;

/**
 * All Query class must extends HBQuery class.
 * @author ncanis
 *
 */
public abstract class HBQuery  implements IHBaseLog{
	protected String tableName;
	protected HBaseBeanProcessor processor;
	protected AbstractHBaseDBO dbo;
	protected SequenceGenerator sequence;
	
	public HBQuery(AbstractHBaseDBO dbo,String tableName){
		this.dbo = dbo;
		init(dbo.getConfig(),tableName);
		this.processor = dbo.getProcessor();
		this.sequence = dbo.getSequence();
	}
	
	private void init(HBaseConfiguration config, String tableName) {
		this.tableName = tableName;
	}
	
	public final IndexedTable borrowTable() throws Exception{
		return dbo.getPool().borrow(tableName);
	}
	
	public final void work(IHBaseWork work){
		IndexedTable table = null;
		try{
			table = borrowTable();
			work.work(table);
		}catch(Exception e){
			log.error("work",e);
		}finally{
			releaseTable(table);
		}
	}
	
	public final void releaseTable(IndexedTable table, RowLock lock){
		if(table==null) return;
		try {
			if(lock!=null) table.unlockRow(lock);
			dbo.getPool().release(table);
		} catch (Exception e) {
			log.error("release error",e);
		}
	}
	public final void releaseTable(IndexedTable table){
		releaseTable(table,null);
	}
	
	
	public String getTableName() {
		return tableName;
	}
}

interface IHBaseWork{
	public void work(IndexedTable table);
}
