package com.gcme.addischurch.addischurch.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.MainActivity;
import com.gcme.addischurch.addischurch.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.Context.LOCATION_SERVICE;


public class Home_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private FloatingSearchView mSearchView;
    SupportMapFragment sMapFragment;
    FragmentManager manager;
    DatabaseAdaptor DbHelper;
    private String mLastQuery = "";
    private AppBarLayout mAppBar;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    public static GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                        LatLngBounds bounds = new LatLngBounds(
                                new LatLng(8.83900, 38.656596), // top left corner of map
                                new LatLng(9.0879298, 38.920954)); // bottom right corner

                        // Set the camera to the greatest possible zoom level that includes the
                        // bounds
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 13));
                        addMarker();

                    }
                });


                /** This is the button to find the current location and move the camera to current location*/
                FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
                fab.show();
                assert fab != null;
                //on click get Current location
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LocationManager locationManager;
                        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

                        // getting GPS status
                        isGPSEnabled = locationManager
                                .isProviderEnabled(LocationManager.GPS_PROVIDER);

                        // getting network status
                        isNetworkEnabled = locationManager
                                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                        /** check if GPS enabled**/
                        if (isGPSEnabled) {
                            if (isNetworkEnabled) {

                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                mMap.setMyLocationEnabled(true);
                                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                                Location location = mMap.getMyLocation();
                                //LatLng target = main.mylocation;
                                if (location!= null) {
                                    double mylLat = location.getLatitude();
                                    double myLon = location.getLongitude();

                                    LatLng target = new LatLng(mylLat, myLon);
                                    FloatingActionButton fab2 = (FloatingActionButton) getView().findViewById(R.id.route);
                                    fab2.hide();


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
                        } else {
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            showSettingsAlert();
                        }

                    }

                });


                Toast.makeText(getActivity(), "please wait..." , Toast.LENGTH_SHORT).show();

            }
        });

        manager.beginTransaction().add(R.id.map,sMapFragment).commit();




    }



        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);
               mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
            final DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
            mSearchView.attachNavigationDrawerToMenuButton(mDrawerLayout);




            mSearchView.setOnLeftMenuClickListener(
                    new FloatingSearchView.OnLeftMenuClickListener() {
                        @Override
                        public void onMenuOpened() {
                            Toast.makeText(getActivity(), "menu opened" , Toast.LENGTH_SHORT).show();

                            mDrawerLayout.openDrawer(Gravity.LEFT);


                        }

                        @Override
                        public void onMenuClosed() {
                            Toast.makeText(getActivity(), "menu closed" , Toast.LENGTH_SHORT).show();
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                        }
                    });





                        setupSearchBar();
            }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.content_main, container, false);
        mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);



               return view;
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        sMapFragment.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        sMapFragment.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        sMapFragment.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        sMapFragment.onLowMemory();
//    }
//
//    private void initMessage(View v) {
//        display_message = (TextView) v.findViewById(R.id.display_message);
//        display_message.setVisibility(v.GONE);
//
//        progressWheel = (ProgressWheel) v.findViewById(R.id.progress_wheel);
//        progressWheel.setVisibility(v.VISIBLE);
//    }


    private void setupSearchBar() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                Toast.makeText(getActivity(), "this is what you searched" +newQuery, Toast.LENGTH_SHORT).show();
//                DbHelper = new DatabaseAdaptor(getActivity());
//                Cursor cursor = DbHelper.getCategoryListByKeyword(newQuery);
//                ListView activeList = (ListView) getView().findViewById(R.id.activelist);
//                ListView searchList = (ListView) getView().findViewById(R.id.search_suggestion_list);
//
//
//
//                Cursor cu = DbHelper.getAll();
//                if (cu.getCount() != 0) {
//                    String[] title = new String[]{DbHelper.TITLE};
//                    int[] toViewId = new int[]{R.id.titleappointment};
//                    final SimpleCursorAdapter CurAdapter2 = new SimpleCursorAdapter(getActivity(), R.layout.search_item, cu, title, toViewId, 0);
//
//                    /**suggestion List view Load **/
//
//                    ListView listView2 = (ListView) getView().findViewById(R.id.search_suggestion_list);
//                    listView2.setAdapter(CurAdapter2);
//                    CurAdapter2.swapCursor(cursor);
//
//                }
//
//
//                if (newQuery.length() > 0) {
//
//                    activeList.setVisibility(View.INVISIBLE);
//
//                    if (cursor==null){
//
//                        searchList.setVisibility(View.INVISIBLE);
//
//                    }else{
//
//                        searchList.setVisibility(View.VISIBLE);
//                    }
//
//
//                } else {
//                    activeList.setVisibility(View.VISIBLE);
//                    searchList.setVisibility(View.INVISIBLE);
//
//                }
//

            }






        });


        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                mLastQuery = searchSuggestion.getBody();


            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
//                Toast.makeText(getActivity(), "this is what you searched" +query, Toast.LENGTH_SHORT).show();

            }
        });

    }


//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//
//    }





    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                (getActivity()).startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    /**This method pin points the location data from the database to map*/
    void addMarker(){

        DatabaseAdaptor DbHelper;
        DbHelper = new DatabaseAdaptor(getActivity());
        Cursor c = DbHelper.getAll();
        c.moveToFirst();
        if(c.getCount()>0){
            while (!c.isAfterLast()){

                final String cname = c.getString(c.getColumnIndex(DbHelper.NAME));
                final String Longitude = c.getString(c.getColumnIndex(DbHelper.LONGITUDE));
                final String Latitude = c.getString(c.getColumnIndex(DbHelper.LONGITUDE));
                final String cate = c.getString(c.getColumnIndex(DbHelper.CATEGORY));

                if (!Latitude.equals("")&&!Longitude.equals("")) {
                    Double latit = Double.parseDouble(Latitude);
                    Double longi = Double.parseDouble(Longitude);
                    LatLng pos = new LatLng(latit, longi);
                    pinpoint(cname,pos,cate);

                }
                c.moveToNext();

            }
            c.close();

        }else {

            Toast.makeText(getActivity(),"There is no location data " , Toast.LENGTH_LONG).show();

        }
    }
    private void pinpoint(String cname, LatLng pos,String cate) {

            mMap.addMarker(new MarkerOptions().position(pos).title(cname).icon(BitmapDescriptorFactory.fromResource(R.drawable.custom_map_pin)));

    }




    /** This is the search **/



}


