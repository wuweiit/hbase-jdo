package com.apache.hadoop.hbase.client.jdo.query;

public interface RowCountReceiver {
	public void receive(int totalCount);
}
