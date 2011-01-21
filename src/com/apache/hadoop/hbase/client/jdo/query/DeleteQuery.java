package com.apache.hadoop.hbase.client.jdo.query;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTable;
import org.apache.hadoop.hbase.client.transactional.TransactionState;

import com.apache.hadoop.hbase.client.jdo.AbstractHBaseDBO;

/**
 * Deletion Query methods
 * @author ncanis
 *
 */
public class DeleteQuery extends HBQuery{
	
	public DeleteQuery(AbstractHBaseDBO dbo, String tableName) {
		super(dbo, tableName);
	}
	
	/**
	 * delete rows
	 * @param rows row keys
	 * @return
	 */
	public boolean deleteRow(byte[] ... rows){
		IndexedTable table = null;
		try{
			table = borrowTable();
			return deleteRow(table,null,rows);
		} catch (Exception e) {
			log.error("delete row",e);
		} finally{
			releaseTable(table);
		}
	
		return false;
	}
	
	/**
	 * delete rows in Transaction mode.
	 * @param state TransactionState
	 * @param rowKeys
	 * @return
	 * @throws IOException 
	 */
	public boolean deleteRow(IndexedTable table,TransactionState state,byte[] ... rowKeys) throws IOException{
		for(byte[] row:rowKeys) {
			Delete del = new Delete(row);
			if(state==null) {
				table.delete(del);
			}else{
				table.delete(state,del);				
			}
		}
		return true;
	}
}
