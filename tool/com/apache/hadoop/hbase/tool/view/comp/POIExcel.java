package com.apache.hadoop.hbase.tool.view.comp;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 
 * Java Object to Excel.
 * @author ncanis
 *
 */
public class POIExcel {
	private HSSFWorkbook wb;
	private HSSFSheet sheet;
	private ExcelHeader[] headers;
	private int currentPos;
	
	public POIExcel() {
		clear();
	}
	
	public void clear() {		
		this.currentPos = 1;
		init();
	}
	
	private void init() {
		wb = new HSSFWorkbook();
		// create a new sheet
		sheet = wb.createSheet();
		
		if(headers!=null) {
			setConfig(headers);
		}
	}

	public void setConfig(ExcelHeader... headers){
		this.headers = headers;
		writeHeader(headers);
	}
	
	private void setStyle(HSSFCell cell){
		HSSFCellStyle style =wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  					//스타일인스턴스의 속성 셑팅		
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);				//테두리 설정
		style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		
		HSSFFont font = wb.createFont();									//폰트 조정 인스턴스 생성
		font.setBoldweight((short)700);		
		style.setFont(font);
		
		cell.setCellStyle(style);
	}
	
	private void writeHeader(ExcelHeader... headers) {
		HSSFRow hrow = sheet.createRow(0);
		for(int col=0;col<headers.length; col++){
			ExcelHeader eh = headers[col];
			
			HSSFCell c2 = hrow.createCell(col);
			setStyle(c2);
			sheet.setColumnWidth(col, eh.getWidth()*1000);
			c2.setCellValue(new HSSFRichTextString(eh.getName()));				
		}
		
		
		currentPos = 1;
	}
	
	public void saveDirect(List<List<String>> data) {
		if(data==null) return;
		int i =0;
		for(int row=currentPos; row<(data.size()+currentPos);row++) {
			HSSFRow hrow = sheet.createRow(row);
			
			List<String> data2 = data.get(i);
			for(int col=0;col<headers.length; col++){
				ExcelHeader eh = headers[col];
				String value = "";
				try {
					value = data2.get(col);
				} catch (Exception e) {
					e.printStackTrace();
				}
				HSSFCell c2 = hrow.createCell(col);
				c2.setCellValue(new HSSFRichTextString(value));				
			}
			i++;
		}
		currentPos+=data.size();
	}
	
	public void save(List<?> data) {
		if(data==null) return;
		for(int row=currentPos; row<(data.size()+currentPos);row++) {
			HSSFRow hrow = sheet.createRow(row);
			for(int col=0;col<headers.length; col++){
				ExcelHeader eh = headers[col];
				String value = "";
				try {
					Object o = PropertyUtils.getSimpleProperty(data.get(row-currentPos),eh.getKey());
					if(o!=null) {
						value = o.toString();
						if(eh.getType()==Types.DATE) {
							long time = Long.valueOf(value);
							Calendar c = Calendar.getInstance();
							c.setTimeInMillis(time);
							value = c.toString();
						}else if( eh.getType() == Types.TIME ){//Thu MAY 10 2009 XXX XXX KST 를 바꾼다.
							SimpleDateFormat recvSimpleFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
							SimpleDateFormat tranSimpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
							
							Date d1 = recvSimpleFormat.parse(value);
							value = tranSimpleFormat.format(d1);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				HSSFCell c2 = hrow.createCell(col);
				c2.setCellValue(new HSSFRichTextString(value));				
			}
		}
		currentPos+=data.size();
	}
	
	public boolean writeExcel(File file) throws Exception {
		boolean success=false;
		// Save
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);			
			wb.write(out);
			success= true;
		} catch (Exception e) {
			throw new Exception(e);
//			e.printStackTrace();
		} finally{
			if(out!=null) try{ out.close(); }catch(Exception e){};
		}
		return success;
	}
	
	public static void main(String[] args) throws Exception {
//		File file = new File("test.xls");
//		
//		List<GameLog> list = new ArrayList<GameLog>();
//		for(int i=0;i<100;i++){
//			GameLog test = new GameLog();
//			test.setLogNo(i);
//			list.add(test);
//		}
//		
//		POIExcel pe = new POIExcel();
//		pe.setConfig(
//	        	new ExcelHeader("logNo",4,"로그번호"),
//	        	new ExcelHeader("userNo",5,"고유번호"),
//	        	new ExcelHeader("charno",5,"케릭터번호"),
//	        	new ExcelHeader("charName",5,"이름"),
//	        	new ExcelHeader("level",2,"레벨"),
//	        	
//	        	new ExcelHeader("victory",3,"승"),
//	        	new ExcelHeader("lose",3,"패"),
//	        	new ExcelHeader("experience",4,"경험치"),
//	        	new ExcelHeader("point",4,"포인트"),
//	        	new ExcelHeader("cash",4,"캐쉬"),
//	        	
//	        	new ExcelHeader("ip",5,"아이피"),
//	        	new ExcelHeader("time",8,"발생시간",Types.DATE),
//	        	new ExcelHeader("gtype",4,"소분류"),
//	        	new ExcelHeader("stype",4,"소분류"),
//	        	new ExcelHeader("thing",6,"Object"),
//	        	new ExcelHeader("content",15,"내용")        	
//	        );
//
//		pe.save(list);
//		pe.save(list);
//		pe.writeExcel(file);
	}
}



