package int0x191f2.mediamaid;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import twitter4j.auth.AccessToken;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity{
    ListView listView;
    List<String> itemList;
    TwitterPictureCacheHandler twitterPictureCacheHandler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        twitterPictureCacheHandler=new TwitterPictureCacheHandler(getApplicationContext());
        listView = (ListView) findViewById(R.id.settingsListView);
        Toolbar tb = (Toolbar) findViewById(R.id.settingsToolbar);
        tb.setTitleTextColor(0xFFFFFFFF);
        if(tb!=null){
            setSupportActionBar(tb);
        }
        getSupportActionBar().setTitle("Settings");
        //Enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Add things to the listview
        itemList = new ArrayList<String>();
        itemList.add("Edit the filter list");
        itemList.add("Clear the cache");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getBaseContext(),R.layout.settings_listview,itemList);
        listView.setAdapter(adp);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListViewClick(position);
            }
        });
    }
    private void onListViewClick(int position){
        if(position==0){
            FragmentManager fragmentManager = getFragmentManager();
            FilterListDialogFragment filterListDialogFragment = new FilterListDialogFragment();
            filterListDialogFragment.setCancelable(true);
            filterListDialogFragment.setDialogTitle("Filter List");
            filterListDialogFragment.show(fragmentManager,"Filter List");
        }
        if(position==1){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    twitterPictureCacheHandler.cleanCache();
                }
            }).start();
            Toast.makeText(getApplicationContext(),"Clearing the cache",Toast.LENGTH_SHORT).show();
        }
    }
}
