package int0x191f2.mediamaid;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by ip4gjb on 7/7/15.
 */
public class TwitterTimelineViewAdapter extends RecyclerView.Adapter<TwitterTimelineViewAdapter.DataObjectHolder>{
    private MyClickListener clickListener;
    private ArrayList<TwitterTimelineDataObject> dataset;
    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout cardViewActionBar;
        CircleImageView profileImage;
        TextView realName;
        TextView userName;
        TextView tweetID;
        TextView tweetDate;
        ImageView retweetIndicator;
        TextView tweetPayload;
        ImageButton actionRetweet;
        public DataObjectHolder(View itemView){
            super(itemView);
            cardViewActionBar = (LinearLayout) itemView.findViewById(R.id.cardViewActionBar);
            profileImage = (CircleImageView) itemView.findViewById(R.id.cardViewProfileImage);
            realName = (TextView) itemView.findViewById(R.id.cardViewRealName);
            userName = (TextView) itemView.findViewById(R.id.cardViewUserName);
            tweetID = (TextView) itemView.findViewById(R.id.cardViewTweetID);
            tweetDate = (TextView) itemView.findViewById(R.id.cardViewTweetDate);
            retweetIndicator = (ImageView) itemView.findViewById(R.id.cardViewRetweetIndicator);
            tweetPayload = (TextView) itemView.findViewById(R.id.cardViewTweetPayload);
            actionRetweet = (ImageButton) itemView.findViewById(R.id.cardViewActionRetweet);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v){
            clickListener.onItemClick(getPosition(),v);
        }
    }
    public void setItemOnClickListener(MyClickListener clickListener){
        this.clickListener = clickListener;
    }
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.twitter_card_view_layout, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position){
        final Twitter twatter;
        ConfigurationBuilder cb = new ConfigurationBuilder();
        TwitterFactory tf;
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(BuildVars.TWITTER_CONSUMER_KEY);
        cb.setOAuthConsumerSecret(BuildVars.TWITTER_CONSUMER_SECRET);
        cb.setOAuthAccessToken(BuildVars.TWITTER_ACCESS_TOKEN_KEY);
        cb.setOAuthAccessTokenSecret(BuildVars.TWITTER_ACCESS_TOKEN_SECRET);
        tf = new TwitterFactory(cb.build());
        twatter = tf.getInstance();
        final Long id = Long.valueOf(dataset.get(position).getTweetID()).longValue();
        
        if(dataset.get(position).getIsRetweetByMe()) {
            holder.actionRetweet.setImageResource(R.drawable.ic_retweet_true);
        }
        holder.profileImage.setImageBitmap(dataset.get(position).getProfileImage());
        holder.realName.setText(dataset.get(position).getRealName());
        holder.userName.setText("@"+dataset.get(position).getUserName());
        holder.tweetID.setText(dataset.get(position).getTweetID());
        holder.tweetDate.setText(dataset.get(position).getTweetDate());
        holder.tweetPayload.setText(dataset.get(position).getTweetPayload());

        //Check if the tweet is on the timeline because it was retweeted
        if(dataset.get(position).getIsRetweet()){
            holder.retweetIndicator.setVisibility(View.VISIBLE);
        }else{
            holder.retweetIndicator.setVisibility(View.GONE);
        }

        //Favorite button click listener
        holder.actionRetweet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                        try {
                            Status status = twatter.tweets().showStatus(id);
                            if(status.isRetweetedByMe()){
                                twatter.destroyStatus(status.getCurrentUserRetweetId());
                                dataset.get(position).setIsRetweetByMe(false);
                                holder.actionRetweet.setImageResource(R.drawable.ic_retweet);
                            }else{
                                twatter.retweetStatus(id);
                                dataset.get(position).setIsRetweetByMe(true);
                                holder.actionRetweet.setImageResource(R.drawable.ic_retweet_true);
                            }

                        } catch (Exception e) {}
                        Log.i("MediaMaid", "clicked da button");
                        //if the user clicked the button, the cardViewActionBar must be visible so no need to check for it being open
                holder.cardViewActionBar.setVisibility(View.GONE);
            }
        });

        //Profile button onClick
        holder.profileImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(),TwitterProfileViewActivity.class);
                intent.putExtra("id",dataset.get(position).getTweetID());
                v.getContext().startActivity(intent);
                Log.i("MediaMaid", dataset.get(position).getUserName());
                Log.i("MediaMaid", dataset.get(position).getTweetID());
                Log.i("MediaMaid",String.valueOf(position));
            }
        });


    }
    public void addItem(TwitterTimelineDataObject dataObject, int index){
        dataset.add(index, dataObject);
        notifyItemInserted(index);
    }
    public void removeItem(int index){
        dataset.remove(index);
        notifyItemRemoved(index);
    }
    public TwitterTimelineViewAdapter(ArrayList<TwitterTimelineDataObject> dataset){
        this.dataset = dataset;
    }
    public interface MyClickListener{
        public void onItemClick(int position, View v);
    }
    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
