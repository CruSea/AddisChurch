package com.gcme.addischurch.addischurch.JSON;


import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.FileManager.FileDownloader;
import com.gcme.addischurch.addischurch.FileManager.FileManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**This Class synchronize database with the server MYSQL Database**/
public class SyncService extends JobService {
    String JSON_STRING;
   //MainActivity mainobject=new MainActivity();
    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    DatabaseAdaptor DbHelper;
    FileDownloader FD;
    public SyncService(){




    }
    @Override
    public boolean onStartJob(JobParameters params) {
        DbHelper = new DatabaseAdaptor(this);
        Cursor cursor = DbHelper.getAll();
        if(cursor.getCount()!=0) {

            downloadchurchImage();
            cursor.close();
        }
        cursor.close();

        Cursor cu = DbHelper.getAlldenominations();
        if(cu.getCount()!=0) {
            downloaddenominationimages();
            cu.close();
        }

        cu.close();
            jobFinished(params, false);
            return false;
    }







    private void downloaddenominationimages() {

        Cursor cu = DbHelper.getAlldenominations();


        cu.moveToFirst();
        if (cu.getCount() > 0) {
            while (!cu.isAfterLast()) {
                FileManager FM;
                FM=new FileManager(this);
                String ImageUrl= cu.getString(cu.getColumnIndex(DbHelper.CAT_IMG_URL));
                //set image name as cat id
                String ImageNamefromCatagory=cu.getString(cu.getColumnIndex(DbHelper.IDCAT));

                String imagename=(ImageNamefromCatagory+".jpg");
                String ImageLocationbyid = FM.getFileAt("denominations", imagename).getAbsolutePath();
                File file = new File(ImageLocationbyid);

                if (file.exists()) {

                    //             Toast.makeText(getApplicationContext(), "Image "+ImageNamefromId+".jpg"+" already exists", Toast.LENGTH_LONG).show();
                }
                else{

                    FD=new FileDownloader(getBaseContext(),ImageUrl,"denominations",ImageNamefromCatagory+".jpg");
                    FD.execute();

                }
                cu.moveToNext();
            }
            cu.close();

        }

    }

    private void downloadchurchImage() {
        Cursor cursor = DbHelper.getAll();
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            while (!cursor.isAfterLast()) {
                FileManager FM;
                FM=new FileManager(this);
                String ImageUrl= cursor.getString(cursor.getColumnIndex("_ImageUrl"));
                //set image name as news id
                String ImageNamefromId=cursor.getString(cursor.getColumnIndex("_id"));

                String imagename=(ImageNamefromId+".jpg");
                String ImageLocationbyid = FM.getFileAt("images", imagename).getAbsolutePath();
                File file = new File(ImageLocationbyid);

//                Cursor cu = DbHelper.getID(ImageNamefromId);
                if (file.exists()) {

                    //             Toast.makeText(getApplicationContext(), "Image "+ImageNamefromId+".jpg"+" already exists", Toast.LENGTH_LONG).show();
                }
                else{

                    FD=new FileDownloader(getBaseContext(),ImageUrl,"images",ImageNamefromId+".jpg");
                    FD.execute();
                    //                  Toast.makeText(getApplicationContext(), "Image DOWNLOADED", Toast.LENGTH_LONG).show();
                    //                 mainobject.Slider();

                }
                cursor.moveToNext();
            }
            cursor.close();

        }

    }


    @Override
    public boolean onStopJob(JobParameters params) {

        return false;
    }


}
