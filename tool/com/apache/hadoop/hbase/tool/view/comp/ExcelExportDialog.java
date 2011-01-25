package com.apache.hadoop.hbase.tool.view.comp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 엑셀 export 다이얼로그
 * @author ncanis
 *
 */
public class ExcelExportDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel mainPane = null;
	private JPanel panel_progress = null;
	private JPanel panel_south = null;
	private JButton btn_cancel = null;
	private JProgressBar pbar = null;
	private JLabel label_progress = null;
	
	private POIExcel poiExcel = null;
	private int recvCount = 0;
	private int count;
	/**
	 * @param owner
	 */
	public ExcelExportDialog(Frame owner) {
		super(owner);
		initialize();
		setLocationRelativeTo(owner);
		setModal(true);
		
		poiExcel = new POIExcel();
	}
	
	public void clear() {
		count = 0;
		recvCount = 0;
		poiExcel.clear();
		this.pbar.setValue(0);
	}
	
	/**
	 * 사용법..
	 * class ExcelData{
	 * 	private int no;
	 *  private int char_no;
	 * }
	 * ExcelData의 내용을 excel로 적용할 때...
	 * 
	 * xlsDialog.setHeader(
	 *			new ExcelHeader("no",3,"번호"),
	 *			new ExcelHeader("char_no",3,"캐릭번호")
	 *		);
	 * "char_no"  : 변수 char_no 와 반드시 일치해야 한다. 변수 이름이 바뀌면 인식하지 못한다.
	 *   3         : excel의 width 
	 * 캐릭번호         : excel의 헤더 이름이다.
	 	 * @param headers
	 */
	public void setHeader(ExcelHeader... headers){
		poiExcel.setConfig(headers);
	}
	
	public int getCallCount() {
		return count;
	}
	
	public boolean saveDirect(List<List<String>> data){
		if(data!=null && data.size()!=0){
			poiExcel.saveDirect(data);
		}
		
		try {
			saveFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean save(List<?> data, int totalCount) throws Exception{
		count++;
		if(data!=null) recvCount+=data.size();
//		System.out.println("recvcount="+recvCount+", total="+totalCount);
		
		if(data!=null && data.size()!=0){
			poiExcel.save(data);
		}
		
		float value = totalCount==0? 1:(float)recvCount/(float)totalCount;
		this.pbar.setValue((int)(value*100));
		this.pbar.repaint();
		
		if(recvCount>=totalCount) {
			saveFile();
			return true;
		}
		return false;
	}
	
	
	public void saveFile() throws Exception {
		this.pbar.setValue(100);
		JFileChooser fileopen = new JFileChooser();
		fileopen.setDialogTitle("데이터를 Excel 파일로 저장");
		FileFilter filter = new FileNameExtensionFilter("*.xls", "xls");
        fileopen.addChoosableFileFilter(filter);
        fileopen.setCurrentDirectory(new File("."));
        int returnVal = fileopen.showSaveDialog(mainPane);
        
        if(returnVal==JFileChooser.APPROVE_OPTION) {
            String path = fileopen.getSelectedFile().getAbsolutePath();
            if(path.lastIndexOf(".xls")<0) path+=".xls";
            File file = new File(path);        
            if(file!=null){
            	boolean result= poiExcel.writeExcel(file);
            	if(result){
            		JOptionPane.showMessageDialog(this,"파일 저장 완료");
            	}else{
            		
//            		JOptionPane.showMessageDialog(this,"파일 저장 실패. 잠시후 다시 시도하세요.");
            	}
            }            	
        }
        clear();
        
        setVisible(false);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(394, 161);
		this.setResizable(false);
		this.setTitle("엑셀 Export");
		this.setContentPane(getMainPane());
	}

	/**
	 * This method initializes mainPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPane() {
		if (mainPane == null) {
			mainPane = new JPanel();
			mainPane.setLayout(new BorderLayout());
			mainPane.add(getPanel_progress(), BorderLayout.CENTER);
			mainPane.add(getPanel_south(), BorderLayout.SOUTH);
		}
		return mainPane;
	}

	/**
	 * This method initializes panel_progress	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanel_progress() {
		if (panel_progress == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setVgap(20);
			label_progress = new JLabel();
			label_progress.setText("진행상황: 진행중..");
			panel_progress = new JPanel();
			panel_progress.setLayout(flowLayout);
			panel_progress.add(getPbar(), null);
			panel_progress.add(label_progress, null);
		}
		return panel_progress;
	}

	/**
	 * This method initializes panel_south	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanel_south() {
		if (panel_south == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			panel_south = new JPanel();
			panel_south.setLayout(new FlowLayout());
			panel_south.setPreferredSize(new Dimension(3, 30));
			panel_south.add(getBtn_cancel(), null);
		}
		return panel_south;
	}

	/**
	 * This method initializes btn_cancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtn_cancel() {
		if (btn_cancel == null) {
			btn_cancel = new JButton();
			btn_cancel.setText("취소");
			btn_cancel.setPreferredSize(new Dimension(60, 22));
			btn_cancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					poiExcel.clear();
					
					setVisible(false);
				}
			});
		}
		return btn_cancel;
	}

	/**
	 * This method initializes pbar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getPbar() {
		if (pbar == null) {
			pbar = new JProgressBar();
			pbar.setPreferredSize(new Dimension(368, 25));
			pbar.setValue(30);
			pbar.setStringPainted(false);
		}
		return pbar;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
