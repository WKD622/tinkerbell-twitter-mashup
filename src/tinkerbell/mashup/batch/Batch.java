package tinkerbell.mashup.batch;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.joda.time.DateTime;

import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.datastax.spark.connector.japi.CassandraRow;
import com.datastax.spark.connector.rdd.reader.RowReaderFactory;
import com.datastax.spark.connector.writer.RowWriterFactory;

import scala.Tuple2;
import tinkerbell.mashup.Config;

public abstract class Batch {
	private static SparkContext context;

	public Batch() {
		if (context == null)
			context = new SparkContext(Config.SPARK_CONF);
	}

	protected SparkContext context() {
		return context;
	}

	protected abstract void run(JavaRDD<CassandraRow> rdd);

	public final void run(DateTime dt) {
		System.out.println("Running for: " + dt);
		String where = new StringBuilder() //
				.append("year = ").append(dt.getYear()) //
				.append(" and month = ").append(dt.getMonthOfYear()) //
				.append(" and day = ").append(dt.getDayOfMonth()) //
				.append(" and hour = ").append(dt.getHourOfDay()) //
				.append(" and minute = ").append(dt.getMinuteOfHour()) //
				.toString();

		System.out.println("Selecting from: " + where);

		JavaRDD<CassandraRow> rdd = CassandraJavaUtil.javaFunctions(context()) //
				.cassandraTable("twittermashup", Config.STREAM_TABLE) //
				.where(where);

		run(rdd);
	}

	protected <T> void save(JavaRDD<T> dd, String tableName, Class<T> mapper) {
		CassandraJavaUtil.javaFunctions(dd)
				.writerBuilder("twittermashup", tableName, CassandraJavaUtil.mapToRow(mapper)).saveToCassandra();
	}
}
