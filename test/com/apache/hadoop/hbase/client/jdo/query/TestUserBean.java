package com.apache.hadoop.hbase.client.jdo.query;

import com.apache.hadoop.hbase.client.jdo.AbstractHBaseBean;
import com.apache.hadoop.hbase.client.jdo.anotation.Column;
import com.apache.hadoop.hbase.client.jdo.anotation.Index;


/**
 * test bean class.
 * 
 * you must add annotation in fields.
 * the columns must be completed matched with hbase's table.
 * @author ncanis
 *
 */
public class TestUserBean extends AbstractHBaseBean{
	@Index @Column
	private String id;
	@Column
	private String name;
	
	public TestUserBean() {		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
