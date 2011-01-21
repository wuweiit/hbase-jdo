package com.apache.hadoop.hbase.client.jdo;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		
		System.setErr(ps);

		new Exception("aaa").printStackTrace();
	}

}
