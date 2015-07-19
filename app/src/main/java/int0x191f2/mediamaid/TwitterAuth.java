package int0x191f2.mediamaid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
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
    //private static SharedPreferences prefs;
    private RequestToken requestToken;
    private AccessToken accessToken;
    private ConfigurationBuilder cb = new ConfigurationBuilder();
    private Twitter twatter;

    public static TwitterAuth instance = new TwitterAuth();

    public static void resetInstance() {
        instance = new TwitterAuth();
    }

    public TwitterAuth(){
        try {
            cb.setOAuthConsumerKey(BuildVars.TWITTER_CONSUMER_KEY);
            cb.setOAuthConsumerSecret(BuildVars.TWITTER_CONSUMER_SECRET);
            Configuration config = cb.build();
            TwitterFactory fact = new TwitterFactory(config);
            twatter = fact.getInstance();
        }catch(Exception e){
            Log.e("MediaMaid","Error Authenticating with Twitter"+e.toString());
        }
    }

    public static TwitterAuth getInstance() {
        return instance;
    }

    public RequestToken getOAuthRequestToken() {
        if (requestToken == null) {
            generateOAuthRequestToken();
        }
        return requestToken;
    }

    public void generateOAuthRequestToken(){
        try {
            requestToken = twatter.getOAuthRequestToken(BuildVars.TWITTER_OAUTH_CALLBACK);
            Log.i("MediaMaid", requestToken.toString());
        } catch (Exception e) {
            Log.e("MediaMaid", "Error creating requestToken"+e.toString());
        }
    }

    public AccessToken generateOAuthAccessToken(String pin){
        try {
            new AccessTokenGenerator().execute(pin).get();
        }
        catch (Exception e) {
            Log.e("MediaMaid", "Fatal threading error");
        }
        return accessToken;
    }

    public RequestToken getRequestToken() {
        return requestToken;
    }

    public class AccessTokenGenerator extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... params) {
            String s = params[0];
            try{
                Log.i("MediaMaid","Access Token Pin:"+s);
                accessToken = twatter.getOAuthAccessToken(requestToken,s);
                return null;

            }catch(Exception e){
                Log.e("MediaMaid","Failed to get accessToken"+e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
