package int0x191f2.mediamaid;

import android.util.Log;

import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

/**
 * Created by Adam on 8/3/2015.
 */
public class TwitterStreamHandler {
    public static UserStreamListener userStreamListener;

    public static TwitterStreamHandler instance = new TwitterStreamHandler();

    public static TwitterStreamHandler getInstance(){ return instance; }

    public static void resetInstance(){instance=new TwitterStreamHandler();}

    public TwitterStreamHandler(){

    }
}
