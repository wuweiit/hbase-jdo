package com.apache.hadoop.hbase.tool.view.table;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.apache.hadoop.hbase.tool.view.AbstractHPanel;
import com.apache.hadoop.hbase.tool.view.comp.table.HJTableScrollPane;
import com.apache.hadoop.hbase.tool.view.comp.table.ISelectedRowListener;
import com.apache.hadoop.hbase.tool.view.processor.TableDetailInfo;
import com.apache.hadoop.hbase.tool.view.processor.TableInfo;
import com.apache.hadoop.hbase.tool.view.processor.TableProcessor;
import javax.swing.JLabel;

import org.apache.hadoop.hbase.HBaseConfiguration;

public class TableMainView extends AbstractHPanel {
	private JPanel panelTopMenu;
	private JButton buttonAllTables;
	private JButton btnCreate;
	private JButton btnDelete;
	private JSplitPane splitPane;
	private JPanel panelMiddle;
	private JPanel panelTableMenu;
	private JPanel panelBottom;

	private HJTableScrollPane<TableInfo> infoTablePane;
	private HJTableScrollPane<TableDetailInfo> infoColumnPane;
	private JLabel lblGetColumnInformation;
	private JLabel labelServer;
	/**
	 * Create the panel.
	 */
	public TableMainView() {
		initialize();
		test();
	}
	
	private void test() {
		infoTablePane.loadTestData(10);
//		infoTablePane.loadTestData(10);
	}
	
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getPanelTopMenu(), BorderLayout.NORTH);
		add(getSplitPane(), BorderLayout.CENTER);
	}

	private JPanel getPanelTopMenu() {
		if (panelTopMenu == null) {
			panelTopMenu = new JPanel();
			FlowLayout fl_panelTopMenu = (FlowLayout) panelTopMenu.getLayout();
			fl_panelTopMenu.setAlignment(FlowLayout.RIGHT);
			panelTopMenu.add(getLabelServer());
			panelTopMenu.add(getButtonAllTables());
			panelTopMenu.add(getBtnCreate());
			panelTopMenu.add(getBtnDelete());
		}
		return panelTopMenu;
	}
	private JButton getButtonAllTables() {
		if (buttonAllTables == null) {
			buttonAllTables = new JButton("All Tables");
			buttonAllTables.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					TableProcessor proc = new TableProcessor();
					List<TableInfo> list = proc.getTableDesc();
					if(list==null || list.size()==0) {
						showSimpleDialog("No data");
					}else{
						infoTablePane.loadModelData(proc.getTableDesc());
					}
				}
			});
		}
		return buttonAllTables;
	}
	private JButton getBtnCreate() {
		if (btnCreate == null) {
			btnCreate = new JButton("Create");
		}
		return btnCreate;
	}
	private JButton getBtnDelete() {
		if (btnDelete == null) {
			btnDelete = new JButton("Delete");
		}
		return btnDelete;
	}
	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new JSplitPane();
			splitPane.setDividerLocation(200);
			splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			splitPane.setTopComponent(getPanelMiddle());
			splitPane.setBottomComponent(getPanelBottom());
		}
		return splitPane;
	}
	private JPanel getPanelMiddle() {
		if (panelMiddle == null) {
			panelMiddle = new JPanel();
			panelMiddle.setLayout(new BorderLayout(0, 0));
			
			TableListModel model = new TableListModel();
			infoTablePane = new HJTableScrollPane<TableInfo>(model, new ISelectedRowListener<TableInfo>() {
				@Override
				public void selected(int row, TableInfo data) {
					String tableName = data.getName();
					TableProcessor proc = new TableProcessor();
					List<TableDetailInfo> list = proc.getTableDetailInfo(tableName);
					infoColumnPane.loadModelData(list);
				}
			});
			panelMiddle.add(infoTablePane, BorderLayout.CENTER);
			
		}
		return panelMiddle;
	}
	private JPanel getPanelTableMenu() {
		if (panelTableMenu == null) {
			panelTableMenu = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelTableMenu.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			panelTableMenu.add(getLblGetColumnInformation());
		}
		return panelTableMenu;
	}
	private JPanel getPanelBottom() {
		if (panelBottom == null) {
			panelBottom = new JPanel();
			panelBottom.setLayout(new BorderLayout(0, 0));
			panelBottom.add(getPanelTableMenu(), BorderLayout.NORTH);
			
			TableFamilyModel model = new TableFamilyModel();
			infoColumnPane = new HJTableScrollPane<TableDetailInfo>(model,null);
			
			panelBottom.add(infoColumnPane, BorderLayout.CENTER);
		}
		return panelBottom;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startPanel() {
		HBaseConfiguration conf = new HBaseConfiguration();
		
		labelServer.setText("Quorum Server="+conf.get("hbase.zookeeper.quorum")
				+":"+conf.get("hbase.zookeeper.property.clientPort"));
	}

	@Override
	public void clearPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closePanel() {
		// TODO Auto-generated method stub
		
	}
	private JLabel getLblGetColumnInformation() {
		if (lblGetColumnInformation == null) {
			lblGetColumnInformation = new JLabel("get column information from table 1 row.");
		}
		return lblGetColumnInformation;
	}
	private JLabel getLabelServer() {
		if (labelServer == null) {
			labelServer = new JLabel("New label");
		}
		return labelServer;
	}
}
