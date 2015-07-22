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
    private List<Status> homeTimelineStatuses,userTimelineStatuses;
    public TwitterTimelineHandler(Context c){
        context = c;
    }
    public List<Status> getTimeline(int count){
        Paging page = new Paging(1,count);
        refreshTimeline(page);
        return homeTimelineStatuses;
    }
    public List<Status> getUserTimeline(int count, String handle){
        Paging page = new Paging(1,count);
        refreshUserTimeline(page, handle);
        return userTimelineStatuses;
    }
    public void refreshUserTimeline(Paging pager, String handle){
        MediaMaidConfigurationBuilder.resetInstance();
        tf = new TwitterFactory(MediaMaidConfigurationBuilder.getInstance().configurationBuilder.build());
        twitter = tf.getInstance();
        try {
            userTimelineStatuses = twitter.getUserTimeline(handle,pager);
        }catch(Exception e){
            Log.e("MediaMaid","Unable to refresh user ("+handle+") timeline "+e.toString());
        }
    }
    public void refreshTimeline(Paging pager) {
        MediaMaidConfigurationBuilder.resetInstance();
        tf = new TwitterFactory(MediaMaidConfigurationBuilder.getInstance().configurationBuilder.build());
        twitter = tf.getInstance();
        try {
            homeTimelineStatuses = twitter.getHomeTimeline(pager);
        } catch (Exception e) {
            Log.e("MediaMaid","Unable to refresh timeline "+e.toString());
        }
    }
}
