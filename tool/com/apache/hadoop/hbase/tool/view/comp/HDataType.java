package com.apache.hadoop.hbase.tool.view.comp;

public enum HDataType {
	STRING(String.class),
	BYPE(Byte.class),
	SHORT(Short.class),
	INT(Integer.class),
	LONG(Long.class),
	FLOAT(Float.class),
	DOUBLE(Double.class),
	;
	
	private Class classType;
	private HDataType(final Class classType){
		this.classType = classType;
	}
	
	@Override
	public String toString() {
		return name();
	}
	
}
