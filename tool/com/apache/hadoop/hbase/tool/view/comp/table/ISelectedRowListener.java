package com.apache.hadoop.hbase.tool.view.comp.table;

public interface ISelectedRowListener<T> {
	public void selected(int row,T data);
}
