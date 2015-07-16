package int0x191f2.mediamaid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ip4gjb on 7/7/15.
 */
public class TwitterTimelineViewAdapter extends RecyclerView.Adapter<TwitterTimelineViewAdapter.DataObjectHolder>{
    private MyClickListener clickListener;
    private ArrayList<TwitterTimelineDataObject> dataset;
    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CircleImageView profileImage;
        TextView realName;
        TextView userName;
        TextView tweetID;
        ImageView retweetIndicator;
        TextView tweetPayload;
        public DataObjectHolder(View itemView){
            super(itemView);
            profileImage = (CircleImageView) itemView.findViewById(R.id.cardViewProfileImage);
            realName = (TextView) itemView.findViewById(R.id.cardViewRealName);
            userName = (TextView) itemView.findViewById(R.id.cardViewUserName);
            tweetID = (TextView) itemView.findViewById(R.id.cardViewTweetID);
            retweetIndicator = (ImageView) itemView.findViewById(R.id.cardViewRetweetIndicator);
            tweetPayload = (TextView) itemView.findViewById(R.id.cardViewTweetPayload);
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
                .inflate(R.layout.timeline_cardview_layout, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
    @Override
    public void onBindViewHolder(DataObjectHolder holder,int position){
        holder.profileImage.setImageBitmap(dataset.get(position).getProfileImage());
        holder.realName.setText(dataset.get(position).getRealName());
        holder.userName.setText(dataset.get(position).getUserName());
        holder.tweetID.setText(dataset.get(position).getTweetID());
        holder.tweetPayload.setText(dataset.get(position).getTweetPayload());
        if(dataset.get(position).getIsRetweet()){
            holder.retweetIndicator.setVisibility(View.VISIBLE);
        }else{
            holder.retweetIndicator.setVisibility(View.GONE);
        }
    }
    public void addItem(TwitterTimelineDataObject dataObject, int index){
        dataset.add(dataObject);
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
