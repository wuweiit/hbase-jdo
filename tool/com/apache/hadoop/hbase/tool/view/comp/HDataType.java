package com.apache.hadoop.hbase.tool.view.comp;

public enum HDataType {
	STRING(String.class),
	BYTE(Byte.class),
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
	
	public static HDataType toSizeType(int length){
		if(length==1){
			return HDataType.BYTE;
		}else if(length==2){
			return HDataType.SHORT;
		}else if(length==4){
			return HDataType.INT;
		}else if(length==8){
			return HDataType.LONG;
		}else{
			return HDataType.STRING;
		}
	}
}
