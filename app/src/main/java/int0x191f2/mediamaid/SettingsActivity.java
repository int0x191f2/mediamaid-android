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
import android.widget.EditText;
import android.widget.Toast;

import twitter4j.auth.AccessToken;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity{
    Button filterButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        filterButton = (Button) findViewById(R.id.settingsModifyFilterButton);
        Toolbar tb = (Toolbar) findViewById(R.id.settingsToolbar);
        tb.setTitleTextColor(0xFFFFFFFF);
        if(tb!=null){
            setSupportActionBar(tb);
        }
        getSupportActionBar().setTitle("Settings");
        //Enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set button listener
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FilterListDialogFragment filterListDialogFragment = new FilterListDialogFragment();
                filterListDialogFragment.setCancelable(true);
                filterListDialogFragment.setDialogTitle("Filter List");
                filterListDialogFragment.show(fragmentManager,"Filter List");
            }
        });
    }
}
