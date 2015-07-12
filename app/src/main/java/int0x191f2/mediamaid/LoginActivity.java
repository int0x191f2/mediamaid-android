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
