package com.gcme.addischurch.addischurch.Fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.JSON.RequestJson;
import com.gcme.addischurch.addischurch.JSON.SyncService;
import com.gcme.addischurch.addischurch.R;
import com.gcme.addischurch.addischurch.Routing.GMapV2Direction;
import com.gcme.addischurch.addischurch.Routing.GMapV2DirectionAsyncTask;
import com.gcme.addischurch.addischurch.listeners.GPSTracker;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import org.w3c.dom.Document;
import java.util.ArrayList;
import static android.content.Context.LOCATION_SERVICE;


public class Home_fragment extends Fragment implements LocationListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private FloatingSearchView mSearchView;
    SupportMapFragment sMapFragment;
    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;
    FragmentManager manager;
    DatabaseAdaptor DbHelper;
    private String mLastQuery = "";
    private AppBarLayout mAppBar;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    private boolean checkingLatLng = false;
    public static GoogleMap mMap;
    ProgressBar mProgressBar;
    Polyline  polylin;
    private double currentLongitude;
    private double currentLatitude;
    LatLng CurrentLocation;
    Marker mPositionMarker;
    Boolean isMarkerRotating=false;
    FloatingActionButton downbutton;

    AnimationSet animMove;
    int position = 0;
    TextView intelStoryTextView;
    String[] my_string = {"drag up to go to map"};


    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION=11;
    private static final int MY_PERMISSION_ACCESS_COURSE_LOCATION=12;
    private boolean mIsDarkSearchTheme = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        MapLoad();



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







    private void MapLoad() {


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


                /** mylocation button **/

                /** This is the button to find the current location and move the camera to current location*/
                FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.mylocation);
                fab.show();
                assert fab != null;
                //on click get Current location
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        checklocationpermission();
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
                                Toast.makeText(getActivity(), "please wait..." , Toast.LENGTH_SHORT).show();

                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }

                                mMap.setMyLocationEnabled(true);
                                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                              //  Location location = mMap.getMyLocation();
                                mProgressBar.setVisibility(View.VISIBLE);
                                LatLng target =getCurrentLocation();

                                if (target!= null) {

                               // plotemylocation();










//                                    mMap.clear();
//                                    addMarker();
//                                    MarkerOptions mp = new MarkerOptions();
//
//                                    mp.position(target);

                                  //  mp.title("my position");

                                   // mMap.addMarker(mp);
//                                    double mylLat = location.getLatitude();
//                                    double myLon = location.getLongitude();

                                   // LatLng target = new LatLng(mylLat, myLon);
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
                                            mProgressBar.setVisibility(View.GONE);
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
                /** End of mylocation icon*/




                /** Start of Route to location icon*/

                /**This is the route button that gets current location and destnation point**/
                final FloatingActionButton routefab = (FloatingActionButton) getView().findViewById(R.id.route);
                assert routefab != null;
                //on click get Current location
                routefab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        mMap.getUiSettings().setMapToolbarEnabled(false);
                        LocationManager locationManager;
                        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                        // getting GPS status
                        isGPSEnabled = locationManager
                                .isProviderEnabled(LocationManager.GPS_PROVIDER);

                        // getting network status
                        isNetworkEnabled = locationManager
                                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                        /** check if GPS enabled**/
                        if (isGPSEnabled) {
                            if (isNetworkEnabled) {
                                routefab.hide();



                                final FloatingActionButton walk = (FloatingActionButton) getView().findViewById(R.id.walk);
                                walk.show();
                                final FloatingActionButton car = (FloatingActionButton) getView().findViewById(R.id.car);
                                car.show();
                                car.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        walk.hide();

                                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                                        mMap.getUiSettings().setMapToolbarEnabled(false);
                                        checklocationpermission();
                                        LatLng Currentlocation  =getCurrentLocation();
                                       // Location location = mMap.getMyLocation();
                                        if (Currentlocation != null) {
                                           // if (location != null) {
//                                                double latitude = location.getLatitude();
//                                                double longitude = location.getLongitude();


                                                final LatLng myLocation = Currentlocation;

                                                Intent intent = getActivity().getIntent();
                                                String dLon = intent.getStringExtra("Lon");
                                                final String dLat = intent.getStringExtra("Lat");

                                                double dlatitude = Double.parseDouble(dLat);
                                                double dlongitude = Double.parseDouble(dLon);
                                                final LatLng destLocation = new LatLng(dlatitude, dlongitude);

                                                String modedriving = "driving";
                                                route(myLocation, destLocation, modedriving);
                                           // }
                                        }
                                    }
                                });


                                walk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        car.hide();
                                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                                        mMap.getUiSettings().setMapToolbarEnabled(false);
                                        checklocationpermission();
                                        LatLng Currentlocation  =getCurrentLocation();
                                        // Location location = mMap.getMyLocation();
                                        if (Currentlocation != null) {
                                            // if (location != null) {
//                                                double latitude = location.getLatitude();
//                                                double longitude = location.getLongitude();


                                            final LatLng myLocation = Currentlocation;

                                            Intent intent = getActivity().getIntent();
                                            String dLon = intent.getStringExtra("Lon");
                                            final String dLat = intent.getStringExtra("Lat");

                                            double dlatitude = Double.parseDouble(dLat);
                                            double dlongitude = Double.parseDouble(dLon);
                                            final LatLng destLocation = new LatLng(dlatitude, dlongitude);

                                            String modewalking = "walking";
                                            route(myLocation, destLocation, modewalking);
                                        }
                                    }
                                });


                            }

                        }else {
                            showSettingsAlert();

                        }
                    }

                });

                /** End of Route to location icon*/




                /** map Marker clicked*/

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                    @Override
                    public boolean onMarkerClick(final Marker marker) {
                        final FloatingActionButton carb = (FloatingActionButton) getView().findViewById(R.id.car);
                        carb.hide();
                        final FloatingActionButton walkb = (FloatingActionButton) getView().findViewById(R.id.walk);
                        walkb.hide();
                        final FloatingActionButton fab3 = (FloatingActionButton) getView().findViewById(R.id.route);
                        fab3.show();
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        mMap.getUiSettings().setMapToolbarEnabled(false);
                        final double dmarkLat = marker.getPosition().latitude;
                        final double dmarkLong = marker.getPosition().longitude;
                        assert fab3 != null;
                        //on click get Current location
                        fab3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                                mMap.getUiSettings().setMapToolbarEnabled(false);
                                LocationManager locationManager;
                                locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                                // getting GPS status
                                isGPSEnabled = locationManager
                                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                                // getting network status
                                isNetworkEnabled = locationManager
                                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                                /** check if GPS enabled**/
                                if (isGPSEnabled) {
                                    if (isNetworkEnabled) {
                                        fab3.hide();

//                                        mMap.setMyLocationEnabled(true);
//                                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                            return;
//                                        }

                                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                                        mMap.getUiSettings().setMapToolbarEnabled(false);









                                        final FloatingActionButton walk = (FloatingActionButton) getView().findViewById(R.id.walk);
                                        walk.show();
                                        final FloatingActionButton car = (FloatingActionButton) getView().findViewById(R.id.car);
                                        car.show();
                                        car.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                final FloatingActionButton routwalk = (FloatingActionButton) getView().findViewById(R.id.walk);
                                                assert routwalk != null;
                                                routwalk.hide();
                                                Toast.makeText(getActivity(), "Please wait a moment !", Toast.LENGTH_LONG).show();
                                                checklocationpermission();
                                                LatLng Currentlocation  =getCurrentLocation();
                                                if (Currentlocation != null) {
                                                    if (Currentlocation != null) {



                                                        final LatLng myLocation = Currentlocation;


                                                        final LatLng destLocation = new LatLng( dmarkLat, dmarkLong);

                                                        String modedriving = "driving";
                                                        route(myLocation, destLocation, modedriving);
                                                    }
                                                }
                                            }
                                        });


                                        walk.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                final FloatingActionButton routcar = (FloatingActionButton) getView().findViewById(R.id.walk);
                                                assert routcar != null;
                                                routcar.hide();
                                                Toast.makeText(getActivity(), "Please wait a moment !", Toast.LENGTH_LONG).show();


                                                checklocationpermission();
                                                LatLng Currentlocation  =getCurrentLocation();




                                                if (Currentlocation != null) {



                                                    final LatLng myLocation = Currentlocation;


                                                    final LatLng destLocation = new LatLng( dmarkLat, dmarkLong);

                                                    String modewalking = "walking";
                                                    route(myLocation, destLocation, modewalking);
                                                }
                                            }
                                        });


                                    }

                                }else {
                                    showSettingsAlert();

                                }
                            }

                        });
                        return false;
                    }

                });


                /** map Marker clicked finish*/



                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {



                        String ChurchName = marker.getTitle();


                        ChurchDetail secondFrag = new ChurchDetail();
                        Bundle args = new Bundle();

                        args.putString("MarkerName",String.valueOf(ChurchName));

                        secondFrag .setArguments(args);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, secondFrag)
                                .addToBackStack(null)
                                .commit();



                    }
                });




            }


        });

        manager.beginTransaction().add(R.id.map,sMapFragment).commit();

    }

    private void plotemylocation() {

        LatLng currentLocation = getCurrentLocation();

        // Display the current location in the UI
        if (currentLocation != null) {

            if (mPositionMarker == null) {
                mPositionMarker = mMap.addMarker(new MarkerOptions()
                        .position(currentLocation)
                        .title("Eu")
                        .icon(BitmapDescriptorFactory.fromResource((R.drawable.myloc))));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            } else
                mPositionMarker.setPosition(currentLocation);

        }
    }


    @TargetApi(23)
    private void checklocationpermission() {


            if ( Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission( getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission( getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(  new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION   },
                        Home_fragment.MY_PERMISSION_ACCESS_FINE_LOCATION );
                requestPermissions(  new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                        Home_fragment.MY_PERMISSION_ACCESS_COURSE_LOCATION );

            }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.content_main, container, false);
        mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
//        intelStoryTextView = (TextView) view.findViewById(R.id.dragup);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar2);
        mProgressBar.setIndeterminate(true);






        DbHelper = new DatabaseAdaptor(getActivity());
        if(DbHelper.getAll().getCount()==0){



            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.sync);
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Turn On Internet connection and press the sync button !", Toast.LENGTH_LONG).show();


                    getActivity().startService(new Intent(getActivity(), RequestJson.class));

                }
            });

        }else{
            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.sync);
            fab.hide();
        }







        ListView listView = (ListView) view.findViewById(R.id.activelist);
        assert listView != null;
        listView.setOnItemClickListener (new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String rowName = cursor.getString(cursor.getColumnIndex(DbHelper.NAME));
                String rowId = cursor.getString(cursor.getColumnIndex(DbHelper.ID));



                ChurchDetail secondFrag = new ChurchDetail();
                Bundle args = new Bundle();
                args.putString("Key",String.valueOf(rowName));
                args.putString("Keyid",String.valueOf(rowId));

                secondFrag .setArguments(args);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, secondFrag)
                        .addToBackStack(null)
                        .commit();


            }
        });


        DbHelper = new DatabaseAdaptor(getContext());



        Cursor cursor = DbHelper.getAll();
        if (cursor.getCount() != 0) {
            String[] title = new String[]{DbHelper.NAME};
            int[] toViewId = new int[]{R.id.titleappointment};
            SimpleCursorAdapter CurAdapter = new SimpleCursorAdapter(getActivity(), R.layout.child_list_item, cursor, title, toViewId, 0);

            /**suggestion List view Load **/

            listView.setAdapter(CurAdapter);


        } else {
          //  Toast.makeText(getActivity(), "happy halowein!", Toast.LENGTH_LONG).show();

        }


