package com.apache.hadoop.hbase.client.jdo.examples;
import com.apache.hadoop.hbase.client.jdo.AbstractHBaseBean;
import com.apache.hadoop.hbase.client.jdo.anotation.Column;
import com.apache.hadoop.hbase.client.jdo.anotation.Index;


/**
 * Test UserBean
 * @author ncanis
 *
 */
public class UserBean extends AbstractHBaseBean{
	@Column@Index
	private String id;
	@Column
	private String password;
	@Column@Index
	private String name;
	@Column
	private String email;
	@Column
	private int age;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	
}
