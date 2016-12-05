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
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.R;
import com.gcme.addischurch.addischurch.Routing.GMapV2Direction;
import com.gcme.addischurch.addischurch.Routing.GMapV2DirectionAsyncTask;
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

public class Home_fragment extends Fragment {
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
    public static GoogleMap mMap;
    ProgressBar mProgressBar;
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
                                mProgressBar.setVisibility(View.VISIBLE);
                                mMap.setMyLocationEnabled(true);
                                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                                Location location = mMap.getMyLocation();

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

                                    mProgressBar.setVisibility(View.GONE);
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

                                mMap.setMyLocationEnabled(true);
                                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
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
                                        Location location = mMap.getMyLocation();
                                        if (location != null) {
                                            if (location != null) {
                                                double latitude = location.getLatitude();
                                                double longitude = location.getLongitude();


                                                final LatLng myLocation = new LatLng(latitude, longitude);

                                                Intent intent = getActivity().getIntent();
                                                String dLon = intent.getStringExtra("Lon");
                                                final String dLat = intent.getStringExtra("Lat");

                                                double dlatitude = Double.parseDouble(dLat);
                                                double dlongitude = Double.parseDouble(dLon);
                                                final LatLng destLocation = new LatLng(dlatitude, dlongitude);

                                                String modedriving = "driving";
                                                route(myLocation, destLocation, modedriving);
                                            }
                                        }
                                    }
                                });


                                walk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        car.hide();
                                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                                        mMap.getUiSettings().setMapToolbarEnabled(false);
                                        Location location = mMap.getMyLocation();

                                        if (location != null) {
                                            double latitude = location.getLatitude();
                                            double longitude = location.getLongitude();


                                            final LatLng myLocation = new LatLng(latitude, longitude);

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

                                        mMap.setMyLocationEnabled(true);
                                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                            return;
                                        }

                                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                                        mMap.getUiSettings().setMapToolbarEnabled(false);
                                        final Location location = mMap.getMyLocation();

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

                                                if (location != null) {
                                                    if (location != null) {
                                                        double latitude = location.getLatitude();
                                                        double longitude = location.getLongitude();


                                                        final LatLng myLocation = new LatLng(latitude, longitude);


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


                                                if (location != null) {
                                                    double latitude = location.getLatitude();
                                                    double longitude = location.getLongitude();


                                                    final LatLng myLocation = new LatLng(latitude, longitude);


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


            }


        });

        manager.beginTransaction().add(R.id.map,sMapFragment).commit();

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.content_main, container, false);
        mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setIndeterminate(true);








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
            Toast.makeText(getActivity(), "happy halowein!", Toast.LENGTH_LONG).show();

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
//                Toast.makeText(getActivity(), "this is what you searched" +query, Toast.LENGTH_SHORT).show();

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

                    Document doc = (Document) msg.obj;
                    GMapV2Direction md = new GMapV2Direction();
                    ArrayList<LatLng> directionPoint = md.getDirection(doc);
                    PolylineOptions rectLine = new PolylineOptions().width(15).color(getResources().getColor(R.color.wallet_holo_blue_light));

                    for (int i = 0; i < directionPoint.size(); i++) {
                        rectLine.add(directionPoint.get(i));
                    }
                    Polyline polylin = mMap.addPolyline(rectLine);
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

        mMap.addMarker(new MarkerOptions().position(pos).title(cname).icon(BitmapDescriptorFactory.fromResource(R.drawable.custom_map_pin)));

    }



}


