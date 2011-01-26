package com.apache.hadoop.hbase.tool.view.comp.table;

import java.io.Serializable;

public abstract class HTableColumn implements Serializable {
	
	private int index;
	private int width;
	private String id;
	private String columnName;
	
	protected HTableColumn(int index,String id, String columnName, int width) {
		this.index = index;
		this.id = id;
		this.columnName = columnName;
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return columnName;
	}
	
	public String getId() {
		return id;
	}
	
	public static HTableColumn get(HTableColumn[] values,int index){
		for(HTableColumn col:values){
			if(col.getIndex()==index) return col;
		}
		return null;
	}
}
