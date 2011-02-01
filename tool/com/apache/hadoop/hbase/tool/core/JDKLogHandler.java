package com.apache.hadoop.hbase.tool.core;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.util.logging.ConsoleHandler;

import javax.swing.JTextArea;

public class JDKLogHandler extends ConsoleHandler{
	private JTextArea textArea;
	public JDKLogHandler(JTextArea textArea){
		super();		
		setOutputStream(new ConsoleStream());
	}
	class ConsoleStream extends FilterOutputStream{
		public ConsoleStream() {
			super(new ByteArrayOutputStream());
		}
		@Override
		public void write(byte b[]) throws IOException {
			textArea.append(new String(b));
		}
		
		@Override
		public void write(byte b[], int off, int len) throws IOException {
			textArea.append(new String(b, off, len));
		}
		
		@Override
		public void write(int b) throws IOException {
			textArea.append(new String(new char[] { (char) b }));
		}
		
	}
}
