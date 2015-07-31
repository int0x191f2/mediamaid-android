package int0x191f2.mediamaid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import android.util.Log;

import org.apache.http.StatusLine;

import de.hdodenhof.circleimageview.CircleImageView;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    public String[] drawerItems = {"Login","Settings"};
    private static SharedPreferences sp;
    private TwitterAuth twitterAuth;
    private TwitterPictureCacheHandler twitterPictureCacheHandler;
    private com.melnykov.fab.FloatingActionButton fab;
    private ListView drawerListView;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private TwitterTimelineHandler timelineHandler;
    private ConnectionDetector connectionDetector;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CircleImageView mCircleImageView;
    private CardView mCardView;


    public void composeDialog(View view) {
        startActivity(new Intent(this, ComposeActivity.class));
    }

    public void updateTimeline(){
        startTimelineOutAnimation();
        new GetDataSet().execute(new ArrayList<TwitterTimelineDataObject>());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Enable networking on the main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Create the ConnectionDetector
        connectionDetector = new ConnectionDetector(getApplicationContext());
        if(!connectionDetector.isConnectingToInternet()){
            Toast.makeText(getApplicationContext(),"No internet connection detected", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
        }

        //Create the Twitter Authenticator
        twitterAuth = TwitterAuth.getInstance();
        //Create the timelineHandler
        timelineHandler = new TwitterTimelineHandler(getApplicationContext());
        //Create the picture cache handler
        twitterPictureCacheHandler = new TwitterPictureCacheHandler(getApplicationContext());
        //Set the toolbar title and color
        Toolbar tb = (Toolbar) findViewById(R.id.mainToolbar);
        tb.setTitleTextColor(0xFFFFFFFF);
        if(tb!=null){
            setSupportActionBar(tb);
        }
        getSupportActionBar().setTitle("MediaMaid");

        sp = getApplicationContext().getSharedPreferences("MediaMaid",0);

        //Start the login activity if the user isn't logged in
        if(!sp.getBoolean(BuildVars.SHARED_PREFERENCES_LOGGED_IN_KEY,false)){
            Log.i("MediaMaid", "We weren't logged in OR we just logged in");
            Uri uri = getIntent().getData();
            //this means we just came from the login intent
            if (uri != null && uri.toString().startsWith(BuildVars.TWITTER_OAUTH_CALLBACK) && uri.getQueryParameter(BuildVars.TWITTER_OAUTH_DENIED)==null) {
                Log.i("MediaMaid", "We are attempting to log in with the OAuth");
                String verifier = uri.getQueryParameter(BuildVars.TWITTER_OAUTH_VERIFIER);
                Log.i("MediaMaid", "verifier: "+verifier);
                AccessToken token = twitterAuth.generateOAuthAccessToken(verifier);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(BuildVars.SHARED_PREFERENCES_ACCESS_TOKEN_KEY, token.getToken());
                editor.putString(BuildVars.SHARED_PREFERENCES_ACCESS_TOKEN_SECRET_KEY, token.getTokenSecret());
                editor.putBoolean(BuildVars.SHARED_PREFERENCES_LOGGED_IN_KEY, true);
                editor.apply();
            } else { //this means we need to log in
                Log.i("MediaMaid", "We really weren't logged in and started the login activity");
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        }

        //Set up the recyclerview and fab integration
        mCircleImageView = (CircleImageView) findViewById(R.id.cardViewProfileImage);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.timelineRefreshLayout);
        fab = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.fab);
        mCardView = (CardView) findViewById(R.id.card_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.timeline_recycler_view);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                updateTimeline();
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        fab.attachToRecyclerView(mRecyclerView);

        //Disable back button in top level of application
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawer,tb,R.string.drawer_open, R.string.drawer_close);
        mToggle.setDrawerIndicatorEnabled(true);
        mDrawer.setDrawerListener(mToggle);

        BuildVars.TWITTER_ACCESS_TOKEN_KEY = sp.getString(BuildVars.SHARED_PREFERENCES_ACCESS_TOKEN_KEY,"");
        BuildVars.TWITTER_ACCESS_TOKEN_SECRET = sp.getString(BuildVars.SHARED_PREFERENCES_ACCESS_TOKEN_SECRET_KEY,"");

        //Get the timeline if the user is logged in
        if(sp.getBoolean("loggedIn",false)){
            updateTimeline();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        //Set up the inital drawer
        updateDrawer();
    }
    private void updateDrawer(){
        //Check that the user is logged in and set the drawer label accordingly
        if(!sp.getBoolean("loggedIn",true)){
            drawerItems[0] = "Login";
        }else{
            drawerItems[0] = "Logout";
        }

        //Drawer list and action handling
        drawerListView = (ListView) findViewById(R.id.navDrawer);
        drawerListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerItems));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id){
            navigationDrawerItem(position);
        }
    }

    private void navigationDrawerItem(int position) {
        //Login button
        if(position==0){
            if(connectionDetector.isConnectingToInternet()) {
                //Check that the user is logged in
                if (sp.getBoolean("loggedIn", true)) {
                    Log.e("MediaMaid", "Logging out");
                    logout();  //Keep the login/logout consistent
                    updateDrawer(); //Clear timeline on logout
                    //getTimeline();
                    Intent returnToLogin = new Intent(this,LoginActivity.class);
                    startActivity(returnToLogin);
                    finish();

                } else {
                    Log.e("MediaMaid", "Logging in");
                    //Start the login activity
                    startActivity(new Intent(this, TwitterLoginActivity.class));
                }
            }else{
                Toast.makeText(getApplicationContext(),"No internet connection detected", Toast.LENGTH_SHORT).show();
            }
        }
        if(position==1){
            startActivity(new Intent(this,SettingsActivity.class));
        }
    }

    private void logout() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(BuildVars.SHARED_PREFERENCES_ACCESS_TOKEN_KEY,"");
        editor.putString(BuildVars.SHARED_PREFERENCES_ACCESS_TOKEN_SECRET_KEY,"");
        editor.putBoolean(BuildVars.SHARED_PREFERENCES_LOGGED_IN_KEY, false);
        editor.apply();
        TwitterAuth.resetInstance();
        twitterAuth = TwitterAuth.getInstance();
    }

    private class GetDataSet extends AsyncTask<ArrayList<TwitterTimelineDataObject>,Integer,ArrayList<TwitterTimelineDataObject>>{

        @Override
        protected ArrayList<TwitterTimelineDataObject> doInBackground(ArrayList<TwitterTimelineDataObject>... params) {
            int index=0;
            ArrayList results = new ArrayList<TwitterTimelineDataObject>();
            List<twitter4j.Status> statuses = timelineHandler.getTimeline(40);

            for (twitter4j.Status status : statuses) {
                //Get the censored tweet payload
                String censoredTweetPayload = MediaMaidFilteringHandler.getInstance().getCensoredTweet(status.getText(),
                        sp.getString(BuildVars.SHARED_PREFERENCES_FILTER_LIST_KEY,""));
                TwitterTimelineDataObject obj = new TwitterTimelineDataObject(status.getUser().getName(),
                        status.getUser().getScreenName(),
                        String.valueOf(status.getId()),
                        //TODO make the date/time work
                        "$(date)",
                        status.isRetweet(),
                        status.isRetweetedByMe(),
                        censoredTweetPayload,
                        twitterPictureCacheHandler.getProfileImageByUser(status.getUser().getScreenName(), status.getUser().getOriginalProfileImageURL()));
                results.add(index, obj);
                index++;
                }
            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<TwitterTimelineDataObject> twitterTimelineDataObjects) {
            super.onPostExecute(twitterTimelineDataObjects);
            mAdapter = new TwitterTimelineViewAdapter(twitterTimelineDataObjects);
            mRecyclerView.setAdapter(mAdapter);
            ((TwitterTimelineViewAdapter) mAdapter).setItemOnClickListener(new
                                                                                   TwitterTimelineViewAdapter.MyClickListener() {
                                                                                       @Override
                                                                                       public void onItemClick(int pos, View v) {
                                                                                           new TwitterTimelineClickHandler(getApplicationContext()).onItemClick(pos, v);
                                                                                       }
                                                                                   });

//            StatusListener listener = new StatusListener() {
//                @Override
//                public void onStatus(twitter4j.Status status) {
//                    TwitterTimelineDataObject obj = new TwitterTimelineDataObject(status.getUser().getName(),
//                            status.getUser().getScreenName(),
//                            String.valueOf(status.getId()),
//                            //TODO make the date/time work
//                            "$(date)",
//                            status.isRetweet(),
//                            status.isRetweetedByMe(),
//                            status.getText(),
//                            twitterPictureCacheHandler.getProfileImageByUser(status.getUser().getScreenName(), status.getUser().getOriginalProfileImageURL()));
//                    ((TwitterTimelineViewAdapter) mAdapter).addItem(obj,mAdapter.getItemCount()+1);
//                }
//
//                @Override
//                public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
//                @Override
//                public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
//                @Override
//                public void onScrubGeo(long userId, long upToStatusId) {}
//                @Override
//                public void onStallWarning(StallWarning warning) {}
//                @Override
//                public void onException(Exception ex) {
//                    Log.e("MediaMaid",ex.toString());
//                }
//            };
//            TwitterStreamFactory twitterStreamFactory;
//            twitterStreamFactory = new TwitterStreamFactory(MediaMaidConfigurationBuilder.getInstance().configurationBuilder.build());
//            TwitterStream twitterStream = twitterStreamFactory.getInstance();
//            twitterStream.addListener(listener);
//            twitterStream.sample();
            mSwipeRefreshLayout.setRefreshing(false);
            startTimelineInAnimation();
        }
    }

    public void startTimelineInAnimation(){
        TranslateAnimation anim = new TranslateAnimation(-2*mRecyclerView.getWidth(),0,0,0);
        anim.setDuration(1000);
        //mRecyclerView.startAnimation(anim);
    }

    public void startTimelineOutAnimation(){
        TranslateAnimation anim = new TranslateAnimation(0,2*mRecyclerView.getWidth(),0,0);
        anim.setDuration(1000);
        //mRecyclerView.startAnimation(anim);
    }

    public boolean checkAuthentication(){
        try{
            timelineHandler.getTimeline(1);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
