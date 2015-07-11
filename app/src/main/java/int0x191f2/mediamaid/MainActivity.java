package int0x191f2.mediamaid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;
import android.util.Log;

import twitter4j.*;


public class MainActivity extends AppCompatActivity {
    public String[] drawerItems = {"Login"};
    private static SharedPreferences sp;
    private int0x191f2.mediamaid.TwitterAuth twitterAuth;
    private com.melnykov.fab.FloatingActionButton fab;
    private ListView drawerListView;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private TwitterTimelineHandler timelineHandler;
    private ConnectionDetector connectionDetector;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public void composeDialog(View view) {
        startActivity(new Intent(this, ComposeActivity.class));
    }

    public void updateTimeline(){
        new getDataSet().execute(new ArrayList<TwitterTimelineDataObject>());
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
        //Create the Twitter Authenticator
        twitterAuth = new int0x191f2.mediamaid.TwitterAuth(getApplicationContext(),BuildVars.TWITTER_CONSUMER_KEY,BuildVars.TWITTER_CONSUMER_SECRET);
        //Create the timelineHandler
        timelineHandler = new TwitterTimelineHandler(getApplicationContext());
        //Set the toolbar title and color
        Toolbar tb = (Toolbar) findViewById(R.id.mainToolbar);
        tb.setTitleTextColor(0xFFFFFFFF);
        if(tb!=null){
            setSupportActionBar(tb);
        }
        getSupportActionBar().setTitle("MediaMaid");

        //Set up the recyclerview and fab integration
        fab = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.fab);
        mRecyclerView = (RecyclerView) findViewById(R.id.timeline_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        fab.attachToRecyclerView(mRecyclerView);
        //Pass a new array list; when onStart calls it updates the timeline.
        mAdapter = new TwitterTimelineViewAdapter(new ArrayList<TwitterTimelineDataObject>());
        mRecyclerView.setAdapter(mAdapter);
        //Disable back button in top level of application
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawer,tb,R.string.drawer_open, R.string.drawer_close);
        mToggle.setDrawerIndicatorEnabled(true);
        mDrawer.setDrawerListener(mToggle);
        sp = getApplicationContext().getSharedPreferences("MediaMaid",0);

        //Check that the application hasn't run before settings loggedIn to false
        if(!sp.getBoolean("hasRun",false)){
            Log.i("MediaMaid","Hasn't run yet");
            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("loggedIn",false);
            e.putBoolean("hasRun",true);
            e.commit();
        }

        if(!connectionDetector.isConnectingToInternet()){
            Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        //Set up the inital drawer
        updateDrawer();

        //Get the timeline if the user is logged in
        updateTimeline();
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
        ((TwitterTimelineViewAdapter) mAdapter).setItemOnClickListener(new
            TwitterTimelineViewAdapter.MyClickListener(){
            @Override
            public void onItemClick(int pos, View v){
                new TwitterTimelineClickHandler(getApplicationContext()).onItemClick(pos);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            this.startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_refresh) {
            updateTimeline();
        }

        return super.onOptionsItemSelected(item);
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
                    twitterAuth.logout();
                    //Keep the login/logout consistent
                    updateDrawer();
                    //Clear timeline on logout
                    //getTimeline();
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
    private class getDataSet extends AsyncTask<ArrayList<TwitterTimelineDataObject>,Integer,ArrayList<TwitterTimelineDataObject>>{
        @Override
        protected ArrayList<TwitterTimelineDataObject> doInBackground(ArrayList<TwitterTimelineDataObject>... params) {
            int index=0;
            ArrayList results = new ArrayList<TwitterTimelineDataObject>();
            List<twitter4j.Status> statuses = timelineHandler.getTimeline();
            for (twitter4j.Status status : statuses) {
                TwitterTimelineDataObject obj = new TwitterTimelineDataObject(status.getUser().getName(),"@"+status.getUser().getScreenName(),status.getText());
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
        }
    }
}
