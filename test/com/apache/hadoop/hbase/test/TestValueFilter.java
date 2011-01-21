package com.apache.hadoop.hbase.test;

import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.WritableByteArrayComparable;

public class TestValueFilter extends CompareFilter {
	public TestValueFilter() {
		super(CompareOp.EQUAL, new TestComparator());
	}
}

class TestComparator extends WritableByteArrayComparable{
	@Override
	public int compareTo(byte[] value) {
//		return super.compareTo(value);
		return 0;
	}
	
}
