package int0x191f2.mediamaid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
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


public class MainActivity extends Activity {
    Twitter twatter;
    ConfigurationBuilder cb = new ConfigurationBuilder();
    TwitterFactory tf;
    final static String TWITTER_CONSUMER_KEY = "4dKIk0KoiLRb91DjbES3nfdy5";
    final static String TWITTER_CONSUMER_SECRET = "OJhxMo8lk2N801KxG6e3Nyszx6kUQEsezrX4cFCi2IRtRgotY9";
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLoggedIn";
    private static SharedPreferences sp;
    private static ConnectionDetector cd;
    private AccessToken accessToken;
    private int0x191f2.mediamaid.TwitterAuth twitterAuth;

    public void submitTweet(View view) {
        new TwitterSendTweet().execute(((EditText) findViewById(R.id.tweetInput)).getText().toString());
    }

    public void getRequestToken(View view) {
        //Log.e("MediaMaid", twitterAuth.getOAuthRequestToken().getAuthorizationURL());
        if(twitterAuth.getOAuthRequestToken()==null){
            Toast.makeText(getApplicationContext(),"requestToken is null!",Toast.LENGTH_SHORT).show();
        }
        try {
            Log.e("MediaMaid","oh crap/s");
            this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAuth.getOAuthRequestToken().getAuthorizationURL())));

        }catch(Exception e){
            Log.e("MediaMaid",e.toString());
        }
    }
    public void getAccessToken(View view) {
        accessToken = twitterAuth.getOAuthAccessToken(((EditText) (findViewById(R.id.pinInput))).getText().toString());
        if(accessToken==null){
            Toast.makeText(getApplicationContext(),"Problems athenticating with Twitter",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Successfully authenticated with Twitter",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()){
            Toast.makeText(getApplicationContext(),"No Internet Connection Detected",Toast.LENGTH_SHORT).show();
        }
        twitterAuth = new int0x191f2.mediamaid.TwitterAuth(getApplicationContext(),TWITTER_CONSUMER_KEY,TWITTER_CONSUMER_SECRET);
        sp = getApplicationContext().getSharedPreferences("MediaMaid",0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




















    public class TwitterSendTweet extends AsyncTask<String,Integer,String> {
        Twitter twatter;
        ConfigurationBuilder cb = new ConfigurationBuilder();
        TwitterFactory tf;
        protected String doInBackground(String... params){
            cb.setDebugEnabled(true);
            cb.setOAuthConsumerKey("4dKIk0KoiLRb91DjbES3nfdy5");
            cb.setOAuthConsumerSecret("OJhxMo8lk2N801KxG6e3Nyszx6kUQEsezrX4cFCi2IRtRgotY9");
            cb.setOAuthAccessToken(sp.getString("accessToken",""));
            cb.setOAuthAccessTokenSecret(sp.getString("accessTokenSecret",""));
            String tweetToSubmit = params[0];
            tf = new TwitterFactory(cb.build());
            twatter = tf.getInstance();
            try {
                twatter.updateStatus(tweetToSubmit);
                Log.i("MediaMaid", "Sending tweet " + tweetToSubmit);
                return "42";
            } catch (Exception e) {
                Log.e("MediaMaid",e.toString());
                return e.toString();
            }
        }
        protected void onPostExecute(String result){
            if(result.equals("42")){
                Toast.makeText(getApplicationContext(),"Tweeted",Toast.LENGTH_SHORT).show();
                ((EditText) findViewById(R.id.tweetInput)).setText("");
            }else{
                Toast.makeText(getApplicationContext(),result.toUpperCase(),Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class TwitterAuth extends AsyncTask <String,Integer,String> {
        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
