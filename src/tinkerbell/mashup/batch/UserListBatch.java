package tinkerbell.mashup.batch;

import org.apache.spark.api.java.JavaRDD;
import org.joda.time.DateTime;

import com.datastax.spark.connector.japi.CassandraRow;

public class UserListBatch extends Batch {
	@Override
	public void run(JavaRDD<CassandraRow> rdd) {
		JavaRDD<UserListRow> dd = rdd
				.map(row -> new UserListRow(row.getDateTime("t_ts"), row.getString("t_user")));
		
		save(dd, "userlist_minute", UserListRow.class);
		save(dd, "userlist_hour", UserListRow.class);
		save(dd, "userlist_day", UserListRow.class);
		save(dd, "userlist_month", UserListRow.class);
	}
	
	public static void main(String[] args) {
		DateTime from = DateTime.now();
		
		Batch batch = new UserListBatch();
		while(!Thread.interrupted()) {
			if(from.plusMinutes(1).compareTo(DateTime.now()) > 0) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					return;
				}
				
				continue;
			}
			
			from = from.plusMinutes(1);
			batch.run(from);
		}
	}
}
