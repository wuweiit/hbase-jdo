package com.apache.hadoop.hbase.tool.view.processor;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class TableInfo {
	private String name;
	private long maxFileSize;
	private long memStoreFlushSize;
	private int familycount;
	private String[] families;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getMaxFileSize() {
		return maxFileSize;
	}
	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}
	public long getMemStoreFlushSize() {
		return memStoreFlushSize;
	}
	public void setMemStoreFlushSize(long memStoreFlushSize) {
		this.memStoreFlushSize = memStoreFlushSize;
	}
	public int getFamilycount() {
		return familycount;
	}
	public void setFamilycount(int familycount) {
		this.familycount = familycount;
	}
	

	public String[] getFamilies() {
		return families;
	}
	public void setFamilies(String[] families) {
		this.families = families;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
}
