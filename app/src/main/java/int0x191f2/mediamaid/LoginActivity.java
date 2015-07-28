package int0x191f2.mediamaid;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import android.view.View;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import twitter4j.Twitter;

public class LoginActivity extends AppCompatActivity {
    TwitterAuth twitterAuth;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = getApplicationContext().getSharedPreferences("MediaMaid",0);
        twitterAuth = TwitterAuth.getInstance();

        Toolbar tb = (Toolbar) findViewById(R.id.loginActivityToolbar);
        tb.setTitleTextColor(0xFFFFFFFF);
        if(tb!=null){
            setSupportActionBar(tb);
        }
        getSupportActionBar().setTitle("Login");
    }
    public void twitterLogin(View v) {
        CircularProgressView circularProgressView = (CircularProgressView) findViewById(R.id.loginCircleProgressView);
        circularProgressView.startAnimation();
        twitterAuth.generateOAuthRequestToken();
        this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAuth.getOAuthRequestToken().getAuthorizationURL())));
        finish();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
}