//        mSearchView3.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
//
//            @Override
//            public void onSearchTextChanged(String oldQuery, final String newQuery) {
//
//
//
//            }
//        });

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


/**this is the search list on click*/
        ListView searchlist = (ListView) view.findViewById(R.id.activelist);
        searchlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String rowName = cursor.getString(cursor.getColumnIndex(DbHelper.NAME));
                String rowId = cursor.getString(cursor.getColumnIndex(DbHelper.ID));



                ChurchDetail secondFrag = new ChurchDetail();
                Bundle args = new Bundle();
                args.putString("Key",String.valueOf(rowName));
                args.putString("Keyid",String.valueOf(rowId));

                secondFrag .setArguments(args);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, secondFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });


        return view;
    }


    public LatLng getCurrentLocation() {

        GPSTracker gps = new GPSTracker(getActivity());

        if (gps.canGetLocation()) {
            currentLatitude = gps.getLatitude();
            currentLongitude = gps.getLongitude();
            CurrentLocation = new LatLng(currentLatitude, currentLongitude);


        } else {
            gps.showSettingsAlert();
            checkingLatLng = true;
        }

        return CurrentLocation;

    }


    private void setupSearchBar() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                DbHelper = new DatabaseAdaptor(getActivity());
                Cursor cursor = DbHelper.getCategoryListByKeyword(newQuery);
                ListView activeList = (ListView) getView().findViewById(R.id.activelist);



                Cursor cu = DbHelper.getAll();
                if (cu.getCount() != 0) {
                    String[] title = new String[]{DbHelper.NAME};
                    int[] toViewId = new int[]{R.id.titleappointment};
                    final SimpleCursorAdapter CurAdapter2 = new SimpleCursorAdapter(getActivity(), R.layout.child_list_item, cu, title, toViewId, 0);

                    /**suggestion List view Load **/

                    ListView listView2 = (ListView) getView().findViewById(R.id.activelist);
                    listView2.setAdapter(CurAdapter2);
                    CurAdapter2.swapCursor(cursor);

                }


                if (newQuery.length() > 0) {

                    activeList.setVisibility(View.VISIBLE);

                    if (cursor == null) {

                        activeList.setVisibility(View.INVISIBLE);

                    } else {

                        activeList.setVisibility(View.VISIBLE);
                    }


                } else {
                    activeList.setVisibility(View.INVISIBLE);


                }

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
            }
        });

    }



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



    /** THIS IS TO ROUTE TO THE MARKER LOCATION **/

    protected void route(final LatLng sourcePosition, LatLng destPosition, String mode) {
        final android.os.Handler handler = new android.os.Handler() {


            public void handleMessage(Message msg) {
                try {

                    if (polylin != null) {
                        mMap.clear();
                        checklocationpermission();
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mMap.setMyLocationEnabled(true);
//
//                        MarkerOptions mp = new MarkerOptions();
//
//                        mp.position(sourcePosition);
//
//                        //  mp.title("my position");
//
//                        mMap.addMarker(mp);


                        addMarker();
                    }
                    PolylineOptions rectLine;


                    Document doc = (Document) msg.obj;
                    GMapV2Direction md = new GMapV2Direction();
                    ArrayList<LatLng> directionPoint = md.getDirection(doc);
                    rectLine = new PolylineOptions().width(15).color(getResources().getColor(R.color.wallet_holo_blue_light));
                   // plotemylocation();
                    for (int i = 0; i < directionPoint.size(); i++) {
                        rectLine.add(directionPoint.get(i));
                    }
                    polylin = mMap.addPolyline(rectLine);

                    String time=md.getDurationText(doc);
                    //Toast.makeText(getApplicationContext(),"you will get there in "+time+" minutes", Toast.LENGTH_LONG).show();
                    float zoom = 17;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sourcePosition, zoom));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };

        new GMapV2DirectionAsyncTask(handler, sourcePosition, destPosition, mode).execute();
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
                final String Latitude = c.getString(c.getColumnIndex(DbHelper.LATITUDE));


                if (!Latitude.equals("")&&!Longitude.equals("")) {
                    Double latit = Double.parseDouble(Latitude);
                    Double longi = Double.parseDouble(Longitude);
                    LatLng pos = new LatLng(latit, longi);
                    pinpoint(cname,pos);

                }
                c.moveToNext();

            }
            c.close();

        }else {

            Toast.makeText(getActivity(),"There is no location data " , Toast.LENGTH_LONG).show();

        }
    }



    private void pinpoint(String cname, LatLng pos) {

        mMap.addMarker(new MarkerOptions().position(pos).title(cname).icon(BitmapDescriptorFactory.fromResource(R.mipmap.church_icon)));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSION_ACCESS_FINE_LOCATION:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
//                    Intent intent  = new Intent(MainActivity.this,MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
                }
            }
            case MY_PERMISSION_ACCESS_COURSE_LOCATION:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
