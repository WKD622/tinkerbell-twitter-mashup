package tinkerbell.mashup.batch;

import java.io.Serializable;

import org.joda.time.DateTime;

public class CorrelatedKeywordsRow implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String keyword;
	public int year;
	public int month;
	public int day;
	private int hour;
	private int minute;
	private int count;
	
	public CorrelatedKeywordsRow() {
		
	}
	
	public static CorrelatedKeywordsRow of(String kw, DateTime dt, int count) {
		CorrelatedKeywordsRow r = new CorrelatedKeywordsRow();
		r.keyword = kw;
		r.year = dt.getYear();
		r.month = dt.getMonthOfYear();
		r.day = dt.getDayOfMonth();
		r.hour = dt.getHourOfDay();
		r.minute = dt.getMinuteOfHour();
		r.setCount(count);
		return r;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
