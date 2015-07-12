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

    public void getAccessToken(View view){
        twitterAuth.generateOAuthAccessToken(((EditText) findViewById(R.id.twitterPinInput)).getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_login);
        twitterAuth = new TwitterAuth(getApplicationContext(),BuildVars.TWITTER_CONSUMER_KEY,BuildVars.TWITTER_CONSUMER_SECRET);
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
        getMenuInflater().inflate(R.menu.menu_twitter_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
