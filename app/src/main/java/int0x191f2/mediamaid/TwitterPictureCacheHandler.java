package int0x191f2.mediamaid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    public Bitmap getCoverImageByUser(String name, String url){
        String covername = "cover_"+name;
        if(isInCache(covername)) {
            try {
                FileInputStream fis = context.openFileInput(covername);
                Bitmap bm = BitmapFactory.decodeStream(fis);
                Log.i("MediaMaid","Read Bitmap from cache as '" + covername + "'");
                return bm;
            } catch (IOException e) {
                //will never occur because it checks the cache first however this is required
                Log.e("MediaMaid", e.toString());
                return null;
            }
        }else{
            Bitmap bm = getImageFromURL(url);
            writeToCache(covername, bm);
            return bm;
        }
    }

    private boolean isInCache(String name){
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

    private void writeToCache(String name, Bitmap bm){
        try {
            FileOutputStream fos = context.openFileOutput(name, Context.MODE_PRIVATE);
            FileOutputStream cacheList = context.openFileOutput("cacheList.txt", Context.MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(cacheList);
            outputStreamWriter.write(name + "\n");
            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Log.i("MediaMaid","Wrote Bitmap to cache as '" + name + "'");
            if(fos!=null){
                fos.close();
            }
            outputStreamWriter.close();
        }catch(IOException e){
            Log.e("MediaMaid",e.toString());
        }
    }

    private Bitmap getImageFromURL(String url){
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

    private List<String> getCacheFilesList(){
        List<String> lines = new ArrayList<String>();
        try{
            InputStream inputStream = context.openFileInput("cacheList.txt");
            if(inputStream!=null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line="";
                while((line=bufferedReader.readLine())!=null){
                    lines.add(line);
                }
                inputStream.close();
            }
        }catch (FileNotFoundException e){
            Log.e("MediaMaid","File not found: " + e.toString());
        }catch (IOException e){
            Log.e("MediaMaid","Can not read file" + e.toString());
        }
        return lines;
    }

    public void cleanCache(){
        try{
            List<String> cacheList = getCacheFilesList();
            for(String s : cacheList) {
                context.deleteFile(s);
            }
        }catch (Exception e){
            Log.e("MediaMaid",e.toString());
        }

    }
}
