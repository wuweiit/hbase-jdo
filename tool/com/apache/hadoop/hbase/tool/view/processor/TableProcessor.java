package com.apache.hadoop.hbase.tool.view.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.apache.hadoop.hbase.client.jdo.IHBaseLog;

public class TableProcessor implements IHBaseLog{
	
	public List<TableInfo> getTableDesc(){
		HBaseAdmin admin = null;
		List<TableInfo> list = null;
		try {
			admin = new HBaseAdmin(new HBaseConfiguration());
			HTableDescriptor[] desc = admin.listTables();
			for(HTableDescriptor de:desc){
				if(list==null) list = new ArrayList<TableInfo>();
				TableInfo info = new TableInfo();
				info.setFamilycount(de.getFamilies().size());
				info.setName(Bytes.toString(de.getName()));
				info.setMaxFileSize(de.getMaxFileSize());
				info.setMemStoreFlushSize(de.getMemStoreFlushSize());
				log(info);
				list.add(info);
			}
		} catch (IOException e) {
			log.error("table desc",e);
		}
		return list; 
	}
	
	/**
	 * get table 1 row per family.
	 * @param tableName
	 * @return
	 * @throws IOException
	 */
	public List<TableDetailInfo> getTableDetailInfo(String tableName){
		HTable table = null;
		List<TableDetailInfo> list = new ArrayList<TableDetailInfo>();
		try {
			table = new HTable(tableName);
			Scan scan = new Scan();
			ResultScanner rs = table.getScanner(scan);
			Result r = rs.next();
			log(r);
			
			NavigableMap<byte[], NavigableMap<byte[], byte[]>> mv = r.getNoVersionMap();
			
			for(byte[] f:mv.keySet()){
				TableDetailInfo info = new TableDetailInfo();
				info.setName(tableName);
				
				info.setFamily(Bytes.toString(f));
				NavigableMap<byte[], byte[]> mv2 = mv.get(f);
				
				Map<String,byte[]> columns = new HashMap<String, byte[]>();
				for(byte[] col:mv2.keySet()) {
					columns.put(Bytes.toString(col),mv2.get(col));
				}
				info.setColumns(columns);
				list.add(info);
				
				log(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static void main(String[] args) throws IOException {
//		HBaseAdmin admin = new HBaseAdmin(new HBaseConfiguration());
//		HTableDescriptor desc = new HTableDescriptor("test2");
//
//		String[] family={"user","account"};
//		for (String f : family) {
//			HColumnDescriptor col = new HColumnDescriptor(f.getBytes());
//			desc.addFamily(col);
//		}
//		admin.createTable(desc);
		
		TableProcessor tp = new TableProcessor();
//		tp.getTableDesc();
		tp.getTableDetailInfo("user_info2");
	}
	
	public static void log(Object... strs){
		StringBuilder sb = new StringBuilder();
		for(Object s:strs) sb.append(s);
		System.out.println(sb.toString());
	}
}
