package int0x191f2.mediamaid;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import twitter4j.*;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ip4gjb on 7/21/15.
 */
public class TwitterProfileViewViewAdapter extends RecyclerView.Adapter<TwitterProfileViewViewAdapter.DataObjectHolder> {
    private ProfileViewClickListener clickListener;
    private ArrayList<TwitterProfileViewDataObject> dataset;
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
        public void onClick(View v) { clickListener.onItemClick(getPosition(),v); }
    }
    public void setItemOnClickListener(ProfileViewClickListener clickListener){
        this.clickListener = clickListener;
    }
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.twitter_card_view_layout,parent,false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position){
        final Twitter twatter;
        MediaMaidConfigurationBuilder.resetInstance();
        twatter = new TwitterFactory(MediaMaidConfigurationBuilder.getInstance().configurationBuilder.build()).getInstance();

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

        SpannableString spannableString=new SpannableString(dataset.get(position).getTweetPayload());
        String payload=dataset.get(position).getTweetPayload();
        String tweetArray[]=payload.split(" ");
        for(String a : tweetArray){
            if(a.startsWith("@")){
                spannableString.setSpan(new UserSpan(a.substring(1).replaceAll("[-+.^:,]", "")),
                        payload.indexOf(a),
                        payload.indexOf(a)+a.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        holder.tweetPayload.setText(spannableString);

        //Check if the tweet is on the timeline because it was retweeted
        if(dataset.get(position).getIsRetweet()){
            holder.retweetIndicator.setVisibility(View.VISIBLE);
        }else{
            holder.retweetIndicator.setVisibility(View.GONE);
        }

        //Profile button onClick
        holder.profileImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            }
        });
    }
    public void addItem(TwitterProfileViewDataObject dataObject, int index){
        dataset.add(dataObject);
        notifyItemInserted(index);
    }
    public void removeItem(int index){
        dataset.remove(index);
        notifyItemRemoved(index);
    }
    public TwitterProfileViewViewAdapter(ArrayList<TwitterProfileViewDataObject> dataset){
        this.dataset = dataset;
    }
    public interface ProfileViewClickListener{
        public void onItemClick(int position, View v);
    }
    @Override
    public int getItemCount() { return dataset.size(); }

    class UserSpan extends ClickableSpan {
        String text;
        UserSpan(String text){
            this.text=text;
        }
        public void onClick(View textView){
            try {
                MediaMaidConfigurationBuilder.resetInstance();
                Twitter twitter = new TwitterFactory(MediaMaidConfigurationBuilder.getInstance().configurationBuilder.build()).getInstance();
                Intent intent = new Intent(textView.getContext(), TwitterProfileViewActivity.class);
                intent.putExtra("userhandle", this.text);
                intent.putExtra("username", twitter.showUser(this.text).getName());
                textView.getContext().startActivity(intent);
            }catch (Exception e){
                Log.e("MediaMaid","Error loading user");
            }
        }
        @Override
        public void updateDrawState(TextPaint ds){
            ds.setColor(Color.BLUE);
            ds.setUnderlineText(false);
        }
    }
}
