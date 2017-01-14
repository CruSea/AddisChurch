package com.gcme.addischurch.addischurch.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.gcme.addischurch.addischurch.Adapters.ScheduleAdapter;
import com.gcme.addischurch.addischurch.Adapters.UIUtils;
import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.FileManager.FileManager;
import com.gcme.addischurch.addischurch.Model.Schedules;
import com.gcme.addischurch.addischurch.R;
import com.gcme.addischurch.addischurch.Testimony.EventHandler;
import com.gcme.addischurch.addischurch.Testimony.NetworkEventController;
import com.gcme.addischurch.addischurch.Testimony.RecyclerEventAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kzone on 11/28/2016.
 */

public class MyChurch extends Fragment {
    DatabaseAdaptor DbHelper;
    public static GoogleMap mMap;
    SupportMapFragment sMapFragment;
    public static final String ORIENTATION = "orientation";
    FragmentManager manager;
    ListView  scheduleList;
    private RecyclerView mRecyclerView;
    private boolean mHorizontal;
    ProgressDialog pd;
    Toolbar toolbar;
    ArrayList<String> aa = new ArrayList<String>();
    ArrayList<String> num= new ArrayList<String>();
    TextView contactsview,webview,sermonview;
    String SelectedSearchitem,Selectedmarkname;
    ImageView BannerImage;
    RequestQueue queue;
    String url = "https://raw.githubusercontent.com/mobilesiri/Android-Custom-Listview-Using-Volley/master/richman.json";
    RecyclerView recyclerView;
    List<EventHandler> feedsList = new ArrayList<EventHandler>();
    RecyclerEventAdapter adapter;
    public MyChurch() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_my_church, container, false);
