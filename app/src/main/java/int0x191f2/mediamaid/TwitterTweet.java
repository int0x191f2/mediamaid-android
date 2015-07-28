package int0x191f2.mediamaid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Adam on 6/23/2015.
 */
public class TwitterTweet {
    SharedPreferences sp;
    Context context;
    Twitter twatter;

    public TwitterTweet(Context c){
        context = c;
        sp = context.getSharedPreferences("MediaMaid",0);
        ConfigurationBuilder cb = new ConfigurationBuilder();
        TwitterFactory tf;
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(BuildVars.TWITTER_CONSUMER_KEY);
        cb.setOAuthConsumerSecret(BuildVars.TWITTER_CONSUMER_SECRET);
        cb.setOAuthAccessToken(sp.getString("accessToken",""));
        cb.setOAuthAccessTokenSecret(sp.getString("accessTokenSecret",""));
        tf = new TwitterFactory(cb.build());
        twatter = tf.getInstance();
    }
    public Boolean send(String message){
        if(MediaMaidFilteringHandler.getInstance().checkTweetLanguageIsAppropriate(message,
                sp.getString(BuildVars.SHARED_PREFERENCES_FILTER_LIST_KEY,""))){
                    return false;
        }
        try {
            twatter.updateStatus(message);
            Log.i("MediaMaid", "Sending tweet " + message);
            return true;
        } catch (Exception e) {
            Log.e("MediaMaid",e.toString());
            return false;
        }
    }

    public Status getTweetByID(long id){
        try {
            return twatter.tweets().showStatus(id);
        }catch(Exception e){
            return null;
        }
    }
}
