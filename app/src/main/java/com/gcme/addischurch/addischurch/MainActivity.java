package com.gcme.addischurch.addischurch;


import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Toast;

import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.Event.MainEvent;
import com.gcme.addischurch.addischurch.FileManager.FileManager;
import com.gcme.addischurch.addischurch.Fragments.AllChurches;
import com.gcme.addischurch.addischurch.Fragments.Home_fragment;
import com.gcme.addischurch.addischurch.Fragments.MapFragment;
import com.gcme.addischurch.addischurch.Fragments.MyChurch;
import com.gcme.addischurch.addischurch.Fragments.MyFav;
import com.gcme.addischurch.addischurch.JSON.RequestJson;
import com.gcme.addischurch.addischurch.JSON.SyncService;
import com.gcme.addischurch.addischurch.utilities.Utils;

import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_PHONE_STATE = 11;
    private static final int JOB_ID = 100;
    private static final int JOB_ID2 = 101;
    private JobScheduler myJobScheduler;
    private Home_fragment home_frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addHome();
        whenlaunch();
        permission();
        initUtils();
        if(savedInstanceState==null){

            addHome();
        }else{

        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    private void addHome() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new MapFragment());
        ft.commit();
    }

    private void whenlaunch() {
        DatabaseAdaptor DbHelper;
        DbHelper = new DatabaseAdaptor(this);


        Intent intent = new Intent(this, RequestJson.class);
        startService(intent);
        myJobScheduler  = JobScheduler.getInstance(this);
        JobConstr();


//
//
        DbHelper.InsertChurch( "5", "Bole MKC", "bole","0913609212" , "bolemkc.com","Sunday 3-5" ,"MKC","38.828731","8.991639","","http://www.delraypsychotherapist.com/wp-content/uploads/2011/08/Porn2-300x177.jpg");
        DbHelper.InsertChurch( "2", "yeka MKC", "bole","0913609212" , "yekamkc.com", "Sunday 3-5" ,"MKC","1","38.785429","8.999935","http://www.delraypsychotherapist.com/wp-content/uploads/2011/08/Porn2-300x177.jpg");
        DbHelper.InsertChurch( "3", "mexico MKC", "bole","0913609212" , "mexicomkc.com","Sunday 3-5" ,"MKC","9.026339","38.817945","","http://www.delraypsychotherapist.com/wp-content/uploads/2011/08/Porn2-300x177.jpg");
        DbHelper.InsertChurch( "4", "bezainternational", "bole","+251 11 663 9213" , "bezainternational.org","Sunday 3-5" ,"bezainternational","9.0094697","38.8033294","","http://www.delraypsychotherapist.com/wp-content/uploads/2011/08/Porn2-300x177.jpg");
        DbHelper.InsertChurch( "6", "try", "bole","+251 11 663 9213" , "bezainternational.org","Sunday 3-5" ,"bezainternational","38.8033294","9.0094697","","http://www.delraypsychotherapist.com/wp-content/uploads/2011/08/Porn2-300x177.jpg");
        DbHelper.InsertChurch( "1", "samplet", "bole","0913609212" , "bolemkc.com","Sunday 3-5" ,"MKC","38.828731","8.991639","","http://www.delraypsychotherapist.com/wp-content/uploads/2011/08/Porn2-300x177.jpg");

//
//
//        DbHelper.InsertChurchSchedule( "6", "5","sunday","10:40","youth");
//        DbHelper.InsertChurchSchedule( "7", "5","fri","10:40","healing");
//        DbHelper.InsertChurchSchedule( "8", "5","wed","10:40","main");

        DbHelper.InsertChurchdenomination( "1", "Muluwengel","","");
        DbHelper.InsertChurchdenomination( "2", "Yougo","","");
        DbHelper.InsertChurchdenomination( "3", "kalehiwot","","");
        DbHelper.InsertChurchdenomination( "4", "MKC","http://bulbulamkc.org/kkk.jpg","");
        DbHelper.InsertChurchdenomination( "5", "bezainternational","","");
    }
    public void JobConstr(){

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this,SyncService.class));
        builder.setPeriodic(100000);
        builder.setBackoffCriteria(500,1);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        myJobScheduler.schedule(builder.build());


        JobInfo.Builder builder2 = new JobInfo.Builder(JOB_ID2, new ComponentName(this, RequestJson.class));
        builder2.setPeriodic(100000);
        builder2.setBackoffCriteria(500,1);
        builder2.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        myJobScheduler.schedule(builder2.build());
    }
//    @Override
//    public void onBackPressed() {
//
//
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.add(R.id.fragment_container, new Home_fragment());
//        ft.addToBackStack("tag_back");
//        ft.commit();
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new MapFragment());
            ft.addToBackStack("tag_back");
            ft.commit();
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(MainActivity.this, MainEvent.class));

//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.fragment_container, new Events_fragment());
//            ft.addToBackStack("tag_back_Events");
//            ft.commit();

        } else if (id == R.id.nav_slideshow) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new AllChurches());
            ft.addToBackStack("tag_back_allchurches");
            ft.commit();

//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.fragment_container, new Churches_fragment());
//            ft.addToBackStack("tag_back_Events");
//            ft.commit();
        } else if (id == R.id.nav_manage) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new MyChurch());
            ft.addToBackStack("tag_back_Mychurch");
            ft.commit();


        } else if (id == R.id.nav_share) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new MyFav());
            ft.addToBackStack("tag_back_myfav");
            ft.commit();




        } else if (id == R.id.About) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new MapFragment());
            ft.addToBackStack("tag_back_myfav");
            ft.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initUtils() {
        new Utils(this);
    }



    private void permission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.INTERNET}, REQUEST_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PHONE_STATE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
        }
    }



}
