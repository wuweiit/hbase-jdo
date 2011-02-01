package com.apache.hadoop.hbase.tool.view.comp;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.SwingConstants;
import javax.swing.JProgressBar;
import javax.swing.JPanel;
import javax.swing.JButton;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ProgressDialog extends JDialog implements IProgressHandler{
	private JLabel lblFilename;
	private JProgressBar progressBar;
	private JPanel panel;
	private JButton btnCancel;
	private boolean isDone;
	private boolean keepDoing = true;
	
	public ProgressDialog(JFrame owner, String name) {
		super(owner);
		setLocationRelativeTo(owner);
		initialize();
		this.lblFilename.setText(name);
		setPreferredSize(new Dimension(300,100));
	}
	
	private void initialize() {
		setTitle("Progress");
		setModal(true);
		getContentPane().add(getLblFilename(), BorderLayout.NORTH);
		getContentPane().add(getProgressBar(), BorderLayout.CENTER);
		getContentPane().add(getPanel(), BorderLayout.SOUTH);
	}
	
	public boolean isDone() {
		return isDone;
	}
	

	@Override
	public void progress(int current, int length) {
		progressBar.setMaximum(length);
		progressBar.setValue(current);
		
		int percent = current*100/length;
		
		progressBar.setString(percent+"% ("+current+"/"+length+")");
		isDone = current==length;
		
		progressBar.repaint();
	}
	
	private JLabel getLblFilename() {
		if (lblFilename == null) {
			lblFilename = new JLabel("filename");
			lblFilename.setSize(300,40);
			lblFilename.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblFilename;
	}
	private JProgressBar getProgressBar() {
		if (progressBar == null) {
			progressBar = new JProgressBar();
			progressBar.setMaximum(100);
			progressBar.setStringPainted(true);
		}
		return progressBar;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.add(getBtnCancel());
		}
		return panel;
	}
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					keepDoing = false;
					setVisible(false);
				}
			});
		}
		return btnCancel;
	}

	@Override
	public boolean keepGoing() {
		return keepDoing;
	}


}
