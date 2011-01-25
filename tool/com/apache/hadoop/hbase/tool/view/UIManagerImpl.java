package com.apache.hadoop.hbase.tool.view;

import javax.swing.JFrame;

import com.apache.hadoop.hbase.tool.core.AbstractUIManager;

public class UIManagerImpl extends AbstractUIManager{
	private static UIManagerImpl mgr;
	private JFrame frame;
	
	private UIManagerImpl(){
	}
	
	public static UIManagerImpl get(){
		synchronized(AbstractUIManager.class) {
			if(mgr==null) mgr= new UIManagerImpl();
			return mgr;
		}
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	
}
