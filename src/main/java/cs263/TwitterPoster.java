package cs263;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterPoster {

    public static void Tweet(String title) {
    	
    	//Posts the tweet to the account https://twitter.com/MoviesProject
    	
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey("vuTtP44fQ26Gjy6TwYmFMGcpq")
          .setOAuthConsumerSecret("NOT_ON_GITHUB")
          .setOAuthAccessToken("2927261215-tMlKLqlqzysPrdPgSoWMcL5vgRr7elkWtCaJ2kG")
          .setOAuthAccessTokenSecret("NOT_ON_GITHUB");

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter t = tf.getInstance();

        try {
        t.updateStatus(title + " was added! Check out the movie list at http://bit.ly/1wJHBuw");
        } catch (TwitterException te) {
            te.printStackTrace();
        }
    }
    
    private String ShrinkTweet(String tweet) {
    	//TODO: Shrink movie title so that tweet <= 140 characters
    	return tweet;
    }
}