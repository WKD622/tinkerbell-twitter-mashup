package tinkerbell.mashup.enduser;

import tinkerbell.mashup.enduser.ReportBuilder.Unit;

public class EndUser {
	public EndUser() {
		
	}
	
	public UserReportBuilder userReport() {
		return new UserReportBuilder(this);
	}
	
	public static void main(String[] args) {
		EndUser eu = new EndUser();
		eu.userReport().from("now").count(1).what(Unit.MONTHS).assemble(a -> {
			a.forEach(System.out::println);
			return null;
		});
	}
}