//                    Intent intent  = new Intent(MainActivity.this,MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {





//        LatLng currentLocation = getCurrentLocation();
//
//        // Display the current location in the UI
//        if (location != null) {
//
//            if (mPositionMarker == null) {
//                mPositionMarker = mMap.addMarker(new MarkerOptions()
//                        .position(   currentLocation
//                        )
//                        .title("Eu")
//                        .icon(BitmapDescriptorFactory.fromResource((R.drawable.myloc))));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
//            } else
//                mPositionMarker.setPosition( currentLocation);
//        }
//
//
//        rotateMarker(mPositionMarker,location.getBearing());


//        LatLng currentLocation = getCurrentLocation();
//
//        // Display the current location in the UI
//        if (currentLocation != null) {
//
//            if (mPositionMarker == null) {
//                mPositionMarker = mMap.addMarker(new MarkerOptions()
//                        .position(currentLocation)
//                        .title("Eu")
//                        .icon(BitmapDescriptorFactory.fromResource((R.drawable.myloc))));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
//            } else
//                mPositionMarker.setPosition(currentLocation);
//        }



//
//
//        mPositionMarker = mMap.addMarker(new MarkerOptions()
//                .flat(true)
//                .icon(BitmapDescriptorFactory
//                        .fromResource(R.drawable.myloc))
//                .anchor(0.5f, 0.5f)
//
//                .position(new LatLng(location.getLatitude(),location.getLongitude())));
//        LatLng mycurrentLatLng = new LatLng (location.getLatitude(), location.getLongitude());
//
//





