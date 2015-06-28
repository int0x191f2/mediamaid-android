package int0x191f2.mediamaid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;


public class TwitterLoginActivity extends AppCompatActivity {
    private AccessToken accessToken;
    private int0x191f2.mediamaid.TwitterAuth twitterAuth;
    private SharedPreferences prefs;
    final static String TWITTER_CONSUMER_KEY = "4dKIk0KoiLRb91DjbES3nfdy5";
    final static String TWITTER_CONSUMER_SECRET = "OJhxMo8lk2N801KxG6e3Nyszx6kUQEsezrX4cFCi2IRtRgotY9";

    public void getAccessToken(View view){
        twitterAuth.generateOAuthAccessToken(((EditText) findViewById(R.id.twitterPinInput)).getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_login);
        twitterAuth = new TwitterAuth(getApplicationContext(),TWITTER_CONSUMER_KEY,TWITTER_CONSUMER_SECRET);
        Toolbar tb = (Toolbar) findViewById(R.id.twitterLoginToolbar);
        tb.setTitleTextColor(0xFFFFFFFF);
        if(tb!=null){
            setSupportActionBar(tb);
        }
        getSupportActionBar().setTitle("Login to Twitter");
        //Enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefs = getApplicationContext().getSharedPreferences("MediaMaid", 0);
        String rts = prefs.getString("requestToken","");
        //To prevent creating the intent without making the token first
        //the creation of the token isn't made in a thread
        twitterAuth.generateOAuthRequestToken();
        Toast.makeText(getApplicationContext(),"Copy the PIN from Twitter",Toast.LENGTH_SHORT).show();
        this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAuth.getOAuthRequestToken().getAuthorizationURL())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twitter_login, menu);
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
}
