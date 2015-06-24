package int0x191f2.mediamaid;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar; 
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class ComposeActivity extends AppCompatActivity {
    private TwitterTweet twitterTweeter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        twitterTweeter = new TwitterTweet(getApplicationContext());
        Toolbar tb = (Toolbar) findViewById(R.id.composeToolbar);
        tb.setTitleTextColor(0xFFFFFFFF);
        if(tb!=null){
            setSupportActionBar(tb);
        }
        getSupportActionBar().setTitle("Compose Message");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.sendMessage) {
            Boolean didSend = twitterTweeter.Send(((EditText)(findViewById(R.id.composeMessageInput))).getText().toString());
            if(!(((EditText)(findViewById(R.id.composeMessageInput))).getText().toString().isEmpty())) {
                if (didSend) {
                    finish();
                    Toast.makeText(getApplicationContext(), "Tweet sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error sending tweet", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Input a message",Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
