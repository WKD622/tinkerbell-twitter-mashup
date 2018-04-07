package tinkerbell.mashup.enduser;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.spark.connector.cql.CassandraConnector;
import com.mdimension.jchronic.Chronic;
import com.mdimension.jchronic.Options;
import com.mdimension.jchronic.utils.Span;

import tinkerbell.mashup.Config;

public class ReportBuilder {
	public static enum Unit {
		MONTHS, DAYS, HOURS, MINUTES;
	}
	
	private final Options options = new Options();
	
	private int count;
	private Unit what;
	private EndUser user;
	
	private Session session;
	
	private PreparedStatement selectMinutes;
	private PreparedStatement selectHours;
	private PreparedStatement selectDays;
	private PreparedStatement selectMonths;
	
	private DateTime from;
	
	private DateTime to;
	
	public ReportBuilder(EndUser user, String tableName) {
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		options.setNow(now);
		options.setGuess(true);
		
		this.user = user;
		
		connect(tableName);
	}
	
	private void connect(String tableName) {
		CassandraConnector connector = CassandraConnector.apply(Config.SPARK_CONF);
		session = connector.openSession();
		
		selectMinutes = session.prepare("select t_user from " + Config.KEYSPACE + "." + tableName
				+ "_minute" + " where year=? and month=? and day=? and hour=? and minute=?;");
		selectHours = session.prepare("select t_user from " + Config.KEYSPACE + "." + tableName
				+ "_hour" + " where year=? and month=? and day=? and hour=?;");
		selectDays = session.prepare("select t_user from " + Config.KEYSPACE + "." + tableName
				+ "_day" + " where year=? and month=? and day=?;");
		selectMonths = session.prepare("select t_user from " + Config.KEYSPACE + "." + tableName
				+ "_month" + " where year=? and month=?;");
	}
	
	public ReportBuilder from(String when) {
		Span span = Chronic.parse(when, options);
		DateTimeZone jodaTz = DateTimeZone.forID(span.getBeginCalendar().getTimeZone().getID());
		from = new DateTime(span.getBeginCalendar().getTimeInMillis(), jodaTz);
		to = null;
		return this;
	}
	
	public ReportBuilder to(String when) {
		Span span = Chronic.parse(when, options);
		DateTimeZone jodaTz = DateTimeZone.forID(span.getBeginCalendar().getTimeZone().getID());
		to = new DateTime(span.getBeginCalendar().getTimeInMillis(), jodaTz);
		from = null;
		return this;
	}
	
	public ReportBuilder count(int count) {
		this.count = count;
		return this;
	}
	
	public ReportBuilder what(Unit what) {
		this.what = what;
		return this;
	}
	
	public Report assemble(Function<Stream<String>, ? extends Report> constructor) {
		Stream<String> stream;
		new Object();
		switch (what) {
			case DAYS:
				if (from == null) {
					from = to.minusDays(count);
				}
				
				stream = streamDays(from, count);
				break;
			case HOURS:
				if (from == null) {
					from = to.minusHours(count);
				}
				
				stream = streamHours(from, count);
				break;
			case MINUTES:
				if (from == null) {
					from = to.minusMinutes(count);
				}
				
				stream = streamMinutes(from, count);
				break;
			case MONTHS:
				if (from == null) {
					from = to.minusMonths(count);
				}
				
				stream = streamMonths(from, count);
				break;
			default:
				throw new UnsupportedOperationException();
		}
		
		session.close();
		return constructor.apply(stream);
	}
	
	private Stream<String> streamDays(DateTime from, int days) {
		return IntStream.range(0, days) //
				.mapToObj(i -> i + from.getDayOfMonth()) //
				.flatMap(day -> {
					BoundStatement bound = selectMonths.bind(from.getYear(), from.getMonthOfYear(),
							day);
					ResultSet rs = session.execute(bound);
					
					return StreamSupport.stream(rs.spliterator(), false)
							.map(row -> row.getString("t_user"));
				});
	}
	
	private Stream<String> streamMonths(DateTime from, int months) {
		return IntStream.range(0, months) //
				.mapToObj(i -> i + from.getMonthOfYear()) //
				.flatMap(month -> {
					BoundStatement bound = selectMonths.bind(from.getYear(), month);
					ResultSet rs = session.execute(bound);
					
					return StreamSupport.stream(rs.spliterator(), false)
							.map(row -> row.getString("t_user"));
				});
	}
	
	private Stream<String> streamHours(DateTime from, int hours) {
		return IntStream.range(0, hours) //
				.mapToObj(i -> i + from.getHourOfDay()) //
				.flatMap(hour -> {
					BoundStatement bound = selectHours.bind(from.getYear(), from.getMonthOfYear(),
							from.getDayOfMonth(), hour);
					ResultSet rs = session.execute(bound);
					
					return StreamSupport.stream(rs.spliterator(), false)
							.map(row -> row.getString("t_user"));
				});
	}
	
	private Stream<String> streamMinutes(DateTime from, int minutes) {
		return IntStream.range(0, minutes) //
				.mapToObj(i -> i + from.getMinuteOfHour()) //
				.flatMap(minute -> {
					BoundStatement bound = selectMonths.bind(from.getYear(), from.getMonthOfYear(),
							from.getDayOfMonth(), from.getHourOfDay(), minute);
					ResultSet rs = session.execute(bound);
					
					return StreamSupport.stream(rs.spliterator(), false)
							.map(row -> row.getString("t_user"));
				});
	}
}
