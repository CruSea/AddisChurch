package com.gcme.addischurch.addischurch.Fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.gcme.addischurch.addischurch.Adapters.ScheduleAdapter;
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

public class ChurchDetail extends Fragment {
    DatabaseAdaptor DbHelper;
    public static GoogleMap mMap;
    SupportMapFragment sMapFragment;
    public static final String ORIENTATION = "orientation";
    FragmentManager manager;

    private RecyclerView mRecyclerView;
    ImageButton favred, favwhite, churchred, churchwhite;
    private boolean mHorizontal;
    ProgressDialog pd;
    ArrayList<String> aa = new ArrayList<String>();
    ArrayList<String> num= new ArrayList<String>();
    TextView contactsview,webview,sermonview;
    String SelectedSearchitem,Selectedmarkname;
    ImageView BannerImage;
    RequestQueue queue;
    String url = "https://raw.githubusercontent.com/mobilesiri/Android-Custom-Listview-Using-Volley/master/richman.json";
    RecyclerView recyclerView;
    ListView  scheduleList;
    List<EventHandler> feedsList = new ArrayList<EventHandler>();
    RecyclerEventAdapter adapter;
    public ChurchDetail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_church_detail, container, false);
/**recieve the church name**/

        contactsview = (TextView) view.findViewById(R.id.phoneno);
        webview = (TextView) view.findViewById(R.id.web);

        BannerImage= (ImageView) view.findViewById(R.id.headerimage);
        scheduleList= (ListView) view.findViewById(R.id.ScheduleList);
        churchwhite = (ImageButton) view.findViewById(R.id.favhomechurch1);
        churchred = (ImageButton) view.findViewById(R.id.favhomechurch2);
        favred = (ImageButton) view.findViewById(R.id.favred);
        favwhite = (ImageButton) view.findViewById(R.id.favwhite);
        favwhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   Toast.makeText(getActivity(), "white clicked", Toast.LENGTH_SHORT).show();
                if(getArguments().getString("Key")!=null) {
                    String Selecteditemid = getArguments().getString("Keyid");
                    DbHelper.Insertfav(Selecteditemid);
                    Toast.makeText(getActivity(), "fav inserted", Toast.LENGTH_SHORT).show();

                }

                favwhite.setVisibility(View.GONE);
                favred.setVisibility(View.VISIBLE);
            }
        });
        favred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getArguments().getString("Key")!=null) {
                    String Selecteditemid = getArguments().getString("Keyid");

                    DbHelper.deletefavData(Selecteditemid);
                    Toast.makeText(getActivity(), "fav removed", Toast.LENGTH_SHORT).show();

                }

                favwhite.setVisibility(View.VISIBLE);
                favred.setVisibility(View.GONE);
            }
        });
        churchwhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(getArguments().getString("Keyid")!=null) {
                    SelectedSearchitem = getArguments().getString("Keyid");
                    DbHelper.InsertHome(SelectedSearchitem);
                }




                churchwhite.setVisibility(View.GONE);
                churchred.setVisibility(View.VISIBLE);
            }
        });
        churchred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getArguments().getString("Keyid" )!=null) {
                    SelectedSearchitem = getArguments().getString("Keyid");
                    DbHelper.changehome();
                    DbHelper.InsertHome(SelectedSearchitem);
                }


                //   Toast.makeText(getActivity(), "white clicked", Toast.LENGTH_SHORT).show();

                churchred.setVisibility(View.GONE);
                churchwhite.setVisibility(View.VISIBLE);
            }
        });


        if(getArguments().getString("Key")!=null) {
            SelectedSearchitem = getArguments().getString("Key");
            String Selecteditemid = getArguments().getString("Keyid");
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            toolbar.inflateMenu(R.menu.main);

            toolbar.setTitle(SelectedSearchitem);



            FillContents(Selecteditemid);
            getschedule(Selecteditemid);




        }


        if(getArguments().getString("MarkerName")!=null) {
            Selectedmarkname = getArguments().getString("MarkerName");


            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            toolbar.inflateMenu(R.menu.main);

            toolbar.setTitle(Selectedmarkname);



            fillbyname(Selectedmarkname);





//             DbHelper = new DatabaseAdaptor(getActivity());
//
//             Cursor cursor = DbHelper.getMarkerDataRowByname(Selectedmarkname);
//             if (cursor != null) {
//                 final String Longitude = cursor.getString(cursor.getColumnIndex(DbHelper.LONGITUDE));
//                 final String Latitude = cursor.getString(cursor.getColumnIndex(DbHelper.LATITUDE));


            FloatingActionButton getdirection = (FloatingActionButton) view.findViewById(R.id.detailgetdirection);
            getdirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Home_fragment secondFrag = new Home_fragment();
                    Bundle args = new Bundle();
//                         args.putString("Longitude",Longitude);
//                         args.putString("Latitude",Latitude);

                    secondFrag.setArguments(args);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, secondFrag)
                            .addToBackStack(null)
                            .commit();


                }
            });


        }





        //}













        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewevent);
        adapter = new RecyclerEventAdapter(getActivity(), feedsList);
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
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






        return view;
    }

    private void getschedule(String selecteditemid) {

//        Cursor cursor=DbHelper.getScheduleDataRowById(selecteditemid);
        Cursor cursor=DbHelper.getScheduleDataRowById(selecteditemid);

        if(cursor.getCount()>0 && cursor.moveToFirst()) {
            List<Schedules> schedules = new ArrayList<Schedules>();


            for (int i = 0; i < cursor.getCount(); i++) {
               //cursor.move(i);
                cursor.moveToPosition(i);



                // do {
                    Schedules schedules1 = new Schedules();

                    schedules1.setCatagory(cursor.getString(cursor.getColumnIndex(DbHelper.ScheduleCategory)));
                    schedules1.setDate(cursor.getString(cursor.getColumnIndex(DbHelper.SheduleDate)));
                    schedules1.setTime(cursor.getString(cursor.getColumnIndex(DbHelper.ScheduleTime)));

                    schedules.add(schedules1);
                }
               //while (cursor.moveToNext());

//                do{
//
//                    String str=cursor.getString(cursor.getColumnIndex(DbHelper.ScheduleCategory));
//
//
//                    //  Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
//                    String str2=cursor.getString(cursor.getColumnIndex(DbHelper.SheduleDate));
//
//                    String str3=cursor.getString(cursor.getColumnIndex(DbHelper.ScheduleTime));
//                    schedules1.setCatagory(str);
//                    schedules1.setDate(str2);
//                    schedules1.setTime(str3);
//
//                }while();


//                schedules1.setCatagory(cursor.getString(cursor.getColumnIndex(DbHelper.ScheduleCategory)));



            //}

            ScheduleAdapter scheduleAdapter = new ScheduleAdapter(schedules, getActivity());
            scheduleList.setAdapter(scheduleAdapter);
        }else {
            Toast.makeText(getActivity(), "There is no schedule!", Toast.LENGTH_LONG).show();

        }


        }



    private void fillbyname(String selectedmarkname) {


        DbHelper = new DatabaseAdaptor(getActivity());
        Cursor cursor = DbHelper.getMarkerDataRowByname(selectedmarkname);
        if (cursor != null) {



            String contacts = cursor.getString(cursor.getColumnIndex(DbHelper.CONTACTS));
            String web = cursor.getString(cursor.getColumnIndex(DbHelper.WEB));
            contactsview.setText(contacts);

            webview.setText(web);




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

            manager.beginTransaction().add(R.id.mapdetail,sMapFragment).commit();



            /**This button pass the category list longitude ,altitude and name to the main activity**/

        } else {

            Toast.makeText(getActivity(), "There is no data by this Location!", Toast.LENGTH_LONG).show();
        }




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
            contactsview.setText(contacts);

            webview.setText(web);




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

            manager.beginTransaction().add(R.id.mapdetail,sMapFragment).commit();



            /**This button pass the category list longitude ,altitude and name to the main activity**/

        } else {

            Toast.makeText(getActivity(), "There is no data by this Location!", Toast.LENGTH_LONG).show();
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
