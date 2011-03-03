package com.apache.hadoop.hbase.test;

import com.apache.hadoop.hbase.client.jdo.AbstractHBaseBean;
import com.apache.hadoop.hbase.client.jdo.anotation.Column;
import com.apache.hadoop.hbase.client.jdo.anotation.Index;

public class TestBean extends AbstractHBaseBean{
	@Column @Index
	private String age;

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	
}
