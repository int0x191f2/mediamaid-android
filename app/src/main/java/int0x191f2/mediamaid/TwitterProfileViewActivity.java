package int0x191f2.mediamaid;

import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import int0x191f2.mediamaid.TwitterProfileViewViewAdapter.ProfileViewClickListener;
import twitter4j.TwitterFactory;
import twitter4j.Twitter;

import android.util.Log;


public class TwitterProfileViewActivity extends ActionBarActivity {
    private String userHandle, userName;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CardView mCardView;
    private Twitter twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_profile_view);

        MediaMaidConfigurationBuilder.resetInstance();
        twitter = new TwitterFactory(MediaMaidConfigurationBuilder.getInstance().configurationBuilder.build()).getInstance();

        String userID = getIntent().getStringExtra("id");
        Long id = Long.valueOf(userID).longValue();
        try {
            Status st = twitter.showStatus(id);
            userHandle = st.getUser().getScreenName();
            userName = st.getUser().getName();
        }catch(Exception e){
            Log.e("MediaMaid","Exception when getting user handle:"+e.toString());
        }

        //Set the toolbar title and color
        Toolbar tb = (Toolbar) findViewById(R.id.profileViewToolbar);
        tb.setTitleTextColor(0xFFFFFFFF);
        if(tb!=null){
            setSupportActionBar(tb);
        }
        getSupportActionBar().setTitle(userName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.twitterProfileViewRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        updateTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twitter_profile_view, menu);
        return true;
    }

    public void updateTimeline(){
        new GetDataSet().execute(new ArrayList<TwitterProfileViewDataObject>());
    }

    private class GetDataSet extends AsyncTask<ArrayList<TwitterProfileViewDataObject>,Integer,ArrayList<TwitterProfileViewDataObject>> {
        TwitterTimelineHandler timelineHandler = new TwitterTimelineHandler(getApplicationContext());
        TwitterPictureCacheHandler twitterPictureCacheHandler = new TwitterPictureCacheHandler(getApplicationContext());
        @Override
        protected ArrayList<TwitterProfileViewDataObject> doInBackground(ArrayList<TwitterProfileViewDataObject>... params) {
            int index=0;
            ArrayList results = new ArrayList<TwitterTimelineDataObject>();
            List<twitter4j.Status> statuses = timelineHandler.getUserTimeline(20,userHandle);

            for (twitter4j.Status status : statuses) {
                TwitterProfileViewDataObject obj = new TwitterProfileViewDataObject(status.getUser().getName(),
                        status.getUser().getScreenName(),
                        String.valueOf(status.getId()),
                        //TODO make the date/time work
                        "$(date)",
                        status.isRetweet(),
                        status.isRetweetedByMe(),
                        status.getText(),
                        twitterPictureCacheHandler.getProfileImageByUser(status.getUser().getScreenName(), status.getUser().getOriginalProfileImageURL()));
                results.add(index, obj);
                index++;
            }
            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<TwitterProfileViewDataObject> twitterProfileViewDataObjects) {
            super.onPostExecute(twitterProfileViewDataObjects);
            mAdapter = new TwitterProfileViewViewAdapter(twitterProfileViewDataObjects);
            mRecyclerView.setAdapter(mAdapter);
            ((TwitterProfileViewViewAdapter) mAdapter).setItemOnClickListener(new
                                                                                      TwitterProfileViewViewAdapter.ProfileViewClickListener() {
                                                                                          @Override
                                                                                          public void onItemClick(int pos, View v) {
                                                                                              Log.i("MediaMaid","INDEX"+pos);
                                                                                          }
                                                                                      });
        }
    }
}
