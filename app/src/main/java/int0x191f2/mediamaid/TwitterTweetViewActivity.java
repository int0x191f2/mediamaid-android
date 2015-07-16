package int0x191f2.mediamaid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import de.hdodenhof.circleimageview.CircleImageView;
import twitter4j.Status;

public class TwitterTweetViewActivity extends AppCompatActivity {
    private TwitterTweet twitterTweet;
    private TwitterPictureCacheHandler twitterPictureCacheHandler;
    private Status tweet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_tweet_view);
        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.tweetViewCircleImageView);
        twitterTweet = new TwitterTweet(getApplicationContext());
        twitterPictureCacheHandler = new TwitterPictureCacheHandler(getApplicationContext());
        String tweetID = getIntent().getStringExtra("tweetID");
        tweet = twitterTweet.getTweetByID(Long.valueOf(tweetID).longValue());

        //Set the circleimageview to the user profile picture of 'tweet'
        circleImageView.setImageBitmap(twitterPictureCacheHandler.getProfileImageByUser(tweet.getUser().getScreenName(),tweet.getUser().getOriginalProfileImageURL()));
    }
}
