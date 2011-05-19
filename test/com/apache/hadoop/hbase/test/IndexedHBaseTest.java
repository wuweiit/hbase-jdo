package com.apache.hadoop.hbase.test;

import java.io.IOException;

import org.apache.hadoop.hbase.client.transactional.HBaseBackedTransactionLogger;

import junit.framework.TestCase;


public class IndexedHBaseTest extends TestCase{
	public void testCall() throws IOException {
		HBaseBackedTransactionLogger.createTable();
	}
}
