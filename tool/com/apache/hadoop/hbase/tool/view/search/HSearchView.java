package com.apache.hadoop.hbase.tool.view.search;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.apache.hadoop.hbase.tool.core.UIResult;
import com.apache.hadoop.hbase.tool.view.AbstractHPanel;
import com.apache.hadoop.hbase.tool.view.comp.ExcelButton;
import com.apache.hadoop.hbase.tool.view.comp.table.HJTablePanel;
import com.apache.hadoop.hbase.tool.view.processor.TableDataBean;
import com.apache.hadoop.hbase.tool.view.processor.TableInfo;
import com.apache.hadoop.hbase.tool.view.processor.TableProcessor;

public class HSearchView extends AbstractHPanel {

	private static final long serialVersionUID = 1L;
	private JPanel panelTop;
	private JPanel panelCenter;
	private JComboBox cboxTable;
	private JButton btnSearch;
	private JComboBox cboxFamily;
	private JPanel panelMenu;
	private JComboBox cboxLimit;
	private JButton btnPrev;
	private JButton btnNext;
	private JButton btnOption;

	private HJTablePanel<TableDataBean> dataPane;
	private JPanel panelTable;
	private TableProcessor proc = new TableProcessor();
	private JLabel lblTable;
	private JLabel lblFamily;
	private JLabel lblLimit;
	private TableDataModel model;

	public HSearchView() {
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		add(getPanelTop(), BorderLayout.NORTH);
		add(getPanelCenter(), BorderLayout.CENTER);
	}

	@Override
	public void update() {
	}

	@Override
	public void startPanel() {
		log.debug("connect to quorum server.");
		// setting tables.
		List<TableInfo> tables = proc.getTableDesc();
		String[] items = new String[tables.size() + 1];
		items[0] = "Select Table.";
		int i = 1;
		for (TableInfo ti : tables) {
			items[i] = ti.getName();
			i++;
		}

		cboxTable.setModel(new DefaultComboBoxModel(items));
	}

	@Override
	public void clearPanel() {

	}

	@Override
	public void closePanel() {
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

	private JPanel getPanelTop() {
		if (panelTop == null) {
			panelTop = new JPanel();
			panelTop.add(getLblTable());
			panelTop.add(getCboxTable());
			panelTop.add(getLblFamily());
			panelTop.add(getCboxFamily());
			panelTop.add(getLblLimit());
			panelTop.add(getCboxLimit());
			panelTop.add(getBtnSearch());
			panelTop.add(getBtnOption());

			ExcelButton exBtn = new ExcelButton();
			exBtn.setSize(100, 50);
			exBtn.setText("Excel");
			panelTop.add(exBtn);
		}
		return panelTop;
	}

	private JPanel getPanelCenter() {
		if (panelCenter == null) {
			panelCenter = new JPanel();
			panelCenter.setLayout(new BorderLayout(0, 0));
			panelCenter.add(getPanelMenu(), BorderLayout.NORTH);
			panelCenter.add(getPanelTable(), BorderLayout.CENTER);
		}
		return panelCenter;
	}

	private JComboBox getCboxTable() {
		if (cboxTable == null) {
			cboxTable = new JComboBox();
			cboxTable.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (cboxTable.getSelectedIndex() == 0)
						return;
					String table = cboxTable.getSelectedItem().toString();
					List<TableInfo> tables = proc.getTableDesc();
					String[] items = null;
					for (TableInfo ti : tables) {
						if (ti.getName().equals(table)) {
							items = ti.getFamilies();
							break;
						}
					}

					cboxFamily.setModel(new DefaultComboBoxModel(items));
				}
			});
		}
		return cboxTable;
	}
	
	private void search(){
		if (cboxTable.getSelectedIndex() == 0)
			return;
		String table = cboxTable.getSelectedItem().toString();
		String family = cboxFamily.getSelectedItem().toString();

		int limit = Integer.parseInt(cboxLimit.getSelectedItem().toString());
		byte[] startRow = model.getLastRow() == null ? null : model.getLastRow().getRow();
		List<TableDataBean> list = proc.getTableData(table, family, startRow, limit);
		dataPane.loadModelData(list);
	}

	private JButton getBtnSearch() {
		if (btnSearch == null) {
			btnSearch = new JButton("Search");
			btnSearch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					search();
				}
			});
		}
		return btnSearch;
	}

	private JComboBox getCboxFamily() {
		if (cboxFamily == null) {
			cboxFamily = new JComboBox();
		}
		return cboxFamily;
	}

	private JPanel getPanelMenu() {
		if (panelMenu == null) {
			panelMenu = new JPanel();
			panelMenu.add(getBtnPrev());
			panelMenu.add(getBtnNext());
		}
		return panelMenu;
	}

	private JComboBox getCboxLimit() {
		if (cboxLimit == null) {
			String[] items = new String[] { "10", "20", "30", "40", "50" };
			cboxLimit = new JComboBox(items);
		}
		return cboxLimit;
	}

	private JButton getBtnPrev() {
		if (btnPrev == null) {
			btnPrev = new JButton("< Prev");
			btnPrev.setEnabled(false);
		}
		return btnPrev;
	}

	private JButton getBtnNext() {
		if (btnNext == null) {
			btnNext = new JButton("Next >");
			btnNext.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					search();
				}
			});
		}
		return btnNext;
	}

	private JButton getBtnOption() {
		if (btnOption == null) {
			btnOption = new JButton("Option");
		}
		return btnOption;
	}

	private JPanel getPanelTable() {
		if (panelTable == null) {
			panelTable = new JPanel();
			panelTable.setLayout(new BorderLayout());
			model = new TableDataModel();
			dataPane = new HJTablePanel<TableDataBean>(model, null,true);
			panelTable.add(dataPane, BorderLayout.CENTER);
		}
		return panelTable;
	}

	private JLabel getLblTable() {
		if (lblTable == null) {
			lblTable = new JLabel("Table:");
		}
		return lblTable;
	}

	private JLabel getLblFamily() {
		if (lblFamily == null) {
			lblFamily = new JLabel("Family:");
		}
		return lblFamily;
	}

	private JLabel getLblLimit() {
		if (lblLimit == null) {
			lblLimit = new JLabel("Limit:");
		}
		return lblLimit;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
