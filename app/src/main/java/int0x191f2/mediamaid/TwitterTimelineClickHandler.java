package int0x191f2.mediamaid;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
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
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.cardViewActionBar);
        if(linearLayout.getVisibility()==View.VISIBLE){
            linearLayout.setVisibility(View.GONE);
        }else if(linearLayout.getVisibility()==View.GONE){
            linearLayout.setVisibility(View.VISIBLE);
        }
        Intent intent = new Intent(context,TwitterTweetViewActivity.class);
        intent.putExtra("tweetID",tweetIDtv.getText().toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startActivity(intent);
        return;
    }
}
