package int0x191f2.mediamaid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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
    private Boolean isKeysGenerated=false;
    private RequestToken requestToken;
    private AccessToken accessToken;
    private ConfigurationBuilder cb = new ConfigurationBuilder();
    private Twitter twatter;
    private Activity act;
    private Context context;
    public TwitterAuth(Context c,String CONSUMER_KEY, String CONSUMER_SECRET){
        try {
            cb.setOAuthConsumerKey(CONSUMER_KEY);
            cb.setOAuthConsumerSecret(CONSUMER_SECRET);
            Configuration config = cb.build();
            TwitterFactory fact = new TwitterFactory(config);
            twatter = fact.getInstance();
            prefs = c.getSharedPreferences("MediaMaid",0);
            context = c;
            isKeysSet=true;
        }catch(Exception e){
            Log.e("MediaMaid","Error Authenticating with Twitter"+e.toString());
            isKeysSet=false;
        }
    }
    public RequestToken getOAuthRequestToken(){
        return requestToken;
    }
    public void generateOAuthRequestToken(){
        new GenerateAccessToken().execute(requestToken);
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
    public class GenerateAccessToken extends AsyncTask<RequestToken, Integer, RequestToken>{
        @Override
        protected RequestToken doInBackground(RequestToken... params) {
            RequestToken rt = params[0];
            try {
                rt = twatter.getOAuthRequestToken();
                Log.i("MediaMaid", rt.toString());
                return rt;
            } catch (Exception e) {
                Log.e("MediaMaid", "Error creating rt"+e.toString());
                return rt;
            }
        }

        @Override
        protected void onPostExecute(RequestToken s) {
            requestToken = s;
            super.onPostExecute(s);
        }
    }
}
