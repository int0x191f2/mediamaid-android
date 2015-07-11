package int0x191f2.mediamaid;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ip4gjb on 7/11/15.
 */
public class TwitterTimelineClickHandler {
    private Context context;
    public TwitterTimelineClickHandler(Context c) { context=c; }
    public void onItemClick(int pos){
        Toast.makeText(context,"Clicked: "+pos,Toast.LENGTH_SHORT).show();
        return;
    }
}
