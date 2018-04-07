package tinkerbell.mashup;

import tinkerbell.mashup.dataset.DataStream;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class Main {
	public static void main(String[] args) {
		final Configuration configuration = new ConfigurationBuilder().setDebugEnabled(true)
				.setOAuthConsumerKey("dqPGzLxwEqnczusSbz4Vk5HGU")
				.setOAuthConsumerSecret("aIrDroP92ed7VTFX3N1GVhuQvkzrmvVXZM4h3W1ZrpHySfkqYw")
				.setOAuthAccessToken("981951660271570944-lC8VXeEyXEfQOBGNWg9W0euLfnhZnlZ")
				.setOAuthAccessTokenSecret("KEcg9QK98U77ksGtzu4L6Oo2eUWeRHCdEbxub6RxWdk9N") //
				.build();

		runStream(configuration);
	}
	
	private static void runStream(final Configuration configuration) {
		DataStream ds = new DataStream();

		StatusListener listener = new StatusListener() {
			@Override
			public void onStatus(Status status) {
				ds.add(status);
				System.out.println(status.getText());
				System.out.println("=================");
			}
			
			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			}

			@Override
			public void onScrubGeo(long l, long l1) {
			}

			@Override
			public void onStallWarning(StallWarning stallWarning) {
			}

			@Override
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		};
		
		TwitterStream twitterStream = new TwitterStreamFactory(configuration).getInstance();
		twitterStream.addListener(listener);
		twitterStream.filter(new FilterQuery().track("facebook"));
	}
}
