package int0x191f2.mediamaid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Adam on 7/13/2015.
 */
public class TwitterPictureCacheHandler {
    Context context;
    public TwitterPictureCacheHandler(Context c){
        this.context = c;
    }

    public Bitmap getProfileImageByUser(String name, String url){
        if(isInCache(name)) {
            try {
                FileInputStream fis = context.openFileInput(name);
                Bitmap bm = BitmapFactory.decodeStream(fis);
                Log.i("MediaMaid","Read Bitmap from cache as '" + name + "'");
                return bm;
            } catch (IOException e) {
                //will never occur because it checks the cache first however this is required
                Log.e("MediaMaid", e.toString());
                return null;
            }
        }else{
            Bitmap bm = getImageFromURL(url);
            writeToCache(name, bm);
            return bm;
        }
    }
    public boolean isInCache(String name){
        try{
            FileInputStream fis = context.openFileInput(name);
            if(fis.available()!=0){
                return true;
            }else{
                return false;
            }
        }catch(IOException e){
            Log.e("MediaMaid","File not found: " + name);
            return false;
        }
    }
    public void writeToCache(String name, Bitmap bm){
        try {
            FileOutputStream fos = context.openFileOutput(name, Context.MODE_PRIVATE);
            bm.compress(Bitmap.CompressFormat.PNG,100,fos);
            Log.e("MediaMaid","Wrote Bitmap to cache as '" + name + "'");
            if(fos!=null){
                fos.close();
            }
        }catch(IOException e){
            Log.e("MediaMaid",e.toString());
        }
    }
    public Bitmap getImageFromURL(String url){
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setDoInput(true);
            connection.connect();
            Log.i("MediaMaid","Got image from url: " + url);
            return BitmapFactory.decodeStream(connection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
