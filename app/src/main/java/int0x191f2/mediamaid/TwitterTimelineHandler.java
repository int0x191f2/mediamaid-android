package int0x191f2.mediamaid;

import android.content.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
/**
 * Created by ip4gjb on 6/29/15.
 */
public class TwitterTimelineHandler {
    SharedPreferences sp;
    Context context;
    Twitter twitter;
    ConfigurationBuilder cb;
    TwitterFactory tf;
    Paging page;
    private List<Status> statuses;
    public TwitterTimelineHandler(Context c){
        context = c;
    }
    public List<Status> getTimeline(int count){
        page = new Paging(1,count);
        refreshTimeline();
        return statuses;
    }
    public Status getTimelineByIndex(int index){
        refreshTimeline();
        return statuses.get(index);
    }
    public void refreshTimeline() {
        sp = context.getSharedPreferences("MediaMaid", 0);
        cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(BuildVars.TWITTER_CONSUMER_KEY);
        cb.setOAuthConsumerSecret(BuildVars.TWITTER_CONSUMER_SECRET);
        cb.setOAuthAccessToken(sp.getString("accessToken", ""));
        cb.setOAuthAccessTokenSecret(sp.getString("accessTokenSecret", ""));
        tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
        try {
            statuses = twitter.getHomeTimeline(page);
        } catch (Exception e) {
            Log.e("MediaMaid","Unable to refresh timeline "+e.toString());
        }
    }
}
