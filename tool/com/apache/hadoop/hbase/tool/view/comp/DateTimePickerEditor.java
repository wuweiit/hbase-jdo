package com.apache.hadoop.hbase.tool.view.comp;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.apache.hadoop.hbase.client.jdo.util.HUtil;

public class DateTimePickerEditor extends AbstractCellEditor implements
		TableCellEditor, ActionListener {

	private static final long serialVersionUID = 1L;
	protected static final String EDIT = "edit";
	
	private JButton button;
	private DateTimePicker dateTimePicker;
	private Calendar c;
	private JDialog dialog;
	public DateTimePickerEditor(){
		button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);
        
		dateTimePicker = new DateTimePicker();
		dialog = new JDialog();
		dialog.add(new JButton("DDD"));
		
		
		
	}
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if ( value.equals("")){
		
		}
		button.setText("AAAAAAA");
		return button;
	}

	@Override
	public Object getCellEditorValue() {
		return "AAAAAA";
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dialog.setVisible(true);
		if ( EDIT.equalsIgnoreCase(e.getActionCommand())){
			button.setText(HUtil.makeSimpleDate(dateTimePicker.getDate()));
		}else{
			dateTimePicker.setVisible(false);
		}
	}

}
