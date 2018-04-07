package tinkerbell.mashup.batch;

import org.apache.spark.api.java.JavaRDD;
import org.joda.time.DateTime;

import com.datastax.spark.connector.japi.CassandraRow;
import scala.Tuple2;

public class CountryBatch extends Batch {

	@Override
	public void run(JavaRDD<CassandraRow> rdd) {
		JavaRDD<Country> list = rdd //
				.mapToPair(row -> Tuple2.apply(Tuple2.apply(row.getString("t_lang"), row.getDateTime("t_ts")), row))
				.aggregateByKey(0, (u, a) -> u + 1, (u1, u2) -> u1 + u2) //
				.map(row -> {
					Country newRow = new Country();
					newRow.count = row._2();
					newRow.country = row._1()._1();
					newRow.year = row._1()._2().getYear();
					newRow.month = row._1()._2().getMonthOfYear();
					newRow.day = row._1()._2().getDayOfMonth();
					newRow.hour = row._1()._2().getHourOfDay();
					newRow.minute = row._1()._2().getMinuteOfHour();
					return newRow;
				});
		
		save(list, "countrylist_day", Country.class);
	}

	public static void main(String[] args) {
		DateTime from = DateTime.now();
		
		Batch batch = new CountryBatch();
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
