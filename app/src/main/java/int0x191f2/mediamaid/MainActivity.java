package int0x191f2.mediamaid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import android.util.Log;

import de.hdodenhof.circleimageview.CircleImageView;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    public String[] drawerItems = {"Search","Settings", "Logout"};
    public int[] drawerIcons = {R.drawable.ic_settings,R.drawable.ic_settings, R.drawable.ic_logout};
    private static SharedPreferences sp,sp_settings;
    private TwitterAuth twitterAuth;
    private TwitterPictureCacheHandler twitterPictureCacheHandler;
    private com.melnykov.fab.FloatingActionButton fab;
    private ListView drawerListView;
    private DrawerLayout mDrawer;
    private RecyclerView mDrawerRecyclerView;
    private RecyclerView.Adapter mDrawerRecyclerViewAdapter;
    private RecyclerView.LayoutManager mDrawerLayoutManager;
    private GestureDetector mDrawerGestureDetector;
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

    public void updateTimeline() {
        startTimelineOutAnimation();
        new GetDataSet().execute(new ArrayList<TwitterTimelineDataObject>());
    }

    public void onCreateUnthreadedWork() {
        updateUserValues();
        updateDrawer();
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
        if (!connectionDetector.isConnectingToInternet()) {
            Toast.makeText(getApplicationContext(), "No internet connection detected", Toast.LENGTH_SHORT).show();
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
        if (tb != null) {
            setSupportActionBar(tb);
        }
        getSupportActionBar().setTitle("MediaMaid");

        sp = getApplicationContext().getSharedPreferences("MediaMaid", 0);
        sp_settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Start the login activity if the user isn't logged in
        if (!sp.getBoolean(BuildVars.SHARED_PREFERENCES_LOGGED_IN_KEY, false)) {
            Log.i("MediaMaid", "We weren't logged in OR we just logged in");
            Uri uri = getIntent().getData();
            //this means we just came from the login intent
            if (uri != null && uri.toString().startsWith(BuildVars.TWITTER_OAUTH_CALLBACK) && uri.getQueryParameter(BuildVars.TWITTER_OAUTH_DENIED) == null) {
                Log.i("MediaMaid", "We are attempting to log in with the OAuth");
                String verifier = uri.getQueryParameter(BuildVars.TWITTER_OAUTH_VERIFIER);
                Log.i("MediaMaid", "verifier: " + verifier);
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
        mToggle = new ActionBarDrawerToggle(this, mDrawer, tb, R.string.drawer_open, R.string.drawer_close);
        mToggle.setDrawerIndicatorEnabled(true);
        mDrawer.setDrawerListener(mToggle);

        //Configure the drawer/temp twitter instance
        mDrawerRecyclerView = (RecyclerView) findViewById(R.id.navDrawerRecyclerView);
        mDrawerLayoutManager = new LinearLayoutManager(this);
        mDrawerRecyclerView.setLayoutManager(mDrawerLayoutManager);
        mDrawerRecyclerView.setHasFixedSize(true);
        mDrawerGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
        mDrawerRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = mDrawerRecyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mDrawerGestureDetector.onTouchEvent(e)) {
                    mDrawer.closeDrawers();
                    navigationDrawerItem(mDrawerRecyclerView.getChildPosition(child));
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }
        });

        BuildVars.TWITTER_ACCESS_TOKEN_KEY = sp.getString(BuildVars.SHARED_PREFERENCES_ACCESS_TOKEN_KEY, "");
        BuildVars.TWITTER_ACCESS_TOKEN_SECRET = sp.getString(BuildVars.SHARED_PREFERENCES_ACCESS_TOKEN_SECRET_KEY, "");
        if(sp.getBoolean("loggedIn",false)){
            onCreateUnthreadedWork();
            updateTimeline();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void updateUserValues() {
        try {
            MediaMaidConfigurationBuilder.resetInstance();
            final Twitter twatter = new TwitterFactory(MediaMaidConfigurationBuilder.getInstance().configurationBuilder.build()).getInstance();
            User user = twatter.showUser(twatter.getScreenName());
            BuildVars.TWITTER_USERNAME=user.getScreenName();
            sp.edit().putString(BuildVars.SHARED_PREFERENCES_NAME_KEY, user.getName()).apply();
            sp.edit().putString(BuildVars.SHARED_PREFERENCES_USERNAME_KEY, user.getScreenName()).apply();
            sp.edit().putString(BuildVars.SHARED_PREFERENCES_PROFILE_IMAGE_URL_KEY, user.getOriginalProfileImageURL()).apply();
            //sp.edit().putString(BuildVars.SHARED_PREFERENCES_PROFILE_HEADER_URL_KEY, user.getProfileBannerURL()).apply();
            if (user.getProfileBannerURL().equals("")) {
                //TODO include this with the app itself
                sp.edit().putString(BuildVars.SHARED_PREFERENCES_PROFILE_HEADER_URL_KEY,"http://3.bp.blogspot.com/-LTQN-dQK2pI/VCp__h7_jhI/AAAAAAAACvI/Gs6Kd4i6Bzw/w2560-h1600-p/material_wallpaper_set_two%2B%281%29.jpg").apply();
            } else {
                sp.edit().putString(BuildVars.SHARED_PREFERENCES_PROFILE_HEADER_URL_KEY,user.getProfileBannerURL()).apply();
            }
        } catch (Exception e) {
            Log.e("MediaMaid", "Error updating the user values: \n" + e.toString());
        }
    }

    private void updateDrawer() {
        try {
            mDrawerRecyclerViewAdapter = new DrawerAdapter(
                    drawerItems,
                    drawerIcons,
                    sp.getString(BuildVars.SHARED_PREFERENCES_NAME_KEY, ""),
                    sp.getString(BuildVars.SHARED_PREFERENCES_USERNAME_KEY, ""),
                    twitterPictureCacheHandler.getProfileImageByUser(sp.getString(BuildVars.SHARED_PREFERENCES_USERNAME_KEY, ""), sp.getString(BuildVars.SHARED_PREFERENCES_PROFILE_IMAGE_URL_KEY, "")),
                    twitterPictureCacheHandler.getCoverImageByUser(sp.getString(BuildVars.SHARED_PREFERENCES_USERNAME_KEY, ""), sp.getString(BuildVars.SHARED_PREFERENCES_PROFILE_HEADER_URL_KEY,""))
            );
            mDrawerRecyclerView.setAdapter(mDrawerRecyclerViewAdapter);
        } catch (Exception e) {
            Log.e("MediaMaid", "Error setting up the drawer: \n" + e.toString());
        }
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

    //Handle the clicks on the navigation drawer
    private void navigationDrawerItem(int position) {
        position=position-1;
        if(position==0){
            FragmentManager fragmentManager = getFragmentManager();
            SearchUserDialogFragment searchUserDialogFragment= new SearchUserDialogFragment();
            searchUserDialogFragment.setCancelable(true);
            searchUserDialogFragment.setDialogTitle("Search User");
            searchUserDialogFragment.show(fragmentManager, "Search User");
        }
        if(position==1){
            startActivity(new Intent(this,SettingsActivity.class));
        }
        if(position==2){
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
            List<twitter4j.Status> statuses = timelineHandler.getTimeline(200);


            for (twitter4j.Status status : statuses) {
                if (sp_settings.getBoolean(BuildVars.SHARED_PREFERENCES_SETTINGS_HARD_MODE_KEY, false)
                        && !MediaMaidFilteringHandler.getInstance().checkTweetLanguageIsAppropriate(status.getText(),
                        sp_settings.getString(BuildVars.SHARED_PREFERENCES_SETTINGS_FILTER_LIST_KEY, ""))) {
                    Log.i("MediaMaid","Found an inappropriate tweet and we are in hard mode! Not showing...");
                }else{
                    //Get the censored tweet payload
                    String censoredTweetPayload = MediaMaidFilteringHandler.getInstance().getCensoredTweet(status.getText(),
                            sp_settings.getString(BuildVars.SHARED_PREFERENCES_SETTINGS_FILTER_LIST_KEY, ""));
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

            //Enable streaming if it is enabled in the settings
            if(sp_settings.getBoolean(BuildVars.SHARED_PREFERENCES_SETTINGS_STREAMING_KEY,false)) {
                MediaMaidConfigurationBuilder.resetInstance();
                TwitterStream twitterStream = new TwitterStreamFactory(MediaMaidConfigurationBuilder.getInstance().configurationBuilder.build()).getInstance();
                UserStreamListener userStreamListener = new UserStreamListener() {
                    @Override
                    public void onDeletionNotice(long directMessageId, long userId) {

                    }

                    @Override
                    public void onFriendList(long[] friendIds) {

                    }

                    @Override
                    public void onFavorite(User source, User target, twitter4j.Status favoritedStatus) {

                    }

                    @Override
                    public void onUnfavorite(User source, User target, twitter4j.Status unfavoritedStatus) {

                    }

                    @Override
                    public void onFollow(User source, User followedUser) {

                    }

                    @Override
                    public void onUnfollow(User source, User unfollowedUser) {

                    }

                    @Override
                    public void onDirectMessage(DirectMessage directMessage) {

                    }

                    @Override
                    public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {

                    }

                    @Override
                    public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {

                    }

                    @Override
                    public void onUserListSubscription(User subscriber, User listOwner, UserList list) {

                    }

                    @Override
                    public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {

                    }

                    @Override
                    public void onUserListCreation(User listOwner, UserList list) {

                    }

                    @Override
                    public void onUserListUpdate(User listOwner, UserList list) {

                    }

                    @Override
                    public void onUserListDeletion(User listOwner, UserList list) {

                    }

                    @Override
                    public void onUserProfileUpdate(User updatedUser) {

                    }

                    @Override
                    public void onUserSuspension(long suspendedUser) {

                    }

                    @Override
                    public void onUserDeletion(long deletedUser) {

                    }

                    @Override
                    public void onBlock(User source, User blockedUser) {

                    }

                    @Override
                    public void onUnblock(User source, User unblockedUser) {

                    }

                    @Override
                    public void onStatus(twitter4j.Status status) {
                        if (sp_settings.getBoolean(BuildVars.SHARED_PREFERENCES_SETTINGS_HARD_MODE_KEY, false)
                                && !MediaMaidFilteringHandler.getInstance().checkTweetLanguageIsAppropriate(status.getText(),
                                sp_settings.getString(BuildVars.SHARED_PREFERENCES_SETTINGS_FILTER_LIST_KEY, ""))) {
                            Log.i("MediaMaid", "Found an inappropriate tweet and we are in hard mode! Not showing...");
                        } else {
                            //Censor the tweet
                            String censoredTweetPayload = MediaMaidFilteringHandler.getInstance().getCensoredTweet(status.getText(),
                                    sp_settings.getString(BuildVars.SHARED_PREFERENCES_SETTINGS_FILTER_LIST_KEY, ""));

                            //Create the data object to give to the adapter
                            TwitterTimelineDataObject obj = new TwitterTimelineDataObject(status.getUser().getName(),
                                    status.getUser().getScreenName(),
                                    String.valueOf(status.getId()),
                                    //TODO make the date/time work
                                    "$(date)",
                                    status.isRetweet(),
                                    status.isRetweetedByMe(),
                                    censoredTweetPayload,
                                    twitterPictureCacheHandler.getProfileImageByUser(status.getUser().getScreenName(), status.getUser().getOriginalProfileImageURL()));
                            //Add new tweet to the timeline
                            ((TwitterTimelineViewAdapter) mAdapter).addItem(obj, 0);

                            //Check if the scroll to top setting is on
                            if (sp_settings.getBoolean(BuildVars.SHARED_PREFERENCES_SETTINGS_SCROLL_TO_TOP_KEY, false)) {
                                //Scroll to the top of the timeline to display the tweet automagically
                                mLayoutManager.smoothScrollToPosition(mRecyclerView, null, 0);
                                Log.i("MediaMaid","Scrolling to top!");
                            }

                        }

                    }

                    @Override
                    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

                    }

                    @Override
                    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

                    }

                    @Override
                    public void onScrubGeo(long userId, long upToStatusId) {

                    }

                    @Override
                    public void onStallWarning(StallWarning warning) {

                    }

                    @Override
                    public void onException(Exception ex) {

                    }
                };
                twitterStream.addListener(userStreamListener);
                twitterStream.user();
            }
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
}
