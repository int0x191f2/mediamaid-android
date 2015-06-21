package int0x191f2.mediamaid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Adam on 6/18/2015.
 */
public class TwitterAuth {
    static final String TWITTER_CALLBACK_URL = "http://www.google.com";
    private static SharedPreferences prefs;
    private Boolean isKeysSet=false;
    private RequestToken requestToken;
    private AccessToken accessToken;
    private ConfigurationBuilder cb = new ConfigurationBuilder();
    private Twitter twatter;
    public TwitterAuth(Context context,String CONSUMER_KEY, String CONSUMER_SECRET){
        try {
            cb.setOAuthConsumerKey(CONSUMER_KEY);
            cb.setOAuthConsumerSecret(CONSUMER_SECRET);
            Configuration config = cb.build();
            TwitterFactory fact = new TwitterFactory(config);
            twatter = fact.getInstance();
            prefs = context.getSharedPreferences("MediaMaid",0);
            isKeysSet=true;
        }catch(Exception e){
            Log.e("MediaMaid","Error Authenticating with Twitter"+e.toString());
            isKeysSet=false;
        }
    }
    public RequestToken getOAuthRequestToken(){
        if(requestToken!=null){
            return requestToken;
        }
        try {
            requestToken = twatter.getOAuthRequestToken();
            Log.e("MediaMaid", requestToken.toString());
            return requestToken;
        } catch (Exception e) {
            Log.e("MediaMaid", "Error creating requestToken"+e.toString());
            return requestToken;
        }
    }
    public AccessToken getOAuthAccessToken(String pin){
        try{
            Log.e("MediaMaid","Pin:"+pin);
            accessToken = twatter.getOAuthAccessToken(requestToken,pin);
            SharedPreferences.Editor e = prefs.edit();
            e.putString("accessToken",accessToken.getToken());
            e.putString("accessTokenSecret",accessToken.getTokenSecret());
            e.putBoolean("loggedIn",true);
            e.commit();
            return accessToken;
        }catch(Exception e){
            Log.e("MediaMaid","Failed to get accessToken"+e.toString());
            return accessToken;
        }
    }
}
