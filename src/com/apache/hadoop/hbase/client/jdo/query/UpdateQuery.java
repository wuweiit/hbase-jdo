package com.apache.hadoop.hbase.client.jdo.query;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RowLock;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTable;

import com.apache.hadoop.hbase.client.jdo.AbstractHBaseDBO;

/**
 * Update Query.
 * @author ncanis
 *
 */
public class UpdateQuery extends HBQuery{
	public UpdateQuery(AbstractHBaseDBO dbo,String tableName){
		super(dbo,tableName);
	}
	
	/**
	 * update column an specified row's column value.
	 * @param row
	 * @param family
	 * @param col
	 * @param value
	 * @return
	 */
	public boolean update(byte[] row, String family, String col, byte[] value){
		Hashtable<String,byte[]> cols = new Hashtable<String, byte[]>();
		cols.put(col,value);
		return update(row,family,cols);
	}
	
	/**
	 * overwrite column value.
	 * (no row lock, just overwrite)
	 * @param family
	 * @param cols
	 * @param rows
	 * @return
	 */
	public boolean overwrite(String family,Hashtable<String,byte[]> cols, byte[]...rows){
		boolean isSuccess = false;
		IndexedTable table = null;
		try{
			table = borrowTable();
			table.setAutoFlush(false);

			List<Put> data = new ArrayList<Put>();
			for(byte[] row:rows) {
				Put p = new Put(row);
				for(String col:cols.keySet()) {
					byte[] value = cols.get(col);
					p.add(family.getBytes(), col.getBytes(), value);
				}
				p.setTimeStamp(System.currentTimeMillis());
//				data.add(p);
				table.put(p);			
			}
			table.flushCommits();
			isSuccess = true;
		}catch(Exception e){
			log.debug("overwrite",e);
		} finally{			
			releaseTable(table);
		}
		return isSuccess;
	}
	
	/**
	 * Delete & Put
	 * @param row
	 * @param family
	 * @param cols
	 * @return
	 */
	public boolean update(byte[] row,String family,Hashtable<String,byte[]> cols){
		boolean isSuccess = false;
		IndexedTable table = null;
		RowLock lock=null;
		try{
			table = borrowTable();
			table.setAutoFlush(false);
			
			lock = table.lockRow(row);			
			Delete del = new Delete(row,HConstants.LATEST_TIMESTAMP,lock); // delete row with all families.
			for(String col:cols.keySet()) {
				del.deleteColumn(family.getBytes(), col.getBytes());
			}
			table.delete(del);
			
			Put p = new Put(row,lock);
			for(String col:cols.keySet()) {
				byte[] value = cols.get(col);
				p.add(family.getBytes(), col.getBytes(), value);
			}
			table.put(p);
			table.flushCommits();
			isSuccess = true;
		}catch(Exception e){
			log.debug("update",e);
		} finally{			
			releaseTable(table);
		}
		return isSuccess;
	}
	
}
