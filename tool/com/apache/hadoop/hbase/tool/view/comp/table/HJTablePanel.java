package com.apache.hadoop.hbase.tool.view.comp.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.apache.hadoop.hbase.tool.view.comp.HDataType;


public class HJTablePanel<T> extends JPanel{
	private AbstractHTableModel<T> model;
	private T selectedObject;
	private JTable table = null;
	private ISelectedRowListener<T> listener;
	private boolean isTypeTable;
	private JTable typeTable = null;
	private ComboBoxTypeTableModel typeModel;
	
	public HJTablePanel(AbstractHTableModel<T> model,ISelectedRowListener<T> listener){
		this(model,listener,false);
	}
	public HJTablePanel(AbstractHTableModel<T> model,ISelectedRowListener<T> listener,boolean isTypeTable){
		this.model = model;
		this.listener = listener;
		this.isTypeTable = isTypeTable;
		init();
		reStructure();
	}
	private void init() {
		setLayout(new BorderLayout());
		add(makeDataTable(),BorderLayout.CENTER);
		if(isTypeTable) {
			add(makeTypeTable(),BorderLayout.EAST);
		}
	}
	public void loadModelData(List<T> list){
		this.model.setData(list);
		this.model.fireTableDataChanged();
		this.model.fireTableStructureChanged();
		reStructure();
		this.repaint();
		
		if(isTypeTable) {
			this.typeModel.setData(model.getColumns());
			this.typeModel.fireTableDataChanged();
		}
	}
	
	public JTable getJTable(){
		return table;
	}
	
	private void reStructure(){
		for(HTableColumn hc:model.columns) {				
			TableColumn col = table.getTableHeader().getColumnModel().getColumn(hc.getIndex());
			col.setPreferredWidth(hc.getWidth());
		}
	}
	
	private JScrollPane makeTypeTable(){
		typeTable = new JTable();
		typeTable.setRowHeight(25);
		typeModel = new ComboBoxTypeTableModel();
		typeTable.setModel(typeModel);
		
		JComboBox comboBox = new JComboBox(HDataType.values());
		DefaultCellEditor editor = new DefaultCellEditor(comboBox);
		
		typeTable.getColumnModel().getColumn(1).setCellEditor(editor);
		
		JScrollPane pane = new JScrollPane();
		pane.setSize(200,100);
		pane.setPreferredSize(new Dimension(200,100));
		pane.setViewportView(typeTable);
		return pane;
	}
	
	private JScrollPane makeDataTable(){
		table = new JTable(model);
		reStructure();
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(25);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		table.getSelectionModel().addListSelectionListener(
			new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					ListSelectionModel lsmodel = (ListSelectionModel)e.getSource();

					if(lsmodel.isSelectionEmpty()==false) {
						if(e.getValueIsAdjusting()==false) {
							int row = table.getSelectedRow();
							selectedObject = HJTablePanel.this.model.getData(row);
							if(listener!=null) listener.selected(row, selectedObject);
						}
					}
				}
				
			});
		
		JScrollPane pane = new JScrollPane();
		pane.setViewportView(table);
		return pane;
	}
	


	public AbstractHTableModel<T> getModel(){
		return (AbstractHTableModel<T>)table.getModel();
	}
	
	public void loadTestData(int count){
		model.loadTestData(count);
	}
	public Object getSelectedObject() {
		return selectedObject;
	}
}
