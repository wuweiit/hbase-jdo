package com.apache.hadoop.hbase.test;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import junit.framework.TestCase;

public class HBaseConnectionTest extends TestCase {
	public void testConnection() throws Exception {
		HBaseConfiguration config = new HBaseConfiguration();
		
		config.setLong("hbase.client.pause", 1000L);
		config.setInt("hbase.client.retries.number", 1);
		
		System.out.println("quorum ip="+config.get("hbase.zookeeper.quorum"));
		System.out.println("quorum port="+config.get("hbase.zookeeper.property.clientPort"));
		
		HBaseAdmin hbase = new HBaseAdmin(config);
	}
}
