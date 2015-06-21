package int0x191f2.mediamaid;

import android.app.Activity;
import android.content.Intent;
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
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
    static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

    public void submitTweet(View view) {
        new TwitterSendTweet().execute(((EditText) findViewById(R.id.tweetInput)).getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        protected String doInBackground(String... strang){
            cb.setDebugEnabled(true);
            cb.setOAuthConsumerKey("4dKIk0KoiLRb91DjbES3nfdy5");
            cb.setOAuthConsumerSecret("OJhxMo8lk2N801KxG6e3Nyszx6kUQEsezrX4cFCi2IRtRgotY9");
            cb.setOAuthAccessToken("2764831833-lbt8G9In5OejzX2kBd6N3gx4k1x1p94FGEEuawf");
            cb.setOAuthAccessTokenSecret("VpHI16z3XW6vRxHsJylXzcF1w6TTkmz9LNh47ivbjxH2g");
            String tweetToSubmit = strang[0];
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
    }
}
