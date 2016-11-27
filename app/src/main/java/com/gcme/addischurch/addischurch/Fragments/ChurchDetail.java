package com.gcme.addischurch.addischurch.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.R;
import com.gcme.addischurch.addischurch.Testimony.App;
import com.gcme.addischurch.addischurch.Testimony.Snap;
import com.gcme.addischurch.addischurch.Testimony.SnapAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class ChurchDetail extends Fragment {
    DatabaseAdaptor DbHelper;
    public static GoogleMap mMap;
    SupportMapFragment sMapFragment;
    public static final String ORIENTATION = "orientation";
    FragmentManager manager;

    private RecyclerView mRecyclerView;
    private boolean mHorizontal;
    ProgressDialog pd;
    ArrayList<String> aa = new ArrayList<String>();
    ArrayList<String> num= new ArrayList<String>();
    TextView contactsview,webview,sermonview;
    String SelectedSearchitem;

    public ChurchDetail() {
        // Required empty public constructor
    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
                View view= inflater.inflate(R.layout.fragment_church_detail, container, false);
/**recieve the church name**/
                if(getArguments().getString("Key")!=null) {
                    SelectedSearchitem = getArguments().getString("Key");
                    String Selecteditemid = getArguments().getString("Keyid");
                    Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
                    toolbar.inflateMenu(R.menu.main);

                    toolbar.setTitle(SelectedSearchitem);


                    contactsview = (TextView) view.findViewById(R.id.phoneno);
                    webview = (TextView) view.findViewById(R.id.web);
                    sermonview = (TextView) view.findViewById(R.id.sermon);



                   FillContents(Selecteditemid);





                }






                mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
                //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        toolbar.setOnMenuItemClickListener(this);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setHasFixedSize(true);

                if (savedInstanceState == null) {
                    mHorizontal = true;
                } else {
                    mHorizontal = savedInstanceState.getBoolean(ORIENTATION);
                }

                setupAdapter();

                return view;
            }

    private void FillContents(String churchId) {





        DbHelper = new DatabaseAdaptor(getActivity());
        Cursor cursor = DbHelper.getMarkerDataRowByID(churchId);
        if (cursor != null) {



            String contacts = cursor.getString(cursor.getColumnIndex(DbHelper.CONTACTS));
            String web = cursor.getString(cursor.getColumnIndex(DbHelper.WEB));
            String sermon = cursor.getString(cursor.getColumnIndex(DbHelper.SERMONS));
            contactsview.setText(contacts);

            webview.setText(web);

            sermonview.setText(sermon);


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


                    addMarker(Longitude,Latitude);

                }
            });

            manager.beginTransaction().add(R.id.mapdetail,sMapFragment).commit();



            /**This button pass the category list longitude ,altitude and name to the main activity**/

        } else {

            Toast.makeText(getActivity(), "There is no data by this Location!", Toast.LENGTH_LONG).show();
        }
    }




    /**this is to plote on map**/
    void addMarker(String Long,String Lat){


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











    private void setupAdapter() {
        List<App> apps = getApps();

        SnapAdapter snapAdapter = new SnapAdapter();
        if (mHorizontal) {
            snapAdapter.addSnap(new Snap(Gravity.CENTER_HORIZONTAL, "Snap center", apps));
            // snapAdapter.addSnap(new Snap(Gravity.START, "Snap start", apps));
            // snapAdapter.addSnap(new Snap(Gravity.END, "Snap end", apps));
        }
//        else {
//            snapAdapter.addSnap(new Snap(Gravity.CENTER_VERTICAL, "Snap center", apps));
//            snapAdapter.addSnap(new Snap(Gravity.TOP, "Snap top", apps));
//            snapAdapter.addSnap(new Snap(Gravity.BOTTOM, "Snap bottom", apps));
//        }

        mRecyclerView.setAdapter(snapAdapter);
    }
    private List<App> getApps() {
        List<App> apps = new ArrayList<>();
        apps.add(new App("Google+", R.drawable.ic_google_48dp, 4.6f));
        apps.add(new App("Google+", R.drawable.ic_google_48dp, 4.6f));
        apps.add(new App("Google+", R.drawable.ic_google_48dp, 4.6f));
        apps.add(new App("Google+", R.drawable.ic_google_48dp, 4.6f));
        apps.add(new App("Google+", R.drawable.ic_google_48dp, 4.6f));
        apps.add(new App("Google+", R.drawable.ic_google_48dp, 4.6f));
        return apps;
    }

}
