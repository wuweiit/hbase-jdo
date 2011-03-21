package com.apache.hadoop.hbase.test;

import com.apache.hadoop.hbase.client.jdo.AbstractHBaseBean;
import com.apache.hadoop.hbase.client.jdo.anotation.Column;
import com.apache.hadoop.hbase.client.jdo.anotation.Index;

public class TestTableBean extends AbstractHBaseBean{
	@Column @Index
	private int aaa;
	@Column
	private int bbb;
	@Column
	private boolean ccc;
	public int getAaa() {
		return aaa;
	}
	public void setAaa(int aaa) {
		this.aaa = aaa;
	}
	public int getBbb() {
		return bbb;
	}
	public void setBbb(int bbb) {
		this.bbb = bbb;
	}
	public boolean isCcc() {
		return ccc;
	}
	public void setCcc(boolean ccc) {
		this.ccc = ccc;
	}
	
}
