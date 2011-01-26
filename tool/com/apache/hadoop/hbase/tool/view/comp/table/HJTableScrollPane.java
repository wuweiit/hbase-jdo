package com.apache.hadoop.hbase.tool.view.comp.table;

import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;


public class HJTableScrollPane<T> extends JScrollPane{
	private AbstractHTableModel<T> model;
	private T selectedObject;
	private JTable table = null;
	private ISelectedRowListener<T> listener;
	
	public HJTableScrollPane(AbstractHTableModel<T> model,ISelectedRowListener<T> listener){
		this.model = model;
		this.listener = listener;
		init();
		reStructure();
	}
	
	public void loadModelData(List<T> list){
		this.model.setData(list);
		reStructure();
		this.model.fireTableDataChanged();
		this.repaint();
	}
	
	private void reStructure(){
		this.model.fireTableStructureChanged();
		for(HTableColumn hc:model.columns) {				
			TableColumn col = table.getTableHeader().getColumnModel().getColumn(hc.getIndex());
			col.setPreferredWidth(hc.getWidth());
		}
	}
	
	private void init() {
		table = new JTable(model);
		for(HTableColumn hc:model.columns) {				
			TableColumn col = table.getTableHeader().getColumnModel().getColumn(hc.getIndex());
			col.setPreferredWidth(hc.getWidth());
		}
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(18);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		table.getSelectionModel().addListSelectionListener(
			new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					ListSelectionModel lsmodel = (ListSelectionModel)e.getSource();

					if(lsmodel.isSelectionEmpty()==false) {
						if(e.getValueIsAdjusting()==false) {
							int row = table.getSelectedRow();
							selectedObject = HJTableScrollPane.this.model.getData(row);
							if(listener!=null) listener.selected(row, selectedObject);
						}
					}
				}
				
			});
		
		this.setViewportView(table);
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
