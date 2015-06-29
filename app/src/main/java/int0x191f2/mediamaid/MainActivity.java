package int0x191f2.mediamaid;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarActivity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class MainActivity extends AppCompatActivity {
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
    private FloatingActionButton fab;
    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private String[] drawerItems = {"Login"};

    public void composeDialog(View view) {
        startActivity(new Intent(this,ComposeActivity.class));
    }

    public void getRequestToken(View view) {
        try{
            this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAuth.getOAuthRequestToken().getAuthorizationURL())));
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"Problems logging in. Check your internet connection",Toast.LENGTH_SHORT).show();
        }
    }
    public void getAccessToken(View view) {
        //twitterAuth.generateOAuthAccessToken(((EditText) (findViewById(R.id.pinInput))).getText().toString());
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
        //Create the Twitter Authenticator
        twitterAuth = new int0x191f2.mediamaid.TwitterAuth(getApplicationContext(),TWITTER_CONSUMER_KEY,TWITTER_CONSUMER_SECRET);
        twitterAuth.generateOAuthRequestToken();
        //Set the toolbar title
        Toolbar tb = (Toolbar) findViewById(R.id.mainToolbar);
        tb.setTitleTextColor(0xFFFFFFFF);
        if(tb!=null){
            setSupportActionBar(tb);
        }
        getSupportActionBar().setTitle("MediaMaid");
        //Disable back button in top level of application
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        sp = getApplicationContext().getSharedPreferences("MediaMaid",0);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerListView = (ListView) findViewById(R.id.navDrawer);
        drawerListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerItems));

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
            this.startActivity(new Intent(this,settings.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
