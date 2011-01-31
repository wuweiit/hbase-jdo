package com.apache.hadoop.hbase.tool.view.comp.table;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class TestFrame extends JFrame {

	  public TestFrame() {
	    super("EditableHeader Example");

	    JTable table = new JTable(7, 5);
	    TableColumnModel columnModel = table.getColumnModel();
	    table.setTableHeader(new EditableHeader(columnModel));

	    String[] items = { "Dog", "Cat" };
	    JComboBox combo = new JComboBox();
	    for (int i = 0; i < items.length; i++) {
	      combo.addItem(items[i]);
	    }
	    ComboRenderer renderer = new ComboRenderer(items);

	    EditableHeaderTableColumn col;
	    // column 1
	    col = (EditableHeaderTableColumn) table.getColumnModel().getColumn(1);
	    col.setHeaderValue(combo.getItemAt(0));
	    col.setHeaderRenderer(renderer);
	    col.setHeaderEditor(new DefaultCellEditor(combo));

	    // column 3
	    col = (EditableHeaderTableColumn) table.getColumnModel().getColumn(3);
	    col.setHeaderValue(combo.getItemAt(0));
	    //col.setHeaderRenderer(renderer);
	    col.setHeaderEditor(new DefaultCellEditor(combo));

	    JScrollPane pane = new JScrollPane(table);
	    getContentPane().add(pane);
	  }

	  class ComboRenderer extends JComboBox implements TableCellRenderer {

	    ComboRenderer(String[] items) {
	      for (int i = 0; i < items.length; i++) {
	        addItem(items[i]);
	      }
	    }

	    public Component getTableCellRendererComponent(JTable table,
	        Object value, boolean isSelected, boolean hasFocus, int row,
	        int column) {
	      setSelectedItem(value);
	      return this;
	    }
	  }

	  public static void main(String[] args) {
		  TestFrame frame = new TestFrame();
	    frame.addWindowListener(new WindowAdapter() {
	      public void windowClosing(WindowEvent e) {
	        System.exit(0);
	      }
	    });
	    frame.setSize(300, 100);
	    frame.setVisible(true);
	  }
	}