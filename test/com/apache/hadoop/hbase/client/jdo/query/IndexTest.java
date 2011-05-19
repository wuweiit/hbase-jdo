package com.apache.hadoop.hbase.client.jdo.query;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTable;
import org.apache.hadoop.hbase.util.Bytes;

import com.apache.hadoop.hbase.client.jdo.AbstractHBaseDBO;
import com.apache.hadoop.hbase.client.jdo.HBaseDBOImpl;


public class IndexTest extends TestCase{
	public void testIndex() throws IOException {
//		String tableName = "test_index";
//		System.out.println(Arrays.toString("aa".getBytes()));
//		System.out.println(Arrays.toString("bb".getBytes()));
//		System.out.println(Arrays.toString(Bytes.add("aa".getBytes(),"bb".getBytes())));
//		AbstractHBaseDBO dbo = new HBaseDBOImpl();
//		if(dbo.isTableExist(tableName)) {
//			dbo.deleteTable(tableName);
//		}
//		dbo.createIndexTable("test_index",HBaseOrder.ASC,"user");
//		dbo.addIndexExistingTable(tableName,"user","id");
//
//		IndexedTable table = new IndexedTable(dbo.getConfig(),tableName.getBytes());
//		Put put = new Put("key".getBytes());
//		put.add("user".getBytes(),"id".getBytes(),"value".getBytes());
//		table.put(put);
//		
		HTable indexTable = new HTable("test_index-id");
		
		/*
		 * IndexTable's index key
		 * 
		 * index row = column value + row
		 * 
		 */
		Scan scan = new Scan();
		ResultScanner rs = indexTable.getScanner(scan);
		for(Result r:rs){
			System.out.println(Bytes.toString(r.getRow()));
		}
		
		
	}
}