/**recieve the church name**/



        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewevent_home);
        scheduleList= (ListView) view.findViewById(R.id.ScheduleList_home);
        contactsview = (TextView) view.findViewById(R.id.phoneno_home);
        webview = (TextView) view.findViewById(R.id.web_home);
        BannerImage= (ImageView) view.findViewById(R.id.headerimage_home);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main);



        DbHelper = new DatabaseAdaptor(getActivity());
        Cursor cursor = DbHelper.gethome();
        if (cursor.getCount() ==0) {
            checkhome();
        }else {
            cursor.moveToFirst();
            String churchid=cursor.getString(cursor.getColumnIndex(DbHelper.Home_Church));
            FillContents(churchid);
            getschedule(churchid);
            FillEvent();
        }



        return view;
    }

    private void FillEvent() {

        adapter = new RecyclerEventAdapter(getActivity(), feedsList);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        recyclerView.setAdapter(adapter);

        queue = NetworkEventController.getInstance(getActivity()).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        JsonArrayRequest newsReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject obj = response.getJSONObject(i);
                        EventHandler feeds = new EventHandler(obj.getString("name"), obj.getString("image"),obj.getString("source"));

                        // adding movie to movies array
                        feedsList.add(feeds);

                    } catch (Exception e) {
                        System.out.println(e.getMessage());

                    } finally {
                        //Notify adapter about data changes
                        adapter.notifyItemChanged(i);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
        //Adding JsonArrayRequest to Request Queue
        queue.add(newsReq);
    }

    private void checkhome() {



            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setCancelable(false);
            // Setting Dialog Title
            alertDialog.setTitle("Wede Church");

            // Setting Dialog Message
            alertDialog.setMessage("You did not selected home church yet!");

            // On pressing Settings button
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {

                    AllChurches secondFrag = new AllChurches();
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, secondFrag)
                            .addToBackStack("tag_back_AllChurches")
                            .commit();
                }
            });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Home_fragment secondFrag = new Home_fragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, secondFrag)
                        .addToBackStack("tag_back_Home_fragment")
                        .commit();
            }
        });


            // Showing Alert Message
            alertDialog.show();




    }



    private void FillContents(String churchId) {





        DbHelper = new DatabaseAdaptor(getActivity());
        Cursor cursor = DbHelper.getMarkerDataRowByID(churchId);
        if (cursor != null) {

            FileManager FM;
            FM=new FileManager(getActivity());
            String imagename=cursor.getString(cursor.getColumnIndex(DbHelper.ID))+".jpg";
            String ImageLocationid = FM.getFileAt("images", imagename).getAbsolutePath();
            if(ImageLocationid!=null) {
                File file = new File(ImageLocationid);
                BannerImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }
            String contacts = cursor.getString(cursor.getColumnIndex(DbHelper.CONTACTS));
            String web = cursor.getString(cursor.getColumnIndex(DbHelper.WEB));
            String churchname = cursor.getString(cursor.getColumnIndex(DbHelper.NAME));
            contactsview.setText(contacts);

            webview.setText(web);
            toolbar.setTitle(churchname);


            final String Longitude = cursor.getString(cursor.getColumnIndex(DbHelper.LONGITUDE));
            final String Latitude = cursor.getString(cursor.getColumnIndex(DbHelper.LATITUDE));






            sMapFragment = SupportMapFragment.newInstance();
            manager=getFragmentManager();
            sMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    mMap.setBuildingsEnabled(true);
                    mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback(){
                        @Override
                        public void onMapLoaded() {

//                            LatLngBounds bounds = new LatLngBounds(
//                                    new LatLng(8.83900, 38.656596), // top left corner of map
//                                    new LatLng(9.0879298, 38.920954)); // bottom right corner
//
//                            // Set the camera to the greatest possible zoom level that includes the
//                            // bounds
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 13));




                            if (Longitude!=null&&Latitude!=null) {
                                double mylLat = Double.parseDouble(Latitude);
                                double myLon = Double.parseDouble(Longitude);

                                LatLng target = new LatLng(mylLat, myLon);

                                final CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(target)      // Sets the center of the map to Mountain View
                                        .zoom(17)                   // Sets the zoom
                                        .bearing(90)                // Sets the orientation of the camera to east
                                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                        .build();                   //


                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                                    @Override
                                    public boolean onMyLocationButtonClick() {
                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                        return true;
                                    }
                                });

                            }

                        }
                    });


                    addMarker(Latitude,Longitude);

                }
            });

            manager.beginTransaction().add(R.id.mapdetail_home,sMapFragment).commit();



            /**This button pass the category list longitude ,altitude and name to the main activity**/

        } else {

            Toast.makeText(getActivity(), "There is no data by this Location!", Toast.LENGTH_LONG).show();
        }
    }


    /** populates the schedule list from the database**/
    private void getschedule(String selecteditemid) {

        Cursor cursor=DbHelper.getScheduleDataRowById(selecteditemid);

        if(cursor.getCount()>0 && cursor.moveToFirst()) {
            List<Schedules> schedules = new ArrayList<Schedules>();


            for (int i = 0; i < cursor.getCount(); i++) {

                cursor.moveToPosition(i);



                // do {
                Schedules schedules1 = new Schedules();

                schedules1.setCatagory(cursor.getString(cursor.getColumnIndex(DbHelper.ScheduleCategory)));
                schedules1.setDate(cursor.getString(cursor.getColumnIndex(DbHelper.SheduleDate)));
                schedules1.setTime(cursor.getString(cursor.getColumnIndex(DbHelper.ScheduleTime)));

                schedules.add(schedules1);
            }


            ScheduleAdapter scheduleAdapter = new ScheduleAdapter(schedules, getActivity());
            scheduleList.setAdapter(scheduleAdapter);
            UIUtils.setListViewHeightBasedOnItems(scheduleList);
        }else {
            Toast.makeText(getActivity(), "There is no schedule!", Toast.LENGTH_LONG).show();

        }


    }


    /**this is to plote on map**/
    void addMarker(String Lat,String Long){


        if (!Long.equals("")&&!Lat.equals("")) {
            Double latit = Double.parseDouble(Lat);
            Double longi = Double.parseDouble(Long);
            LatLng pos = new LatLng(latit, longi);
            pinpoint(SelectedSearchitem,pos);

        }

    }
    private void pinpoint(String cname, LatLng pos) {

        mMap.addMarker(new MarkerOptions().position(pos).title(cname).icon(BitmapDescriptorFactory.fromResource(R.drawable.custom_map_pin)));

    }













}
