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
	@Column
	private String age;
	@Column
	private String mail;
	@Column
	private String address;
	@Column
	private String sex;
	@Column
	private String nick;
	@Column
	private String height;
	
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

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
	
	
}
