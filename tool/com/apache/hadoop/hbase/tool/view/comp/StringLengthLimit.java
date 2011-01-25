package com.apache.hadoop.hbase.tool.view.comp;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * JTextArea 글자수 입력을 제한한다.
 * 
 * 사용법:
 * jTextArea.setDocument(new StringLengthLimit(200)) ->200자 이하까지만 
 * @author 박재혁
 *
 */
public class StringLengthLimit extends PlainDocument {

		
	private static final long serialVersionUID = 1L;
	private int limit;
	public StringLengthLimit(int limit){
		super();
		this.limit = limit;
	}
	
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException{
		if ( str == null) return;
		
		if ( getLength() + str.length() <= limit ){
			super.insertString(offset, str, attr);
		}
	}
	
}
