package com.apache.hadoop.hbase.tool.view.hadoop;

import org.apache.hadoop.fs.FileStatus;

public class FileInfo {
	private FileStatus fileStatus;

	public FileInfo(FileStatus fileStatus){
		this.fileStatus = fileStatus;		
	}
	public FileStatus getFileStatus() {
		return fileStatus;
	}
	@Override
	public String toString() {
		return fileStatus.getPath().getName();
	}
}
