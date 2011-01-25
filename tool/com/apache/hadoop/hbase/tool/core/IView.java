package com.apache.hadoop.hbase.tool.core;

public interface IView {
	public int getIndex();
	public IRootPanel make();
}
