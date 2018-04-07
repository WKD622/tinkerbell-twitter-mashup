package tinkerbell.mashup.batch;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.joda.time.DateTime;

import com.datastax.spark.connector.japi.CassandraRow;

import scala.Tuple2;

public class CorrelatedKeywordsBatch extends Batch {
	private static class Keyword implements Serializable {
		private static final long serialVersionUID = 1L;

		public CassandraRow row;
		public String keyword;

		public Keyword(CassandraRow row, String keyword) {
			this.row = row;
			this.keyword = keyword;
		}
	}

	@Override
	public void run(JavaRDD<CassandraRow> rdd) {
		JavaRDD<CorrelatedKeywordsRow> dd = rdd //
				.<Keyword>flatMap(row -> {
					String text = row.getString("t_text");

					List<Keyword> list = Arrays.stream(text.split("\\s+")) //
							.map(String::toLowerCase) //
							.map(kw -> new Keyword(row, kw)) //
							.collect(Collectors.toList());
					return list //
							.iterator();
				}) //
				.filter(kw -> {
					return !kw.keyword.startsWith("http");
				}) //
				.filter(kw -> {
					return kw.keyword.length() < 15;
				}) //
				.mapToPair(k -> {
					return Tuple2.apply(
							Tuple2.apply(k.keyword, k.row.getDateTime("t_ts").withHourOfDay(0).withMinuteOfHour(0)), k);
				}) //
				.aggregateByKey(0, (u, k) -> u + 1, (u1, u2) -> u1 + u2) //
				.map(t -> {
					return CorrelatedKeywordsRow.of(t._1()._1(), t._1()._2(), t._2());
				});

		save(dd, "correlatedkeywords_day", CorrelatedKeywordsRow.class);
	}

	public static void main(String[] args) {
		DateTime from = DateTime.now();

		Batch batch = new CorrelatedKeywordsBatch();
		while (!Thread.interrupted()) {
			if (from.plusMinutes(1).compareTo(DateTime.now()) > 0) {
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
