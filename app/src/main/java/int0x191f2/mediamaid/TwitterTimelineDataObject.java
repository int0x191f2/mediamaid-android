package int0x191f2.mediamaid;

import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.util.Log;

/**
 * Created by ip4gjb on 7/7/15.
 */
public class TwitterTimelineDataObject {
    private Bitmap profileImage;
    private String realName;
    private String userName;
    private String tweetPayload;
    TwitterTimelineDataObject(String rn, String un, String tp, Bitmap im){
        profileImage = im;
        realName = rn;
        userName = un;
        tweetPayload = tp;
    }
    public Bitmap getProfileImage() { return profileImage; }
    public String getUserName(){
        return userName;
    }
    public String getRealName(){
        return realName;
    }
    public String getTweetPayload(){
        return tweetPayload;
    }
    public void setProfileImage(Bitmap im) {
        this.profileImage = im;
        Log.i("MediaMaid",im.toString());
    }
    public void setUserName(String un){
        this.userName = un;
    }
    public void setRealName(String rn){
        this.realName = rn;
    }
    public void setTweetPayload(String tp){
        this.tweetPayload = tp;
    }
}
