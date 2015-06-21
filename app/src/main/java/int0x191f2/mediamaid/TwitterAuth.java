package int0x191f2.mediamaid;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.*;
import org.apache.http.impl.client.DefaultHttpClient;

import twitter4j.*;

/**
 * Created by Adam on 6/18/2015.
 */
public class TwitterAuth {
    org.apache.http.client.HttpClient client = new DefaultHttpClient();
    public TwitterAuth(String username, String password) {
        String URL = "https://api.twitter.com/oauth/request_token?oauth_callback=''";
    }
    public TwitterAuth(){

    }
}
