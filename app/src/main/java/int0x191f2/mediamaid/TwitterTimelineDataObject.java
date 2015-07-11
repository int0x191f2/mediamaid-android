package int0x191f2.mediamaid;

/**
 * Created by ip4gjb on 7/7/15.
 */
public class TwitterTimelineDataObject {
    private String realName;
    private String userName;
    private String tweetPayload;
    TwitterTimelineDataObject(String rn, String un, String tp){
        realName = rn;
        userName = un;
        tweetPayload = tp;
    }
    public String getUserName(){
        return userName;
    }
    public String getRealName(){
        return realName;
    }
    public String getTweetPayload(){
        return tweetPayload;
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
