package com.apache.hadoop.hbase.tool.core;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ConsoleStream extends FilterOutputStream{
	private IConsoleHandler handler;
	public ConsoleStream(OutputStream out,IConsoleHandler handler) {
		super(out);
		this.handler = handler;
	}
	@Override
	public void write(byte b[]) throws IOException {
		handler.message(new String(b));
	}

	@Override
	public void write(byte b[], int off, int len) throws IOException {
		handler.message(new String(b, off, len));
	}

	@Override
	public void write(int b) throws IOException {
		handler.message(new String(new char[] { (char) b }));
	}

}