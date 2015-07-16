package int0x191f2.mediamaid;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ip4gjb on 7/11/15.
 */
public class TwitterTimelineClickHandler {
    private Context context;
    private TwitterTweet twitterTweet;
    public TwitterTimelineClickHandler(Context c) {
        context=c;
        twitterTweet = new TwitterTweet(context);
    }
    public void onItemClick(int pos, View view){
        TextView tweetIDtv = (TextView) view.findViewById(R.id.cardViewTweetID);
        Long tweetID = Long.valueOf(tweetIDtv.getText().toString()).longValue();
        String tweetPayload = twitterTweet.getTweetByID(tweetID).getText();
        Toast.makeText(context,tweetPayload,Toast.LENGTH_SHORT).show();
        return;
    }
}
