package com.apache.hadoop.hbase.tool.core;

public enum UIResult {
	SUCCESS("SUCCESS"),
	FAIL("FAIL"),
	NOT_READY("NOT_READY");
	;
	private final String msg;
	
	private UIResult(String msg){
		this.msg=msg;
	}

	public String getMsg() {
		return msg;
	}
	
	public boolean isGood(){
		return equals(UIResult.SUCCESS);
	}
	
}
