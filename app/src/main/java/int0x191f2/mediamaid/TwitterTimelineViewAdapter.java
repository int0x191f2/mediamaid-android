package int0x191f2.mediamaid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by ip4gjb on 7/7/15.
 */
public class TwitterTimelineViewAdapter extends RecyclerView.Adapter<TwitterTimelineViewAdapter.DataObjectHolder>{
    private MyClickListener clickListener;
    private ArrayList<TwitterTimelineDataObject> dataset;
    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView realName;
        TextView userName;
        TextView tweetPayload;
        public DataObjectHolder(View itemView){
            super(itemView);
            realName = (TextView) itemView.findViewById(R.id.realName);
            userName = (TextView) itemView.findViewById(R.id.userName);
            tweetPayload = (TextView) itemView.findViewById(R.id.tweetPayload);
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
                .inflate(R.layout.twitter_timeline_layout, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
    @Override
    public void onBindViewHolder(DataObjectHolder holder,int position){
        holder.realName.setText(dataset.get(position).getRealName());
        holder.userName.setText(dataset.get(position).getUserName());
        holder.tweetPayload.setText(dataset.get(position).getTweetPayload());
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
