package tinkerbell.mashup;

import org.apache.spark.SparkConf;

public class Config {
	public static final SparkConf SPARK_CONF;
	static {
		SPARK_CONF = new SparkConf(true);
		SPARK_CONF.setAppName("twitter-hackaton-mashup");
		SPARK_CONF.setMaster("local[*]");
		SPARK_CONF.set("spark.cassandra.connection.host", Config.CASSANDRA_HOST);
		SPARK_CONF.set("spark.cassandra.connection.port", Config.CASSANDRA_PORT);
	}
	
	private Config() {}
	
	public static final String KEYSPACE = "twittermashup";
	public static final String STREAM_TABLE = "stream";
	public static final String CASSANDRA_HOST = "localhost";
	public static final String CASSANDRA_PORT = "9042";
}
