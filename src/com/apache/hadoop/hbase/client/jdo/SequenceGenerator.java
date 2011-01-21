package com.apache.hadoop.hbase.client.jdo;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.apache.hadoop.hbase.client.jdo.query.HBaseOrder;

/**
 * SequenceGenerator.
 * you can create unique row key by using sequence like RDB
 * 
 * SequenceGenerator seq = new SequenceGenerator();
   seq.makeNextUniqueKey("test");
		
 * @author ncanis
 *
 */
public class SequenceGenerator implements IHBaseLog{
	private static final String SEQUENCE_TABLE="jdo.sequence";
	private static final String SEQUENCE_FAMILY="jdo.seq.table";
	private static final String SEQUENCE_ID="jdo.seq.id";
	private static final String SEQUENCE_ORDER="jdo.seq.order";
	
	private AbstractHBaseDBO dbo = null;
	private static boolean IS_INIT = false;
	public SequenceGenerator(AbstractHBaseDBO dbo){
		this.dbo = dbo;
		checkSequenceTable();
	}

	private void checkSequenceTable() {		
		if(IS_INIT==false) {
			dbo.createTableIfNotExist(SEQUENCE_TABLE, HBaseOrder.NONE, SEQUENCE_FAMILY);
		}
		IS_INIT = true;
	}
	
	public long makeNextUniqueKey(String tableName){
		long nextValue = 0L;
		try{
			HTable table = new HTable(SEQUENCE_TABLE);
			HBaseOrder ho = getOrder(table,tableName.getBytes());
			int value = ho==HBaseOrder.DESC ? -1:1;
			nextValue = table.incrementColumnValue(
					tableName.getBytes(),
					SEQUENCE_FAMILY.getBytes(),
					SEQUENCE_ID.getBytes(),
					value
			);
		}catch(Exception e){
			log.error("Cannot increment next key. table="+tableName,e);
		}
		return nextValue;
	}
	
	protected HBaseOrder getOrder(HTable table, byte[] row) throws IOException{
		Get get = new Get(row);
		Result result = table.get(get);
		long id = Bytes.toLong(result.getValue(SEQUENCE_FAMILY.getBytes(), SEQUENCE_ID.getBytes()));
		String orderType = Bytes.toString(result.getValue(SEQUENCE_FAMILY.getBytes(), SEQUENCE_ORDER.getBytes()));
		return HBaseOrder.getOrder(orderType);
	}
	
	public void createSequence(String tableName,HBaseOrder order) throws IOException{
		HTable table = new HTable(SEQUENCE_TABLE);
		long value = order==HBaseOrder.DESC ? Long.MAX_VALUE:Long.MIN_VALUE;
		Put p = new Put(tableName.getBytes());
		p.add(SEQUENCE_FAMILY.getBytes(), SEQUENCE_ID.getBytes(), Bytes.toBytes(value));
		p.add(SEQUENCE_FAMILY.getBytes(), SEQUENCE_ORDER.getBytes(), order.getName().getBytes());
		table.put(p);
	}
}
