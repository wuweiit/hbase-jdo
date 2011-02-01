package com.apache.hadoop.hbase.tool.core;

import javax.swing.JTextArea;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

public class Log4jLogAppender implements Appender{
	private JTextArea text;
	private Layout layout = null;
	
	public Log4jLogAppender(JTextArea text){
		this.text = text;
		this.layout = new org.apache.log4j.PatternLayout("%p[%d{yy/MM/dd HH:mm:ss}][%c{2}]: %m%n");
	}
	@Override
	public void addFilter(Filter arg0) {
	}

	@Override
	public void clearFilters() {
	}

	@Override
	public void close() {
	}

	@Override
	public void doAppend(LoggingEvent event) {
//		System.out.println("==>"+event.getMessage().toString());
		text.append(layout.format(event));		
	}

	@Override
	public ErrorHandler getErrorHandler() {
		return null;
	}

	@Override
	public Filter getFilter() {
		return null;
	}

	@Override
	public Layout getLayout() {
		return layout;
		
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}

	@Override
	public void setErrorHandler(ErrorHandler arg0) {
	}

	@Override
	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	@Override
	public void setName(String arg0) {
	}
	
}
