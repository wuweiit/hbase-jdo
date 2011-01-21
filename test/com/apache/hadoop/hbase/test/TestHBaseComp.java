package com.apache.hadoop.hbase.test;

import junit.framework.TestCase;

import org.apache.hadoop.hbase.util.Bytes;


public class TestHBaseComp extends TestCase{
	public void testCompare() throws Exception {
//		byte[] buffer ={0,1,2,3};
//		byte[] prefix ={0,1};
		byte[] buffer = "hi-1".getBytes();
		byte[] prefix = "1".getBytes();
		int cmp = Bytes.compareTo(buffer, 0, prefix.length, prefix, 0,
		        prefix.length);
		System.out.println(cmp);
	}
}
