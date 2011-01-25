package com.apache.hadoop.hbase.tool.view.table;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.apache.hadoop.hbase.tool.core.UIResult;
import com.apache.hadoop.hbase.tool.view.AbstractHPanel;

public class HTableView extends AbstractHPanel{

	private static final long serialVersionUID = 1L;
	
	private JPanel pane_guild_rank = null;
	private JTabbedPane tpane_stat = null;
	
	public HTableView(){
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setLayout(new BorderLayout());
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void startPanel() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void clearPanel() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void closePanel() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public UIResult checkAvailable() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean doConfirm() {
		// TODO Auto-generated method stub
		return false;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
