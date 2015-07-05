package int0x191f2.mediamaid;

import android.content.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
    final static String TWITTER_CONSUMER_KEY = "4dKIk0KoiLRb91DjbES3nfdy5";
    final static String TWITTER_CONSUMER_SECRET = "OJhxMo8lk2N801KxG6e3Nyszx6kUQEsezrX4cFCi2IRtRgotY9";
    public TwitterTimelineHandler(Context c){
        context = c;
    }
    public List<Status> getTimeline(){
        sp = context.getSharedPreferences("MediaMaid",0);
        cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
        cb.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
        cb.setOAuthAccessToken(sp.getString("accessToken", ""));
        cb.setOAuthAccessTokenSecret(sp.getString("accessTokenSecret", ""));
        tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
        List<Status> statuses;
        try{
            statuses = twitter.getHomeTimeline();
        }catch (Exception e){
            statuses = new List<Status>() {
                @Override
                public void add(int location, Status object) {

                }

                @Override
                public boolean add(Status object) {
                    return false;
                }

                @Override
                public boolean addAll(int location, Collection<? extends Status> collection) {
                    return false;
                }

                @Override
                public boolean addAll(Collection<? extends Status> collection) {
                    return false;
                }

                @Override
                public void clear() {

                }

                @Override
                public boolean contains(Object object) {
                    return false;
                }

                @Override
                public boolean containsAll(Collection<?> collection) {
                    return false;
                }

                @Override
                public Status get(int location) {
                    return null;
                }

                @Override
                public int indexOf(Object object) {
                    return 0;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @NonNull
                @Override
                public Iterator<Status> iterator() {
                    return null;
                }

                @Override
                public int lastIndexOf(Object object) {
                    return 0;
                }

                @NonNull
                @Override
                public ListIterator<Status> listIterator() {
                    return null;
                }

                @NonNull
                @Override
                public ListIterator<Status> listIterator(int location) {
                    return null;
                }

                @Override
                public Status remove(int location) {
                    return null;
                }

                @Override
                public boolean remove(Object object) {
                    return false;
                }

                @Override
                public boolean removeAll(Collection<?> collection) {
                    return false;
                }

                @Override
                public boolean retainAll(Collection<?> collection) {
                    return false;
                }

                @Override
                public Status set(int location, Status object) {
                    return null;
                }

                @Override
                public int size() {
                    return 0;
                }

                @NonNull
                @Override
                public List<Status> subList(int start, int end) {
                    return null;
                }

                @NonNull
                @Override
                public Object[] toArray() {
                    return new Object[0];
                }

                @NonNull
                @Override
                public <T> T[] toArray(T[] array) {
                    return null;
                }
            };
            Log.e("MediaMaid","Crashed+"+e.toString());
        }
        return statuses;
    }
}
