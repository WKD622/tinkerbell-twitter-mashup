package tinkerbell.mashup.enduser;

import org.joda.time.DateTime;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.mdimension.jchronic.Options;

public class UserReportBuilder extends ReportBuilder{
	private final Options options = new Options();
	private DateTime from;
	private DateTime to;
	private EndUser user;
	
	private Session session;
	
	private PreparedStatement selectMinutes;
	private PreparedStatement selectHours;
	private PreparedStatement selectDays;
	private PreparedStatement selectMonths;
	
	public UserReportBuilder(EndUser user) {
		super(user, "userlist");
	}
	
	public Report assemble() {
		return super.assemble(a -> { a.forEach(System.out::println); return null; });
	}
}