//        if (location == null)
//            return;
//
//        if (mPositionMarker == null) {
//
//            mPositionMarker = mMap.addMarker(new MarkerOptions()
//                    .flat(true)
//                    .icon(BitmapDescriptorFactory
//                            .fromResource(R.drawable.custom_map_pin))
//                    .anchor(0.5f, 0.5f)
//                    .position(
//                            new LatLng(location.getLatitude(), location
//                                    .getLongitude())));
//        }
//
//        animateMarker(mPositionMarker, location); // Helper method for smooth
//        // animation
//
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location
//                .getLatitude(), location.getLongitude())));
//




//
//        mMap.clear();
//        addMarker();
//        MarkerOptions mp = new MarkerOptions();
//
//        mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
//
//     //   mp.title("my position");
//
//        mMap.addMarker(mp);
//
////        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
////                new LatLng(location.getLatitude(), location.getLongitude()), 16));

    }

    private double bearingBetweenLocations(LatLng latLng1,LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    private void rotateMarker(final Marker marker, final float toRotation) {
        if(!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 2000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    float bearing =  -rot > 180 ? rot / 2 : rot;

                    marker.setRotation(bearing);

                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }

    public void animateMarker(final Marker marker, final Location location) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final LatLng startLatLng = marker.getPosition();
        final double startRotation = marker.getRotation();
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);

                double lng = t * location.getLongitude() + (1 - t)
                        * startLatLng.longitude;
                double lat = t * location.getLatitude() + (1 - t)
                        * startLatLng.latitude;

                float rotation = (float) (t * location.getBearing() + (1 - t)
                        * startRotation);

                marker.setPosition(new LatLng(lat, lng));
                marker.setRotation(rotation);

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }


