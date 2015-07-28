package int0x191f2.mediamaid;

import android.util.Log;

import java.util.Arrays;
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
}
