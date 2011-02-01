package com.apache.hadoop.hbase.tool.view;

import static com.apache.hadoop.hbase.client.jdo.IHBaseLog.log;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import com.apache.hadoop.hbase.client.jdo.IHBaseLog;
import com.apache.hadoop.hbase.tool.core.ConsoleStream;
import com.apache.hadoop.hbase.tool.core.IChanger;
import com.apache.hadoop.hbase.tool.core.IConsoleHandler;
import com.apache.hadoop.hbase.tool.core.IRootPanel;
import com.apache.hadoop.hbase.tool.core.IView;
import com.apache.hadoop.hbase.tool.core.UIResult;
import com.apache.hadoop.hbase.tool.view.comp.SpacerIcon;
import com.apache.hadoop.hbase.tool.view.hadoop.HHadoopView;
import com.apache.hadoop.hbase.tool.view.search.HSearchView;
import com.apache.hadoop.hbase.tool.view.setting.HSettingView;
import com.apache.hadoop.hbase.tool.view.table.TableMainView;

/**
 * @author ncanis
 *
 */
public class HMainPanel extends AbstractHPanel implements IRootPanel,IChanger, IHBaseLog {
	private JLabel label_status = null;
	private JPanel southPanel = null;
	private HMenuBar menuBarPanel = null;
	private JTabbedPane tpane_center = null;
	
	
	// panels
	private TableMainView tablePanel;
	private HSearchView searchPanel;
	private HHadoopView hadoopPanel;
	private HSettingView settingPanel;
	private JSplitPane splitPane;
	private JScrollPane scrollPane;
	private JTextArea taMessage;
	private JPanel panelBottom;
	private JPanel panel_2;
	private JButton btnClear;
	
	public HMainPanel(){
		initialize();
	}
	
	////////////////////////////////////////////////////////////////////
	private void initialize() {
		tablePanel = (TableMainView)UIManagerImpl.get().getPanel(HView.TABLE);
		searchPanel = (HSearchView)UIManagerImpl.get().getPanel(HView.SEARCH);
		hadoopPanel = (HHadoopView)UIManagerImpl.get().getPanel(HView.HADOOP);
		settingPanel = (HSettingView)UIManagerImpl.get().getPanel(HView.SETTING);

		label_status = new JLabel();
		label_status.setText("Hbase tool");
		setLayout(new BorderLayout());
//		this.setSize(new Dimension(812, 267));
		this.add(getSouthPanel(), BorderLayout.SOUTH);
		add(getSplitPane(), BorderLayout.CENTER);
//		add(getTpane_center(), BorderLayout.EAST);
//		add(getScrollPane(), BorderLayout.CENTER);
//		add(getPanelBottom(), BorderLayout.CENTER);
	}

	////////////////////////////////////////////////////////
	/**
	 * This method initializes southPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSouthPanel() {
		if (southPanel == null) {
			southPanel = new JPanel();
			southPanel.setLayout(new FlowLayout());
			southPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			southPanel.setPreferredSize(new Dimension(94, 30));
			southPanel.setPreferredSize(new Dimension(94, 35));
			southPanel.add(label_status, null);
		}
		return southPanel;
	}
	
	public void setMenuPanel(HMenuBar bar) {
		menuBarPanel = bar;
	}

	/**
	 * This method initializes tpane_center	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getTpane_center() {
		if (tpane_center == null) {
			tpane_center = new JTabbedPane();
			tpane_center.addTab(HView.TABLE.getDesc(),  new SpacerIcon(25), tablePanel, null);
			tpane_center.addTab(HView.SEARCH.getDesc(),  new SpacerIcon(25), searchPanel, null);
			tpane_center.addTab(HView.HADOOP.getDesc(),  new SpacerIcon(25), hadoopPanel, null);
			tpane_center.addTab(HView.SETTING.getDesc(),  new SpacerIcon(25), settingPanel, null);
			tpane_center.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					JTabbedPane pane = (JTabbedPane)e.getSource();

					int index = pane.getSelectedIndex();
					String title = pane.getTitleAt(index);
					HView view = HView.getView(title);
					UIManagerImpl.get().changeUI(view,false);	
				}
			});
		}
		return tpane_center;
	}

	@Override
	public boolean changeUI(IRootPanel panel, IView view) {
		tpane_center.setSelectedIndex(view.getIndex());
		return true;
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
	public UIResult checkAvailable() {
		return UIResult.SUCCESS;
	}

	@Override
	public boolean doConfirm() {
		return true;
	}
	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new JSplitPane();
			splitPane.setDividerLocation(500);
			splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			splitPane.setTopComponent(getTpane_center());
			splitPane.setBottomComponent(getPanelBottom());
			
		}
		return splitPane;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTaMessage());
			
		}
		return scrollPane;
	}
	/**
	 * This method initializes taMessage	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextArea getTaMessage() {
		if (taMessage == null) {
			taMessage = new JTextArea();
			taMessage.setLineWrap(true);
			taMessage.setAutoscrolls(true);
			taMessage.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));	
		}
		return taMessage;
	}
	private JPanel getPanelBottom() {
		if (panelBottom == null) {
			panelBottom = new JPanel();
			panelBottom.setLayout(new BorderLayout(0, 0));
			panelBottom.add(getScrollPane());
			panelBottom.add(getPanel_2(), BorderLayout.NORTH);
		}
		return panelBottom;
	}
	private JPanel getPanel_2() {
		if (panel_2 == null) {
			panel_2 = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			panel_2.add(getBtnClear());
		}
		return panel_2;
	}
	private JButton getBtnClear() {
		if (btnClear == null) {
			btnClear = new JButton("Clear");
			btnClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					taMessage.setText("");
				}
			});
		}
		return btnClear;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
