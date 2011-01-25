package com.apache.hadoop.hbase.tool.view.comp;


/**
 * Excel 헤더
 * @author ncanis
 *
 */
public class ExcelHeader {
	private String key;
	private int width;
	private String name;
	private int type;
	
	public ExcelHeader(String key,int width, String name){
		this.key = key;
		this.width = width;
		this.name = name;
	}
	
	public ExcelHeader(String key,int width, String name,int toType){
		this.key = key;
		this.width = width;
		this.name = name;
		this.type = toType;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
