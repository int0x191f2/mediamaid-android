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
    private static SharedPreferences prefs;
    public RequestToken requestToken;
    private AccessToken accessToken,a;
    private ConfigurationBuilder cb = new ConfigurationBuilder();
    private Twitter twatter;
    private Context context;
    private MainActivity mm = new MainActivity();

    public TwitterAuth(Context c,String CONSUMER_KEY, String CONSUMER_SECRET){
        try {
            cb.setOAuthConsumerKey(CONSUMER_KEY);
            cb.setOAuthConsumerSecret(CONSUMER_SECRET);
            Configuration config = cb.build();
            TwitterFactory fact = new TwitterFactory(config);
            twatter = fact.getInstance();
            prefs = c.getSharedPreferences("MediaMaid",0);
            context = c;
        }catch(Exception e){
            Log.e("MediaMaid","Error Authenticating with Twitter"+e.toString());
        }
    }

    public RequestToken getOAuthRequestToken(){
        return requestToken;
    }

    public void generateOAuthRequestToken(){
        try {
            requestToken = twatter.getOAuthRequestToken();
            Log.i("MediaMaid", requestToken.toString());
        } catch (Exception e) {
            Log.e("MediaMaid", "Error creating requestToken"+e.toString());
        }
    }

    public void generateOAuthAccessToken(String pin){
        new AccessTokenGenerator().execute(pin);
    }

    public void checkOAuthAccessToken(){
        if(accessToken==null){
            Toast.makeText(context,"Error logging into Twitter",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Successfully logged into Twitter", Toast.LENGTH_SHORT).show();
        }
    }

    public void logout(){
        SharedPreferences.Editor e = prefs.edit();
        e.putString("accessToken","");
        e.putString("accessTokenSecret", "");
        e.putBoolean("loggedIn", false);
        e.commit();
        Toast.makeText(context,"Logged out of Twitter",Toast.LENGTH_SHORT).show();
    }

    public class AccessTokenGenerator extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... params) {
            String s = params[0];
            try{
                Log.i("MediaMaid","Access Token Pin:"+s);
                accessToken = twatter.getOAuthAccessToken(requestToken,s);
                SharedPreferences.Editor e = prefs.edit();
                e.putString("accessToken",accessToken.getToken());
                e.putString("accessTokenSecret",accessToken.getTokenSecret());
                e.putBoolean("loggedIn",true);
                e.commit();
                return s;
            }catch(Exception e){
                Log.e("MediaMaid","Failed to get accessToken"+e.toString());
                return s;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            checkOAuthAccessToken();
            super.onPostExecute(s);
        }
    }
}
