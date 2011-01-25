package com.apache.hadoop.hbase.tool.view.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class HTableInfo {
	private List<String> columns = null; 
	private List<List<String>> values = null;
	
	public void copy(HTableInfo data) {
		HTableInfo log = (HTableInfo)data;
		this.columns = log.columns;
		this.values = log.values;
	}
	
	public HTableInfo() {
		columns = new ArrayList<String>();
		values = new ArrayList<List<String>>();
	}
	
	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public List<List<String>> getValues() {
		return values;
	}

	public void setValues(List<List<String>> values) {
		this.values = values;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public void clear() {
		this.columns.clear();
		this.values.clear();
	}
}
