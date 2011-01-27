package com.apache.hadoop.hbase.tool.view.processor;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class TableDataBean {
	private String table;
	private String family;
	private byte[] row;
	private Map<String,byte[]> columns;
	
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
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
	public int getColumnCount() {
		return columns==null? 0:columns.size();
	}
	public byte[] getRow() {
		return row;
	}
	public void setRow(byte[] row) {
		this.row = row;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
}