//
//    public void showWaitPopup() {
//        new MaterialDialog.Builder(getActivity())
//                .title(R.string.pls_wait)
//                .content(R.string.gps_not_ready)
//                .positiveText(R.string.OK)
//                .show();
//    }

//    @Override
//    public void onLocationChanged(Location location) {
//
//        if (location == null)
//            return;
//
//        if (mPositionMarker == null) {
//
//            mPositionMarker = mMap.addMarker(new MarkerOptions()
//                    .flat(true)
//                    .icon(BitmapDescriptorFactory
//                            .fromResource(R.drawable.positionIndicator))
//                    .anchor(0.5f, 0.5f)
//                    .position(
//                            new LatLng(location.getLatitude(), location
//                                    .getLongitude())));
//        }
//
//        animateMarker(mPositionMarker, location); // Helper method for smooth
//        // animation
//
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location
//                .getLatitude(), location.getLongitude())));
//
//    }
//
//    public void animateMarker(final Marker marker, final Location location) {
//        final Handler handler = new Handler();
//        final long start = SystemClock.uptimeMillis();
//        final LatLng startLatLng = marker.getPosition();
//        final double startRotation = marker.getRotation();
//        final long duration = 500;
//
//        final Interpolator interpolator = new LinearInterpolator();
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                long elapsed = SystemClock.uptimeMillis() - start;
//                float t = interpolator.getInterpolation((float) elapsed
//                        / duration);
//
//                double lng = t * location.getLongitude() + (1 - t)
//                        * startLatLng.longitude;
//                double lat = t * location.getLatitude() + (1 - t)
//                        * startLatLng.latitude;
//
//                float rotation = (float) (t * location.getBearing() + (1 - t)
//                        * startRotation);
//
//                marker.setPosition(new LatLng(lat, lng));
//                marker.setRotation(rotation);
//
//                if (t < 1.0) {
//                    // Post again 16ms later.
//                    handler.postDelayed(this, 16);
//                }
//            }
//        });
//    }
//



}


