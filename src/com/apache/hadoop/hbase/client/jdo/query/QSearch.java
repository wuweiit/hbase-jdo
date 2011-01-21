package com.apache.hadoop.hbase.client.jdo.query;

import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;

/**
 * Search Option Enum.
 * @author ncanis
 *
 */
public enum QSearch {
	EQUAL,
	NOT_EQUAL,
	LIKE,
	;
	
	/**
	 * convert Qsearch to CompareOp
	 * 
	 * @return
	 */
	public CompareOp toCompareOp() {
		if(this==QSearch.EQUAL) {
			return CompareOp.EQUAL;
		}else if(this==QSearch.NOT_EQUAL){
			return CompareOp.NOT_EQUAL;
		}else{
			return CompareOp.EQUAL;
		}
	}
}
