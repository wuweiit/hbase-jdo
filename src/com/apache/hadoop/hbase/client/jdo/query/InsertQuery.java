package com.apache.hadoop.hbase.client.jdo.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTable;
import org.apache.hadoop.hbase.client.transactional.TransactionState;
import org.apache.hadoop.hbase.util.Bytes;

import com.apache.hadoop.hbase.client.jdo.AbstractHBaseBean;
import com.apache.hadoop.hbase.client.jdo.AbstractHBaseDBO;
import com.apache.hadoop.hbase.client.jdo.query.HBaseBeanProcessor.ANT_TYPE;

/**
 * Insertion Query
 * @author ncanis
 *
 */
public class InsertQuery extends HBQuery{
	public InsertQuery(AbstractHBaseDBO dbo, String tableName) {
		super(dbo, tableName);
	}

	/**
	 * insert all columns data from specified bean information.
	 * default order is asc
	 * @param list
	 */
	public boolean insert(List<AbstractHBaseBean> list){
		return insert(list.toArray(new AbstractHBaseBean[list.size()]));
	}
	
	/**
	 * insert all columns data from specified bean information in transaction mode.
	 * default order is asc
	 * @param state
	 * @param list
	 */
	public boolean insert(IndexedTable table,TransactionState state,List<AbstractHBaseBean> list){
		
		return insert(table,state,list.toArray(new AbstractHBaseBean[list.size()]));
	}

	/**
	 * insert all columns data from specified bean information.
	 * default order is asc
	 * @param list
	 */
	public boolean insert(final AbstractHBaseBean... list){
		boolean isSuccess = false;
		IndexedTable table = null;
		try{
			table = borrowTable();
			isSuccess = insert(table,null,list);
		}catch(Exception e){
			log.error("work",e);
		}finally{
			releaseTable(table);
		}
		return isSuccess;
	}
	

	/**
	 * insert all columns data from specified bean information.
	 * @param state
	 * @param order
	 * @param list
	 */
	public boolean insert(IndexedTable table, TransactionState state, AbstractHBaseBean... list){
		boolean isSuccess=false;
		try {			
			List<Put> puts = null;
			for(AbstractHBaseBean bean: list) {
				long newRowKey = sequence.makeNextUniqueKey(tableName);
				Properties p =processor.toProperties(bean,ANT_TYPE.COLUMN);
				Put put = new Put(Bytes.toBytes(newRowKey));
				for(Object col:p.keySet()){
					byte[] value = (byte[])p.get(col.toString());
					put.add(bean.getFamily().getBytes(),
							Bytes.toBytes(col.toString()),
							value);					
				}
				bean.setRow(Bytes.toBytes(newRowKey));
				if(state!=null){
					table.put(state,put);
				}else{					
					if(puts==null) puts = new ArrayList<Put>();
					puts.add(put);
				}
			}
			if(state==null) {
				table.put(puts);
			}
			isSuccess = true;
		} catch (Exception e) {
			log.error("insert Bean",e);
		}
		return isSuccess;
	}
}
