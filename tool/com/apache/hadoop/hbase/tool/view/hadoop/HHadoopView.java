package com.apache.hadoop.hbase.tool.view.hadoop;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.apache.hadoop.hbase.client.jdo.util.HUtil;
import com.apache.hadoop.hbase.tool.view.AbstractHPanel;

public class HHadoopView extends AbstractHPanel {

	private static final long serialVersionUID = 1L;
	private JTree tree;
	private DefaultMutableTreeNode node;
	private FileSystem fs;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JScrollPane scrollPaneTree;
	private JPanel panelMenu;
	private JButton btnSave;
	private JButton btnUpload;
	public HHadoopView() {
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		add(getPanel(), BorderLayout.EAST);
		add(getScrollPaneTree(), BorderLayout.CENTER);

		loadHadoop();
		add(getPanelMenu(), BorderLayout.SOUTH);
	}
	
	private void loadHadoop(){

		try {
			fs = FileSystem.get(new Configuration());
			node = createTree(new Path("/"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		DefaultTreeModel model = new DefaultTreeModel(node);
		tree.setModel(model);
		model.reload();
	}

	private DefaultMutableTreeNode createTree(Path p) throws IOException {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode();
		FileStatus fst = fs.getFileStatus(p);
		top.setUserObject(new FileInfo(fst));
		if(fst.isDir()) {
			int i = 0;
			for (FileStatus st : fs.listStatus(p)) {
				top.insert(createTree(st.getPath()), i);
				i++;
			}
		}
		return (top);
	}

	// ////
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

	private JTree getTree() {
		if (tree == null) {
			tree = new JTree();
			tree.addTreeSelectionListener(new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent e) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
					
					FileInfo fi = (FileInfo)node.getUserObject();
					FileStatus fst = fi.getFileStatus();
					
					StringBuilder sb = new StringBuilder();
					Calendar c = Calendar.getInstance();
					
					sb.append("Path=").append(fst.getPath()).append("\n");
					
					c.setTimeInMillis(fst.getAccessTime());
					sb.append("AccessTime=").append(HUtil.makeSimpleDate(c)).append("\n");
					
					sb.append("Length=");
					long  size = fst.getLen();
					if(size>1024*1024) {
						sb.append(size/(1024*1024)).append(" MB.\n");
					}else if(size>1024) {
						sb.append(size/1024).append(" kbytes.\n");
					}else{
						sb.append(size).append(" byte.\n");
					}
					
					
					c.setTimeInMillis(fst.getModificationTime());
					sb.append("ModTime=").append(HUtil.makeSimpleDate(c)).append("\n");
					sb.append("Rep count=").append(fst.getReplication()).append("\n");
					textArea.setText(sb.toString());
				}
			});
		}
		return tree;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();			
			panel.setPreferredSize(new Dimension(300,100));
			panel.setLayout(new BorderLayout(0, 0));
			panel.add(getScrollPane());
		}
		return panel;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTextArea());
		}
		return scrollPane;
	}
	private JTextArea getTextArea() {
		if (textArea == null) {
			textArea = new JTextArea();
			textArea.setLineWrap(true);
		}
		return textArea;
	}
	private JScrollPane getScrollPaneTree() {
		if (scrollPaneTree == null) {
			scrollPaneTree = new JScrollPane();
			scrollPaneTree.setViewportView(getTree());
		}
		return scrollPaneTree;
	}
	private JPanel getPanelMenu() {
		if (panelMenu == null) {
			panelMenu = new JPanel();
			panelMenu.add(getBtnSave());
			panelMenu.add(getBtnUpload());
		}
		return panelMenu;
	}
	private JButton getBtnSave() {
		if (btnSave == null) {
			btnSave = new JButton("Save to Local");
		}
		return btnSave;
	}
	private JButton getBtnUpload() {
		if (btnUpload == null) {
			btnUpload = new JButton("Upload to DFS");
		}
		return btnUpload;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
