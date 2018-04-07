package tinkerbell.mashup.dataset;

import java.util.Date;

import org.apache.spark.sql.SparkSession;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.spark.connector.cql.CassandraConnector;

import tinkerbell.mashup.Config;
import twitter4j.Status;

public class DataStream {
	private Cluster cluster;

	private Session session;

	private final PreparedStatement preparedInsert;

	public DataStream() {
		SparkSession sparkSession = SparkSession.builder().config(Config.SPARK_CONF).getOrCreate();

		CassandraConnector connector = CassandraConnector.apply(sparkSession.sparkContext().conf());
		session = connector.openSession();
		
		preparedInsert = session.prepare("insert into " + Config.KEYSPACE + "." + Config.STREAM_TABLE
				+ "(year, month, day, hour, minute, t_id, t_text, t_ts, t_user, t_lang) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
	}
	
	public Session getSession() {
		return this.session;
	}

	public void close() {
		session.close();
		cluster.close();
	}

	public void add(Status status) {
		DateTime t = DateTime.now(DateTimeZone.UTC);

		Date createdAt = status.getCreatedAt();
		BoundStatement bs = preparedInsert.bind(t.getYear(), t.getMonthOfYear(), t.getDayOfMonth(),
						t.getHourOfDay(), t.getMinuteOfHour(), status.getId(), status.getText(), createdAt,
						status.getUser().getName(), status.getLang());

		session.execute(bs);
	}

}
