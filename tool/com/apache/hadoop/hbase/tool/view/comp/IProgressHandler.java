package com.apache.hadoop.hbase.tool.view.comp;

public interface IProgressHandler {
	public void progress(int current, int length);
	public boolean keepGoing();
}
