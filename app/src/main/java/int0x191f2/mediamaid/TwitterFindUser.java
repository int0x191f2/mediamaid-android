package int0x191f2.mediamaid;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class TwitterFindUser extends AppCompatActivity {
    Button submit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_find_user_layout);
        submit = (Button) findViewById(R.id.userIDSubmit);
        //Set button listener
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FilterListDialogFragment filterListDialogFragment = new FilterListDialogFragment();
                filterListDialogFragment.setCancelable(true);
                filterListDialogFragment.setDialogTitle("Search User");
                filterListDialogFragment.show(fragmentManager, "Search User");
            }
        });
    }
}
