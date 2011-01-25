package com.apache.hadoop.hbase.tool.view.comp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.toedter.calendar.JDateChooser;

public class DateTimePicker extends JPanel {
	private static final long serialVersionUID = 1L;
	private JComboBox combo_hour = null;
	private JPanel panel_time = null;
	private JComboBox combo_minute = null;

	private JDateChooser dateChooser = null;
	private boolean today;
	/**
	 * This is the default constructor
	 */
	public DateTimePicker() {
		super();
		initialize();
	}

	public void init(long time){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		dateChooser.setDate(c.getTime());
		combo_hour.setSelectedIndex(c.get(Calendar.DAY_OF_MONTH)+1);
		combo_minute.setSelectedIndex(c.get(Calendar.MINUTE)+1);
	}
	
	private void initTime(Calendar date ){
		dateChooser.setDate(date.getTime());
		combo_hour.setSelectedIndex(date.get(Calendar.HOUR_OF_DAY)+1);
		combo_minute.setSelectedIndex(date.get(Calendar.MINUTE)+1);
	}
	
	public void initTime( int date, int hour_of_day, int minute, int second, int ms){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, date);
		c.set(Calendar.HOUR_OF_DAY, hour_of_day);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);
		c.set(Calendar.MILLISECOND, ms);
		
		dateChooser.setDate(c.getTime());
		combo_hour.setSelectedIndex(hour_of_day+1);
		combo_minute.setSelectedIndex(minute+1);
	}
	public void initMonthStart( Calendar c){
		c.set(Calendar.DAY_OF_MONTH, 1);
		initTime(c);
	}
	public void initMonthEnd( Calendar c ){
		int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.set(Calendar.DAY_OF_MONTH, lastDay);
		initTime(c);
	}
	public void initTodayStart(){
		Calendar c = Calendar.getInstance();
		initTime(c.get(Calendar.DAY_OF_MONTH), 0,0,0,0);
	}
	public void intNow(){
		initTime(Calendar.getInstance());
	}
	/**
	 * c.set(Calendar.SECOND, 0);
	 * c.set(Calendar.MILLISECOND,0); 
	 * @param calendar
	 */
	public void initTodayStart(Calendar calendar){
		initTime( calendar);
	}
	public void initTodayEnd(){
		Calendar c = Calendar.getInstance();
		initTime(c.get(Calendar.DAY_OF_MONTH), 23,59,59,998);
	}
	public void initTodayEnd(Calendar calendar){
		initTime( calendar);
	}
	public void init(){
		initTodayStart();
				
	}
	public void initNone(){
		dateChooser.setDate(Calendar.getInstance().getTime());
		combo_hour.setSelectedIndex(0);
		combo_minute.setSelectedIndex(0);
	}
	public Calendar getSelectedDate() {
		Calendar cal = Calendar.getInstance(); 
		Date date = dateChooser.getDate();		
		cal.setTime(date);
		String s = (String)combo_hour.getSelectedItem();
		if(combo_hour.getSelectedIndex()>0) {
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s));
		}else{
			cal.set(Calendar.HOUR_OF_DAY,0);
		}
		String s2 = (String)combo_minute.getSelectedItem();
		if(combo_minute.getSelectedIndex()>0) {
			cal.set(Calendar.MINUTE, Integer.parseInt(s2));
		}else{
			cal.set(Calendar.MINUTE,0);
		}
		
		return cal;
	}

	public long getSelectedTimemillis(){
		return getSelectedDate().getTimeInMillis();
	}


	private void initialize() {
		this.setSize(287, 25);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(191, 15));

		dateChooser = new JDateChooser(new Date());
		dateChooser.setPreferredSize(new Dimension(91, 15));
		this.add(dateChooser, BorderLayout.CENTER);
		this.add(getPanel_time(), BorderLayout.EAST);
	}

	/**
	 * This method initializes combo_hour	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCombo_hour() {
		if (combo_hour == null) {
			Vector<String> vec = new Vector<String>();
			vec.add("시");
			for(int i=0;i<24;i++){
				vec.add(i+"");
			}			
			combo_hour = new JComboBox(vec);

			combo_hour.setName("jComboBox");
			combo_hour.setSelectedIndex(0);
			combo_hour.setPreferredSize(new Dimension(35, 20));
		}
		return combo_hour;
	}

	/**
	 * This method initializes panel_time	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanel_time() {
		if (panel_time == null) {
			GridLayout gridLayout8 = new GridLayout();
			gridLayout8.setRows(1);
			GridLayout gridLayout7 = new GridLayout();
			gridLayout7.setRows(1);
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			GridLayout gridLayout6 = new GridLayout();
			gridLayout6.setRows(1);
			panel_time = new JPanel();
			panel_time.setPreferredSize(new Dimension(80, 20));
			panel_time.setLayout(gridLayout8);
			panel_time.add(getCombo_hour(), null);
			panel_time.add(getCombo_minute(), null);
		}
		return panel_time;
	}

	/**
	 * This method initializes combo_minute	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCombo_minute() {
		if (combo_minute == null) {
			Vector<String> vec = new Vector<String>();
			vec.add("M");
			for(int i=0;i<60;i++){				
				vec.add(i+"");
			}	
			combo_minute = new JComboBox(vec);
			combo_minute.setName("jComboBox1");
			combo_minute.setSelectedIndex(0);
			combo_minute.setPreferredSize(new Dimension(35, 20));
		}
		return combo_minute;
	}

	public Date getDate(){

		if ( today ){
			return new Date();
		}
		return getDate( today );
		
	}
	public Date getDate(boolean nowTime){
		
		Integer hour = null;
		Integer minute = null;
		try{
			hour = Integer.valueOf( (String) combo_hour.getModel().getSelectedItem() ).intValue();
		}catch( ClassCastException ce ){
			hour = (Integer) combo_hour.getModel().getSelectedItem();
		}catch( java.lang.NumberFormatException ne ){
			return null;
		}

		try{
			minute = Integer.valueOf( (String) combo_minute.getModel().getSelectedItem() ).intValue();
		}catch( ClassCastException e ){
			minute = (Integer) combo_minute.getModel().getSelectedItem();
		}catch( java.lang.NumberFormatException ne ){
			return null;
		}
		
		Calendar date = Calendar.getInstance();
		Date d = dateChooser.getDate();
		date.setTime(d);
		date.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour).intValue());
		date.set(Calendar.MINUTE, Integer.valueOf(minute).intValue());
		
		return date.getTime();
	}

	public void setData(Date date ){
		dateChooser.setDate(date);
		combo_hour.setSelectedIndex(date.getHours()+1);
		combo_minute.setSelectedIndex(date.getMinutes()+1);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		dateChooser.setEnabled(enabled);
		combo_minute.setEnabled(enabled);
		combo_hour.setEnabled(enabled);
	}

	public void setTodayMode() {
		today = true;
	}
	public void setVisibleDate( boolean isVisible ){
		dateChooser.setVisible(isVisible);
	}

}  
