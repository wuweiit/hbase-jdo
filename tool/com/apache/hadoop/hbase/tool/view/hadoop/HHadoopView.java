package com.apache.hadoop.hbase.tool.view.hadoop;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.apache.hadoop.hbase.client.jdo.HBaseBigFile;
import com.apache.hadoop.hbase.client.jdo.util.HConfigUtil;
import com.apache.hadoop.hbase.client.jdo.util.HUtil;
import com.apache.hadoop.hbase.tool.view.AbstractHPanel;
import com.apache.hadoop.hbase.tool.view.comp.IProgressWork;
import com.apache.hadoop.hbase.tool.view.comp.ProgressDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
	private JButton btnDelete;
	private JButton btnRefresh;
	
	private DefaultTreeModel treeModel;
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
	
	private FileInfo getSelectedFile(){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		FileInfo info = (FileInfo)node.getUserObject();
		return info;
	}
	
	private void addNode(DefaultMutableTreeNode parent, Path p){
		FileStatus fst;
		try {
			fst = fs.getFileStatus(p);
			DefaultMutableTreeNode node = new DefaultMutableTreeNode();
			node.setUserObject(new FileInfo(fst));
			parent.add(node);
			treeModel.nodeStructureChanged(parent);
		} catch (IOException e) {
			log.error("error",e);
		}

	}
	
	private void loadHadoop(){

		try {
			tree.removeAll();
			fs = FileSystem.get(HConfigUtil.makeConfig());
			node = createTree(new Path("/"));
			log.debug("reloaded hadoop directory.");			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		treeModel = new DefaultTreeModel(node);
		
		tree.setModel(treeModel);
		treeModel.reload();
	}

	private DefaultMutableTreeNode createTree(Path p) throws IOException {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		
		FileStatus fst = fs.getFileStatus(p);
		node.setUserObject(new FileInfo(fst));
		if(fst.isDir()) {
			int i = 0;
			for (FileStatus st : fs.listStatus(p)) {
				node.insert(createTree(st.getPath()), i);
				i++;
			}
		}
		return node;
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
					if(node==null) return;
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
			panelMenu.add(getBtnRefresh());
			panelMenu.add(getBtnSave());
			panelMenu.add(getBtnUpload());
			panelMenu.add(getBtnDelete());
		}
		return panelMenu;
	}
	private JButton getBtnSave() {
		if (btnSave == null) {
			btnSave = new JButton("Save to Local");
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final FileStatus fst = getSelectedFile().getFileStatus();
					if(fst.isDir()){
						showSimpleDialog("Select File.");
						return;
					}
					JFileChooser  fc = new JFileChooser(new File("."));
					int returnVal = fc.showOpenDialog(frame);

		            if (returnVal == JFileChooser.APPROVE_OPTION) {						
		                final File file = fc.getSelectedFile();
		                final HBaseBigFile hb = new HBaseBigFile(HConfigUtil.makeHBaseConfig());
		                final ProgressDialog pdg = new ProgressDialog(frame,file.getName());
		                pdg.setLocationRelativeTo(frame);
		                hb.setHandler(pdg);
		                pdg.setSize(300,150);
		                new Thread(){
		                	public void run(){
		                		pdg.setVisible(true);
		                	}}.start();
		                
		                new Thread(){
		                	public void run(){
		                		 boolean isSuccess = hb.copyFile2Local(fst.getPath(),file);
		                		 if(isSuccess){
		                			 showSimpleDialog("Download completed.");
		                		 }else{
		                			 showSimpleDialog("Fail to download");
		                		 }
		                	}
		                }.start();
		            }
				}
			});
		}
		return btnSave;
	}
	private JButton getBtnUpload() {
		if (btnUpload == null) {
			btnUpload = new JButton("Upload to DFS");
			btnUpload.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final FileStatus fst = getSelectedFile().getFileStatus();
					if(fst.isDir()==false){
						showSimpleDialog("Select Directory Path.");
						return;
					}
					
					JFileChooser  fc = new JFileChooser(new File("."));					
					int returnVal = fc.showOpenDialog(frame);
					
					
		            if (returnVal == JFileChooser.APPROVE_OPTION) {
		                final File file = fc.getSelectedFile();
		                final HBaseBigFile hb = new HBaseBigFile(HConfigUtil.makeHBaseConfig());
		                
		                final ProgressDialog pdg = new ProgressDialog(frame,file.getName());
		                pdg.setLocationRelativeTo(frame);
		                hb.setHandler(pdg);
		                pdg.setSize(300,150);
		                new Thread(){
		                	public void run(){
		                		pdg.setVisible(true);
		                	}}.start();
		                
		                new Thread(){
		                	public void run(){
		                		boolean isSuccess = hb.uploadFile(fst.getPath(),file.getName(),file);
				                if(isSuccess) {
				                	DefaultMutableTreeNode parent = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				                	Path p = new Path(fst.getPath(),file.getName());
				                	addNode(parent, p);
				                	showSimpleDialog("Upload completed.");
				                }else{
				                	showSimpleDialog("Fail to upload file");
				                }
		                	}
		                }.start();
		            }				
		         }
			});
		}
		return btnUpload;
	}
	private JButton getBtnDelete() {
		if (btnDelete == null) {
			btnDelete = new JButton("Delete");
			btnDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					HBaseBigFile hb = new HBaseBigFile(HConfigUtil.makeHBaseConfig());
					TreePath treePath = tree.getSelectionPath();
					int result = showConfirmDialog("Do you want to delete "+getSelectedFile().toString(),"Delete");
					if(result!=JOptionPane.YES_OPTION) return;
					try {
						hb.delete(getSelectedFile().getFileStatus().getPath(),true);
						treeModel.removeNodeFromParent((DefaultMutableTreeNode)tree.getLastSelectedPathComponent());
					} catch (IOException e1) {
						log.error("delete",e);
					}
				}
			});
		}
		return btnDelete;
	}
	private JButton getBtnRefresh() {
		if (btnRefresh == null) {
			btnRefresh = new JButton("Refresh");
			btnRefresh.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					loadHadoop();
				}
			});
		}
		return btnRefresh;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
