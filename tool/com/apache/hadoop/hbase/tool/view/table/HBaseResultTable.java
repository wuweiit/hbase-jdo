package com.apache.hadoop.hbase.tool.view.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.apache.hadoop.hbase.tool.view.AbstractHPanel;

/**
 * 통계 조회 결과 테이블
 */
public class HBaseResultTable extends AbstractHPanel{
	private static final long serialVersionUID = 1L;
	private HBaseTableModel statModel;
	private JScrollPane spane_stat = null;
	private JTable table_stat = null;
	private JScrollPane jScrollPane = null;
	private JTextArea jTextArea = null;
	private JSplitPane jSplitPane = null;
	public HBaseResultTable() {
		super();
		initialize();
	}
	
	
	//////////////////////////////////////////////////////////
	@Override
	public void initStatus(int status) {
		
	}
	
	public void setData(HTableInfo statInfo){
		statModel.setData(statInfo);
		statModel.fireTableDataChanged();
		statModel.fireTableStructureChanged();
		this.repaint();
	}
	
	//////////////////////////////////////////////////////////
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(1024, 301));
        this.add(getJSplitPane(), BorderLayout.CENTER);
         test();
			
	}
	
	public HTableInfo getData() {
		return statModel.getData();
	}

	private void test(){
		HTableInfo statInfo = new HTableInfo();
		for(int i=0;i<13;i++) {
			statInfo.getColumns().add("Col-"+i);
		}
		setData(statInfo);
	}

	/**
	 * This method initializes spane_gmlist	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getSpane_gmlist() {
		if (spane_stat == null) {
			spane_stat = new JScrollPane();
			spane_stat.setViewportView(getTable_gmlist());
		}
		return spane_stat;
	}
	
	public void setColSize(HTableInfo info){
		List<String> cols= info.getColumns();
		int i=0;
		for(String col:cols) {
			TableColumn tcol = table_stat.getTableHeader().getColumnModel().getColumn(i);
			tcol.setPreferredWidth(100);
			tcol.setMinWidth(100);
			i++;
		}
	}
	/**
	 * This method initializes table_gmlist	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getTable_gmlist() {
		if (table_stat == null) {
			table_stat = new JTable();
			
			statModel = new HBaseTableModel();
			table_stat.setModel(statModel);
			
			table_stat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table_stat.setFont(new Font("굴림", Font.PLAIN, 11));
			table_stat.setRowHeight(18);
			table_stat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			
			table_stat.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {
						@Override
						public void valueChanged(ListSelectionEvent e) {
							ListSelectionModel model = (ListSelectionModel)e.getSource();

							if(model.isSelectionEmpty()==false) {
								if(e.getValueIsAdjusting()==false) {
									int row = table_stat.getSelectedRow();;
									jTextArea.setText(statModel.getRowString(row));
								}
							}
						}
						
					});
		}
		return table_stat;
	}


	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new Dimension(170, 50));
			jScrollPane.setViewportView(getJTextArea());
		}
		return jScrollPane;
	}


	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
		}
		return jTextArea;
	}


	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setDividerLocation(180);
			jSplitPane.setLeftComponent(getJScrollPane());
			jSplitPane.setRightComponent(getSpane_gmlist());
		}
		return jSplitPane;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
