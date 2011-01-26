package com.apache.hadoop.hbase.tool.view.processor;

import java.util.Map;

public class TableDetailInfo extends TableInfo{
	private String family;
	private Map<String,byte[]> columns;
	
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public Map<String, byte[]> getColumns() {
		return columns;
	}
	public void setColumns(Map<String, byte[]> columns) {
		this.columns = columns;
	}
	
	public int getColumnCount(){
		return columns.size();
	}
}
