package int0x191f2.mediamaid;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ip4gjb on 7/28/15.
 */
public class MediaMaidFilteringHandler {
    public static MediaMaidFilteringHandler instance = new MediaMaidFilteringHandler();

    public void resetInstance() { instance = new MediaMaidFilteringHandler(); }

    public static MediaMaidFilteringHandler getInstance() { return instance; }

    public Boolean checkTweetLanguageIsAppropriate(String tweet,String filterList){
        Boolean isAppropriate = true;
        //Check if the filter list is empty
        if(filterList.equals("")){
            return true;
        }
        List<String> stringList = Arrays.asList(filterList.split("\\s*,\\s*"));
        for(String s : stringList ){
            if(tweet.toLowerCase().contains(s.toLowerCase())){
                Log.i("MediaMaid", "Found filter word in tweet -> " + s);
                isAppropriate = false;
            }
        }
        return isAppropriate;
    }

    public String getCensoredTweet(String tweet, String filterList){
        int index=0;
        String censoredTweet="";
        if(filterList.equals("")){
            return tweet;
        }
        List<String> stringList = Arrays.asList(filterList.split("\\s*,\\s*"));
        List<String> tweetList = Arrays.asList(tweet.split("\\s+"));
        List<String> tweetListOriginal = new LinkedList<String>(Arrays.asList(tweet.split("\\s+")));
        for(String s : tweetList){
            for(String a : stringList){
                //Log.i("MediaMaid","Searching : " + s.toString() + " for : " + a.toString());
                if(s.toLowerCase().equals(a.toLowerCase())) {
                    Log.i("MediaMaid", "Found filter word -> " + a);
                    String censoredWord="";
                    for(int i = a.length();i>0;i--){
                        censoredWord = censoredWord + "*";
                    }
                    tweetListOriginal.remove(index);
                    tweetListOriginal.add(index,censoredWord);
                }
            }
            index++;
        }
        for(String a : tweetListOriginal){
            censoredTweet = censoredTweet + " " + a.toString();
        }
        return censoredTweet;
    }
}
