package com.apache.hadoop.hbase.tool.view.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTable;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.apache.hadoop.hbase.client.jdo.HBaseDBOImpl;
import com.apache.hadoop.hbase.client.jdo.IHBaseLog;
import com.apache.hadoop.hbase.client.jdo.util.HConfigUtil;

public class TableProcessor implements IHBaseLog{
	
	public void deleteAllTables(){
		List<TableInfo> list = getTableDesc();
		HBaseDBOImpl dbo = new HBaseDBOImpl();
		for(TableInfo info:list){
			dbo.deleteTable(false,info.getName());
		}
	}
	
	/**
	 * check to connect to server.
	 * @return
	 */
	public boolean isConnectable(){
		try {
			HBaseConfiguration config = HConfigUtil.makeHBaseConfig();
			config.setInt("hbase.client.retries.number",1);
			HBaseAdmin admin = new HBaseAdmin(config);
			return true;
		} catch (Exception e) {
			log.error("isConnectable",e);
			return false;
		}
	}
	/**
	 * get Table information.
	 * @return
	 */
	public List<TableInfo> getTableDesc(){
		HBaseAdmin admin = null;
		List<TableInfo> list = null;
		try {
			admin = new HBaseAdmin(HConfigUtil.makeHBaseConfig());
			HTableDescriptor[] desc = admin.listTables();
			for(HTableDescriptor de:desc){
				if(list==null) list = new ArrayList<TableInfo>();
				TableInfo info = new TableInfo();
				info.setFamilycount(de.getFamilies().size());
				info.setName(Bytes.toString(de.getName()));
				info.setMaxFileSize(de.getMaxFileSize());
				info.setMemStoreFlushSize(de.getMemStoreFlushSize());
				log(info);
				java.util.Collection<HColumnDescriptor> cols = de.getFamilies();
				String[] fnames = new String[cols.size()];
				int i=0;
				for(HColumnDescriptor hd:cols){
					fnames[i]= Bytes.toString(hd.getName());
					i++;
				}
				info.setFamilies(fnames);
				list.add(info);
			}
		} catch (IOException e) {
			log.error("table desc",e);
		}
		return list; 
	}
	
	/**
	 * get table rows.
	 * @param tableName table name
	 * @param family family name
	 * @param startRowKey start position
	 * @param limit row count.
	 * @return
	 */
	public List<TableDataBean> getTableData(String tableName, String family, byte[] startRowKey, int limit) {
		IndexedTable table = null;
		List<TableDataBean> list = new ArrayList<TableDataBean>();
		try {
			table = new IndexedTable(HConfigUtil.makeHBaseConfig(),tableName.getBytes());
			PageFilter pageFilter = new PageFilter(limit);
			Scan scan = new Scan();
			scan.setCaching(100);
			if(startRowKey!=null) {
				scan.setStartRow(startRowKey);
			}
			scan.setFilter(pageFilter);
			scan.addFamily(family.getBytes());
			
			ResultScanner rs = table.getScanner(scan);			
			for(Result r:rs) {
				if(r==null) continue;
				
				TableDataBean info = new TableDataBean();
				info.setRow(r.getRow());
				info.setTable(tableName);				
				info.setFamily(family);
				
				NavigableMap<byte[],NavigableMap<byte[],byte[]>> map = r.getNoVersionMap();
				NavigableMap<byte[],byte[]> map2 = map.get(family.getBytes());
				Set<byte[]> columns = map2.keySet();
				Map<String,byte[]> columData = new HashMap<String, byte[]>();
				for(byte[] col:columns){
					byte[] value = map2.get(col);
					String colName = Bytes.toString(col);		
					columData.put(colName,value);
				}
				
				info.setColumns(columData);
				list.add(info);
				
				log(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
			table = new HTable(HConfigUtil.makeHBaseConfig(),tableName);
			Scan scan = new Scan();
			ResultScanner rs = table.getScanner(scan);
			Result r = rs.next();
			log(r);
			
			NavigableMap<byte[], NavigableMap<byte[], byte[]>> mv = r.getNoVersionMap();
			
			for(byte[] f:mv.keySet()){
				TableDetailInfo info = new TableDetailInfo();
				info.setName(tableName);
				
				info.setFamily(Bytes.toString(f));
				
				Map<String,byte[]> columns = new HashMap<String, byte[]>();
				NavigableMap<byte[], byte[]> mv2 = mv.get(f);
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
//		tp.getTableDetailInfo("user_info2");
		tp.getTableData("member","user", null,10);
	}
	
	public static void log(Object... strs){
		StringBuilder sb = new StringBuilder();
		for(Object s:strs) sb.append(s);
		System.out.println(sb.toString());
	}
}
