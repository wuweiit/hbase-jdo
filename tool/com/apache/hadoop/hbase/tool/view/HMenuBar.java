package com.apache.hadoop.hbase.tool.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * @author ncanis
 *
 */
public class HMenuBar extends AbstractHPanel{
	private JMenuBar gmMenu = null;
	private JMenu menu_file = null;
	private JMenu menu_about = null;
	private JMenuItem mitem_exit = null;
	private JMenuItem mitem_help = null;
	public HMenuBar() {
		initialize();
	}

	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(300, 20));
		this.add(getGmMenu(), BorderLayout.NORTH);
	}

	/**
	 * This method initializes gmMenu	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	public JMenuBar getGmMenu() {
		if (gmMenu == null) {
			gmMenu = new JMenuBar();
			gmMenu.setPreferredSize(new Dimension(0, 20));
			gmMenu.add(getMenu_file());
			gmMenu.add(getMenu_about());
		}
		return gmMenu;
	}

	/**
	 * This method initializes menu_file	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getMenu_file() {
		if (menu_file == null) {
			menu_file = new JMenu();
			menu_file.setName("");
			menu_file.setText("File");
			menu_file.add(getMitem_exit());
		}
		return menu_file;
	}

	/**
	 * This method initializes menu_about	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getMenu_about() {
		if (menu_about == null) {
			menu_about = new JMenu();
			menu_about.setText("Help");
			menu_about.add(getMitem_help());
		}
		return menu_about;
	}

	/**
	 * This method initializes mitem_exit	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getMitem_exit() {
		if (mitem_exit == null) {
			mitem_exit = new JMenuItem();
			mitem_exit.setText("Exit");
			mitem_exit.addActionListener(new java.awt.event.ActionListener() {   
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					int result=JOptionPane.showConfirmDialog(frame,"Exit Program","Exit Program",JOptionPane.OK_CANCEL_OPTION);
					if(result==JOptionPane.OK_OPTION) {
						System.exit(0);
					}
				}
			
			});
		}
		return mitem_exit;
	}

	/**
	 * This method initializes mitem_help	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getMitem_help() {
		if (mitem_help == null) {
			mitem_help = new JMenuItem();
			mitem_help.setText("About");
			mitem_help.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {		
					String message= "Hbase tool";
					JOptionPane.showMessageDialog(frame,message," HBase tool",JOptionPane.INFORMATION_MESSAGE);
				}
			});
		}
		return mitem_help;
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

}
