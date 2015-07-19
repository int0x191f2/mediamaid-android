package int0x191f2.mediamaid;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
/**
 * Created by ip4gjb on 7/19/15.
 */
public class MediaMaidConfigurationBuilder {
    public ConfigurationBuilder configurationBuilder;
    public static MediaMaidConfigurationBuilder instance = new MediaMaidConfigurationBuilder();

    public static void resetInstance() { instance = new MediaMaidConfigurationBuilder(); }

    public static MediaMaidConfigurationBuilder getInstance() { return instance; }

    public MediaMaidConfigurationBuilder(){
        try{
            configurationBuilder = new ConfigurationBuilder();
            configurationBuilder.setOAuthConsumerKey(BuildVars.TWITTER_CONSUMER_KEY);
            configurationBuilder.setOAuthConsumerSecret(BuildVars.TWITTER_CONSUMER_SECRET);
            configurationBuilder.setOAuthAccessToken(BuildVars.TWITTER_ACCESS_TOKEN_KEY);
            configurationBuilder.setOAuthAccessTokenSecret(BuildVars.TWITTER_ACCESS_TOKEN_SECRET);
            //configurationBuilder.setRestBaseURL("http://127.0.0.1:8081/twitter/");
        }catch(Exception e){
            return;
        }
    }
}
