package com.gcme.addischurch.addischurch;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.widget.Toast;

import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.FileManager.FileManager;
import com.gcme.addischurch.addischurch.JSON.SyncService;

import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

/**
 * Created by kzone on 11/19/2016.
 */

public class AddisChurch extends Application {
    /**This Class starts everytime our application opens**/
    private static final int JOB_ID = 100;
    private JobScheduler myJobScheduler;
    private FileManager FM;
    //
    @Override
    public void onCreate() {

        super.onCreate();
        DatabaseAdaptor DbHelper;
        DbHelper = new DatabaseAdaptor(this);



        FM=new FileManager(this);
        Intent intent = new Intent(this, SyncService.class);
        startService(intent);
        myJobScheduler  = JobScheduler.getInstance(this);
        Toast.makeText(getApplicationContext(),"updating data!", Toast.LENGTH_LONG).show();
        JobConstr();




         DbHelper.InsertChurch( "1", "Bole MKC", "bole","0913609212" , "Sunday 3-5" ,"MKC","","","","http://www.delraypsychotherapist.com/wp-content/uploads/2011/08/Porn2-300x177.jpg");
        DbHelper.InsertChurch( "2", "yeka MKC", "bole","0913609212" , "Sunday 3-5" ,"MKC","","","","http://www.delraypsychotherapist.com/wp-content/uploads/2011/08/Porn2-300x177.jpg");
        DbHelper.InsertChurch( "3", "mexico MKC", "bole","0913609212" , "Sunday 3-5" ,"MKC","","","","http://www.delraypsychotherapist.com/wp-content/uploads/2011/08/Porn2-300x177.jpg");


        DbHelper.InsertChurchdenomination( "1", "Muluwongel");
        DbHelper.InsertChurchdenomination( "2", "Yougo");
        DbHelper.InsertChurchdenomination( "3", "kalehiwot");
        DbHelper.InsertChurchdenomination( "4", "MKC");


    }

    public void JobConstr(){

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this,SyncService.class));
        builder.setPeriodic(100000);
        builder.setBackoffCriteria(500,1);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        myJobScheduler.schedule(builder.build());
    }
}