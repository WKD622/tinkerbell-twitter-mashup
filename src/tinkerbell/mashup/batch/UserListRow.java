package tinkerbell.mashup.batch;

import org.joda.time.DateTime;

public class UserListRow {
	private DateTime t_ts;
	private String t_user;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	
	public UserListRow() {
		
	}
	
	public UserListRow(DateTime ts, String user) {
		this.t_ts = ts;
		this.t_user = user;
		this.year = ts.getYear();
		this.month = ts.getMonthOfYear();
		this.day = ts.getDayOfMonth();
		this.hour = ts.getHourOfDay();
		this.minute = ts.getMinuteOfHour();
	}
	
	public DateTime getT_ts() {
		return t_ts;
	}
	
	public void setT_ts(DateTime t_ts) {
		this.t_ts = t_ts;
	}
	
	public String getT_user() {
		return t_user;
	}
	
	public void setT_user(String t_user) {
		this.t_user = t_user;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public void setMonth(int month) {
		this.month = month;
	}
	
	public int getDay() {
		return day;
	}
	
	public void setDay(int day) {
		this.day = day;
	}
	
	public int getHour() {
		return hour;
	}
	
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public void setMinute(int minute) {
		this.minute = minute;
	}
}
