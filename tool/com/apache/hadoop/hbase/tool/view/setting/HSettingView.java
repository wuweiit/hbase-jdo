package com.apache.hadoop.hbase.tool.view.setting;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.hadoop.hbase.HBaseConfiguration;

import com.apache.hadoop.hbase.tool.view.AbstractHPanel;
import com.apache.hadoop.hbase.tool.view.comp.table.HJTablePanel;

public class HSettingView extends AbstractHPanel{

	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPane;
	private JPanel panelHBase;
	private JPanel panelHadoop;
	
	private HJTablePanel<HConfigInfo> hbase;
	private HJTablePanel<HConfigInfo> hadoop;
	private TableConfigModel hbaseModel;
	private TableConfigModel hadoopModel;
	
	public HSettingView(){
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setLayout(new BorderLayout());
		add(getTabbedPane(), BorderLayout.CENTER);
		
		//test
//		hbaseModel.loadTestData(10);
		loadData();
	}
	
	private void loadData(){
		HBaseConfiguration config = new HBaseConfiguration();
		Iterator<Entry<String, String>> it = config.iterator();
		
		List<HConfigInfo> listHBase = new ArrayList<HConfigInfo>();
		List<HConfigInfo> listHadoop = new ArrayList<HConfigInfo>();
		
		while(it.hasNext()){
			Entry<String, String> entry = it.next();
			String name = entry.getKey();
			String value = entry.getValue();
			
			HConfigInfo info = new HConfigInfo();
			info.setName(name);
			info.setValue(value);
			
			if(name.startsWith("hbase")) {
				listHBase.add(info);
			}else{
				listHadoop.add(info);
			}
		}
		
		hbase.loadModelData(listHBase);
		hadoop.loadModelData(listHadoop);
	}
	
	
	
	@Override
	public void update() {
	}
	@Override
	public void startPanel() {
	}
	@Override
	public void clearPanel() {
	}
	@Override
	public void closePanel() {
	}
	@Override
	public boolean doConfirm() {
		return false;
	}
	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.addTab("HBase", null, getPanelHBase(), null);
			tabbedPane.addTab("Hadoop", null, getPanelHadoop(), null);
		}
		return tabbedPane;
	}
	private JPanel getPanelHBase() {
		if (panelHBase == null) {
			panelHBase = new JPanel();
			panelHBase.setLayout(new BorderLayout(0, 0));
			
			hbaseModel = new TableConfigModel();
			hbase = new HJTablePanel<HConfigInfo>(hbaseModel, null);
			
			panelHBase.add(hbase,BorderLayout.CENTER);
		}
		return panelHBase;
	}
	private JPanel getPanelHadoop() {
		if (panelHadoop == null) {
			panelHadoop = new JPanel();
			panelHadoop.setLayout(new BorderLayout(0, 0));
			
			hadoopModel = new TableConfigModel();
			hadoop = new HJTablePanel<HConfigInfo>(hadoopModel, null);
			
			panelHadoop.add(hadoop,BorderLayout.CENTER);
		}
		return panelHadoop;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
