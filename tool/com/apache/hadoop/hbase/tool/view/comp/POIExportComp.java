package com.apache.hadoop.hbase.tool.view.comp;

public class POIExportComp {
	private String id;
	private int pass;	
	private short age;
	private long date;

	
	public POIExportComp(){}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getPass() {
		return pass;
	}
	public void setPass(int pass) {
		this.pass = pass;
	}
	
	public String toString(){
		return id+","+pass;
	}
	public short getAge() {
		return age;
	}
	public void setAge(short age) {
		this.age = age;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	
}
