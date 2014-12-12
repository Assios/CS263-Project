package cs263;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterPoster {

    public static void Tweet(String tweet) {
    	
    	//Posts the tweet to the account https://twitter.com/MoviesProject
    	
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey("NOT_ON_GITHUB")
          .setOAuthConsumerSecret("NOT_ON_GITHUB")
          .setOAuthAccessToken("NOT_ON_GITHUB")
          .setOAuthAccessTokenSecret("NOT_ON_GITHUB");

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter t = tf.getInstance();

        try {
        t.updateStatus(tweet);
        } catch (TwitterException te) {
            te.printStackTrace();
        }
    }
}