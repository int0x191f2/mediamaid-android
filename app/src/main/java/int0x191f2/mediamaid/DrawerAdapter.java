package int0x191f2.mediamaid;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Adam on 7/31/2015.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {
    private static final int TYPE_HEADER=0;
    private static final int TYPE_ITEM=1;
    private String mNavTitles[];
    private int mNavIcons[];
    private String name, username;
    private Bitmap profileImage, coverImage;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int holderid;
        TextView itemTextView, nameTextView, userNameTextView;
        ImageView iconImageView, profileImageView;
        RelativeLayout coverImageLayout;

        public ViewHolder(View itemView, int ViewType){
            super(itemView);
            if (ViewType == TYPE_ITEM) {
                itemTextView = (TextView) itemView.findViewById(R.id.navigationDrawerRowText);
                iconImageView = (ImageView) itemView.findViewById(R.id.navigationDrawerRowIcon);
                holderid=1;
            }else{
                nameTextView = (TextView) itemView.findViewById(R.id.navigationDrawerHeaderName);
                userNameTextView = (TextView) itemView.findViewById(R.id.navigationDrawerHeaderUserName);
                profileImageView = (ImageView) itemView.findViewById(R.id.navigationDrawerProfileImage);
                coverImageLayout = (RelativeLayout) itemView.findViewById(R.id.navigationDrawerRelativeLayout);
            }
        }
    }

    DrawerAdapter(String titles[],int icons[], String name, String username, Bitmap profile, Bitmap cover){
        this.mNavTitles = titles;
        this.mNavIcons = icons;
        this.name = name;
        this.username = username;
        this.profileImage = profile;
        this.coverImage = cover;
    }

    @Override
    public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_drawer_item_row,parent,false);
            return new ViewHolder(v,viewType);
        }else if(viewType==TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_drawer_header,parent,false);
            return new ViewHolder(v,viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(DrawerAdapter.ViewHolder holder, int position){
        if(holder.holderid==1){
            holder.itemTextView.setText(mNavTitles[position-1]);
            holder.iconImageView.setImageResource(mNavIcons[position-1]);
        }else{
            holder.nameTextView.setText(name);
            holder.userNameTextView.setText(username);
            holder.profileImageView.setImageBitmap(profileImage);

            BitmapDrawable background = new BitmapDrawable(coverImage);
            holder.coverImageLayout.setBackgroundDrawable(background);
        }
    }

    @Override
    public int getItemCount(){
        return mNavTitles.length+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position)){
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    public boolean isPositionHeader(int position){
        return position==0;
    }
}
