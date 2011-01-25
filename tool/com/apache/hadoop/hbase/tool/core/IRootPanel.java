package com.apache.hadoop.hbase.tool.core;


public interface IRootPanel {
	public void update();
	
	public void startPanel();
	
	public void clearPanel();
	
	public void closePanel();
	
	public UIResult checkAvailable();
	
	public boolean doConfirm();
}
