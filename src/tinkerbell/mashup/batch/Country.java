package tinkerbell.mashup.batch;

public class Country {
	public String country;
	public int count;
	public int year;
	public int month;
	public int day;
	public int hour;
	public int minute;

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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Country join(Country c) {
		Country ret = new Country();
		ret.country = country;
		ret.count = count + c.count;
		ret.year = year;
		ret.month = month;
		ret.day = day;
		ret.hour = hour;
		ret.minute = minute;
		return ret;
	}
}