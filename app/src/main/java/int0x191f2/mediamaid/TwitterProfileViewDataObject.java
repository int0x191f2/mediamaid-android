package int0x191f2.mediamaid;

import android.graphics.Bitmap;

/**
 * Created by ip4gjb on 7/21/15.
 */
public class TwitterProfileViewDataObject {
    private Bitmap profileImage;
    private String realName;
    private String userName;
    private String tweetID;
    private String tweetDate;
    private Boolean isRetweet;
    private Boolean isRetweetByMe;
    private String tweetPayload;
    TwitterProfileViewDataObject(String rn, String un, String ti, String td, Boolean rt, Boolean irtbm, String tp, Bitmap im){
        profileImage = im;
        realName = rn;
        userName = un;
        tweetID = ti;
        tweetDate = td;
        isRetweet = rt;
        isRetweetByMe = irtbm;
        tweetPayload = tp;
    }
    public Bitmap getProfileImage() { return profileImage; }
    public String getUserName(){
        return userName;
    }
    public String getRealName(){
        return realName;
    }
    public String getTweetID() { return tweetID; }
    public String getTweetDate() { return tweetDate; }
    public Boolean getIsRetweet() {return isRetweet; }
    public Boolean getIsRetweetByMe() { return isRetweetByMe; }
    public String getTweetPayload(){
        return tweetPayload;
    }
    public void setProfileImage(Bitmap im) { this.profileImage = im; }
    public void setUserName(String un){
        this.userName = un;
    }
    public void setRealName(String rn){
        this.realName = rn;
    }
    public void setTweetID(String ti) {this.tweetID = ti; }
    public void setTweetDate(String td) { this.tweetDate = td; }
    public void setIsRetweet(Boolean rt) { this.isRetweet = rt; }
    public void setIsRetweetByMe(Boolean irtbm) { this.isRetweetByMe = irtbm; }
    public void setTweetPayload(String tp){ this.tweetPayload = tp; }
}
