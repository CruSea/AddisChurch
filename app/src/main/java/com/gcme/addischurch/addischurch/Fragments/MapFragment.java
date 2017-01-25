package com.gcme.addischurch.addischurch.Fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.gcme.addischurch.addischurch.Adapters.FavoriteAdapter;
import com.gcme.addischurch.addischurch.Adapters.SearchAdapter;
import com.gcme.addischurch.addischurch.Adapters.UIUtils;
import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.FileManager.FileManager;
import com.gcme.addischurch.addischurch.JSON.RequestJson;
import com.gcme.addischurch.addischurch.Model.PItemDatalocal;
import com.gcme.addischurch.addischurch.Model.favorites;
import com.gcme.addischurch.addischurch.Model.search;
import com.gcme.addischurch.addischurch.Routing.GMapV2Direction;
import com.gcme.addischurch.addischurch.Routing.GMapV2DirectionAsyncTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gcme.addischurch.addischurch.R;
import com.google.android.gms.maps.model.Polyline;
import com.gcme.addischurch.addischurch.listeners.GPSTracker;
import com.gcme.addischurch.addischurch.Config;

import com.gcme.addischurch.addischurch.Adapters.MapPopupAdapter;
import com.gcme.addischurch.addischurch.Model.PItemData;
import com.gcme.addischurch.addischurch.utilities.*;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.rey.material.widget.Slider;

import org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Panacea-Soft on 7/15/15.
 * Contact Email : teamps.is.cool@gmail.com
 */

public class MapFragment extends Fragment {

    /**
     * ------------------------------------------------------------------------------------------------
     * Start Block - Private Variables
     * *------------------------------------------------------------------------------------------------
     */
    private FloatingSearchView mSearchView;
    SupportMapFragment sMapFragment;
    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;
    FragmentManager manager;
    private String mLastQuery = "";
    private AppBarLayout mAppBar;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    ProgressBar mProgressBar;
    Polyline polylin;

    LatLng CurrentLocation;
    Marker mPositionMarker;
    Boolean isMarkerRotating = false;
    FloatingActionButton downbutton;
    LocationManager locationManager;
    public static Context context;

    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION=11;
    private static final int MY_PERMISSION_ACCESS_COURSE_LOCATION=12;
    private boolean mIsDarkSearchTheme = false;


    FileManager FM;
    String chid;
    String chcat_id;
    String chname;
    String chdescription;
    String chaddress;
    String chphone;
    String chemail;
    String chlat;
    String chlng;
    String chsearch_tag;
    String ImageLocationid;
    String imagename;

    Double latit;
    Double longi;
    LatLng pos;


    DatabaseAdaptor DbHelper;
    public static GoogleMap mMap;
    private GoogleMap googleMap;
    private Marker customMarker;
    private LatLng markerLatLng;
    private ProgressWheel progressWheel;
    private ArrayList<PItemDatalocal> items;
    private TextView display_message;
    private RequestQueue queue;
    private boolean checkingLatLng = false;
    private HashMap<String, Uri> images = new HashMap<String, Uri>();

    private HashMap<String, String> markerImages = new HashMap<String, String>();
    private HashMap<Marker, PItemData> markerInfo = new HashMap<Marker, PItemData>();
    private HashMap<String, String> markerAddress = new HashMap<String, String>();
    private HashMap<String, String> churchid = new HashMap<String, String>();

    private SharedPreferences pref;
    private int selectedCityId;
    private int selectedSubCatId;
    private double selectedRegionLat;
    private double selectedRegionLng;
    private double currentLongitude;
    private double currentLatitude;
    View marker;
    MaterialDialog dialog;
    MapView mMapView;
    private String jsonStatusSuccessString;
    private SpannableString connectionErrorString;
    TextView addressTextView;
    FloatingActionButton routefab ;
    FloatingActionButton walk;
    FloatingActionButton car;
    ListView activeList;
    FloatingActionButton locationSearchFAB;
    /**------------------------------------------------------------------------------------------------
     * End Block - Private Variables
     **------------------------------------------------------------------------------------------------*/


    /**
     * ------------------------------------------------------------------------------------------------
     * Start Block - Override Functions
     * *------------------------------------------------------------------------------------------------
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) v.findViewById(R.id.mapView1);
        addressTextView = (TextView) v.findViewById(R.id.complete_address);
        routefab = (FloatingActionButton) v.findViewById(R.id.route1);
        walk = (FloatingActionButton) v.findViewById(R.id.walk1);
        car = (FloatingActionButton) v.findViewById(R.id.car1);
        activeList = (ListView) v.findViewById(R.id.activelist1);
        locationSearchFAB = (FloatingActionButton) v.findViewById(R.id.mylocation1);

        context=getActivity();

        initData(v);

        initUI(v, inflater, container, savedInstanceState);

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mMapView.onDestroy();
//    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /**------------------------------------------------------------------------------------------------
     * End Block - Override Functions
     **------------------------------------------------------------------------------------------------*/

    /**
     * ------------------------------------------------------------------------------------------------
     * Start Block - INit data Functions
     * *------------------------------------------------------------------------------------------------
     */
    private void initData(View v) {

        DbHelper = new DatabaseAdaptor(getActivity());
        if(DbHelper.getAll().getCount()==0){



            FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.sync1);
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Turn On Internet connection and press the sync button !", Toast.LENGTH_LONG).show();


                    getActivity().startService(new Intent(getActivity(), RequestJson.class));

                }
            });

        }else{
            FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.sync1);
            fab.hide();
        }

    }

    /**------------------------------------------------------------------------------------------------
     * End Block - Init Data Functions
     **------------------------------------------------------------------------------------------------*/

    /**
     * ------------------------------------------------------------------------------------------------
     * Start Block - Init UI Functions
     * *------------------------------------------------------------------------------------------------
     */
    private void initUI(View v, LayoutInflater inflater, ViewGroup container,
                        Bundle savedInstanceState) {
        if (Utils.isGooglePlayServicesOK(getActivity())) {
            Utils.psLog("Google Play Service is ready for Google Map");
            initFAB(v);
            initSearch(v);
            initMessage(v);
            loadPreferenceData();


            loadMap(savedInstanceState, inflater, container);

            queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        } else {
            showNoServicePopup();
        }
    }

    private void initFABroute() {

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


                routefab.hide();
                routefab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isGPSEnabled) {
                            if (isNetworkEnabled) {
                                walk.show();
                                car.show();
                            car.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                walk.hide();
                                progressWheel.setVisibility(View.VISIBLE);
                                getRouteCurrentLocation();

                            }
                        });


                        walk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                car.hide();
                                progressWheel.setVisibility(View.VISIBLE);
                                getRoutewalking();
                            }
                        });


                            }else {
                                Toast.makeText(getActivity(), "No Network", Toast.LENGTH_LONG);
                            }

                        } else {
                            showSettingsAlert();

                        }


                    }




                });


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
                        requestDataFromLocal();
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
                    progressWheel.setVisibility(View.GONE);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sourcePosition, zoom));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };

        new GMapV2DirectionAsyncTask(handler, sourcePosition, destPosition, mode).execute();
    }


    private void initSearch(View v) {

        mSearchView = (FloatingSearchView) v.findViewById(R.id.floating_search_view11);
        final DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        mSearchView.attachNavigationDrawerToMenuButton(mDrawerLayout);

        //getallchurchlist();
        DbHelper = new DatabaseAdaptor(getActivity());



        assert activeList != null;
//        activeList.setOnItemClickListener (new AdapterView.OnItemClickListener() {
//
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
//                String rowName = cursor.getString(cursor.getColumnIndex(DbHelper.NAME));
//                String rowId = cursor.getString(cursor.getColumnIndex(DbHelper.ID));
//
//
//
//                ChurchDetail secondFrag = new ChurchDetail();
//                Bundle args = new Bundle();
//                args.putString("Key",String.valueOf(rowName));
//                args.putString("Keyid",String.valueOf(rowId));
//
//                secondFrag .setArguments(args);
//                getFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.fragment_container, secondFrag)
//                        .addToBackStack("tag_back_mapfrag")
//                        .commit();
//
//
//            }
//        });



        mSearchView.setOnLeftMenuClickListener(
                new FloatingSearchView.OnLeftMenuClickListener() {
                    @Override
                    public void onMenuOpened() {

                        mDrawerLayout.openDrawer(Gravity.LEFT);


                    }

                    @Override
                    public void onMenuClosed() {

                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }
                });





        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                DbHelper = new DatabaseAdaptor(getActivity());
                Cursor cu = DbHelper.getCategoryListByKeyword(newQuery);






//                Cursor cu = DbHelper.getAll();


                if(cu.getCount()>0 && cu.moveToFirst()) {
                    List<search> searchList = new ArrayList<search>();


                    for (int i = 0; i < cu.getCount(); i++) {
                        search search1 = new search();
                        cu.moveToPosition(i);
                        search1.setchName(cu.getString(cu.getColumnIndex(DbHelper.NAME)));
                        search1.setDeno(cu.getString(cu.getColumnIndex(DbHelper.CHURCH_LOCATION)));
                        search1.setId(cu.getInt(cu.getColumnIndex(DbHelper.ID)));
                        searchList.add(search1);


                    }

                    cu.close();

                    SearchAdapter searchAdapter = new SearchAdapter(searchList, context,MapFragment.this);
                    activeList.setAdapter(searchAdapter);
                    UIUtils.setListViewHeightBasedOnItems(activeList);
                    activeList.setTextFilterEnabled(true);
//                    searchAdapter.swapCursor(cursor);


                }






//                Cursor cu = DbHelper.getAll();
//                if (cu.getCount() != 0) {
//                    String[] title = new String[]{DbHelper.NAME};
//                    int[] toViewId = new int[]{R.id.titleappointment};
//                    final SimpleCursorAdapter CurAdapter2 = new SimpleCursorAdapter(getActivity(), R.layout.child_list_item, cu, title, toViewId, 0);
//
//                    /**suggestion List view Load **/
//
//
//                    activeList.setAdapter(CurAdapter2);
//                    CurAdapter2.swapCursor(cursor);
//
//                }


                if (newQuery.length() > 0) {
                    locationSearchFAB.setVisibility(View.INVISIBLE);
                    activeList.setVisibility(View.VISIBLE);


                    if (cu == null) {

                        activeList.setVisibility(View.INVISIBLE);

                    } else {

                        activeList.setVisibility(View.VISIBLE);
                    }


                } else {
                    activeList.setVisibility(View.INVISIBLE);
                    locationSearchFAB.setVisibility(View.VISIBLE);

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
//
//    private void getallchurchlist() {
//
//            Cursor cu = DbHelper.getAll();
//
//
//            if(cu.getCount()>0 && cu.moveToFirst()) {
//                List<search> searchList = new ArrayList<search>();
//
//
//                for (int i = 0; i < cu.getCount(); i++) {
//                    search search1 = new search();
//                    cu.moveToPosition(i);
//                    search1.setchName(cu.getString(cu.getColumnIndex(DbHelper.NAME)));
//                    search1.setDeno(cu.getString(cu.getColumnIndex(DbHelper.CHURCH_LOCATION)));
//                    search1.setId(cu.getInt(cu.getColumnIndex(DbHelper.ID)));
//                    searchList.add(search1);
//
//
//                }
//
//                cu.close();
//
//                SearchAdapter searchAdapter = new SearchAdapter(searchList, context);
//                activeList.setAdapter(searchAdapter);
//                UIUtils.setListViewHeightBasedOnItems(activeList);
//
//
//
//            }else {
//
//
//            }
//    }

    private void initFAB(View v) {
        DbHelper = new DatabaseAdaptor(getActivity());
        locationSearchFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkingLatLng) {
                    if (readyLatLng()) {
                        progressWheel.setVisibility(View.VISIBLE);
                        getcurrentlocation(view);

                    } else {
                        showWaitPopup();
                    }
                } else {

                    getcurrentlocation(view);

                }
            }
        });

    }

    private void initMessage(View v) {
//        display_message = (TextView) v.findViewById(R.id.display_message);
//        display_message.setVisibility(v.GONE);

        progressWheel = (ProgressWheel) v.findViewById(R.id.progress_wheel);
        progressWheel.setVisibility(v.VISIBLE);
    }

    private void loadMap( Bundle savedInstanceState, LayoutInflater inflater, ViewGroup container) {

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                mMap = googleMap;
                mMap.setBuildingsEnabled(true);
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        LatLngBounds bounds = new LatLngBounds(
                                new LatLng(8.83900, 38.656596), // top left corner of map
                                new LatLng(9.0879298, 38.920954)); // bottom right corner

                        // Set the camera to the greatest possible zoom level that includes the
                        // bounds
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 13));
                        requestDataFromLocal();
                        initFABroute();

                    }
                });
            }
        });

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        marker = inflater.inflate(R.layout.custom_marker, container, false);

//        requestData(Config.APP_API_URL + Config.ITEMS_BY_SUB_CATEGORY + selectedCityId + "/sub_cat_id/" + selectedSubCatId + "/item/all/", marker);

    }

    private void loadPreferenceData() {
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        try {
            selectedCityId = pref.getInt("_selected_city_id", 0);
            selectedSubCatId = pref.getInt("_selected_sub_cat_id", 0);

            selectedRegionLat = Double.parseDouble(pref.getString("_city_region_lat", ""));
            selectedRegionLng = Double.parseDouble(pref.getString("_city_region_lng", ""));
        } catch (NumberFormatException e) {
            // EditText EtPotential does not contain a valid double
        }
    }

    /**------------------------------------------------------------------------------------------------
     * End Block - INit UI Functions
     **------------------------------------------------------------------------------------------------*/

    /**
     * ------------------------------------------------------------------------------------------------
     * Start Block - Private Functions
     * *------------------------------------------------------------------------------------------------
     */

    private void setupSearchBar(View v) {

    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("Current Address ( ");
                Utils.psLog("Getting Address.");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    if (i != returnedAddress.getMaxAddressLineIndex() - 1) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    } else {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("");
                    }
                }
                strReturnedAddress.append(" )");
                strAdd = strReturnedAddress.toString();
                Utils.psLog("My loction address --- " + "" + strReturnedAddress.toString());
            } else {
                Utils.psLog("My Current loction address" + "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.psLog("My Current loction address >>" + e.getMessage());
        }
        return strAdd;
    }


    private void requestDataFromLocal() {

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                mMap = googleMap;

                mMap.setBuildingsEnabled(true);
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {


                        Cursor c = DbHelper.getAll();
                        if (c.getCount() > 0 && c.moveToFirst()) {
                            for (int i = 0; i < c.getCount(); i++) {
                                c.moveToPosition(i);


                                chid = c.getString(c.getColumnIndex(DbHelper.ID));
                                chcat_id = c.getString(c.getColumnIndex(DbHelper.CATEGORY));
                                chname = c.getString(c.getColumnIndex(DbHelper.NAME));
                                chdescription = c.getString(c.getColumnIndex(DbHelper.CATEGORY));
                                chaddress = c.getString(c.getColumnIndex(DbHelper.CHURCH_LOCATION));
                                chphone = c.getString(c.getColumnIndex(DbHelper.CONTACTS));
                                chemail = c.getString(c.getColumnIndex(DbHelper.WEB));
                                chlat = c.getString(c.getColumnIndex(DbHelper.LATITUDE));
                                chlng = c.getString(c.getColumnIndex(DbHelper.LONGITUDE));
                                chsearch_tag = c.getString(c.getColumnIndex(DbHelper.NAME));


                                FM = new FileManager(getActivity());
                                imagename = chid + ".jpg";
                                File file = new File(FM.getFileAt("images", imagename).getAbsolutePath());

                                if (file.exists()) {
                                    ImageLocationid = file.getAbsolutePath();


                                } else {
                                    ImageLocationid = null;

                                }

                                if (!chlat.equals("") && !chlng.equals("")) {
                                    latit = Double.parseDouble(chlat);
                                    longi = Double.parseDouble(chlng);
                                    pos = new LatLng(latit, longi);

                                }
                                pinpoint(chid, chcat_id, chname, chdescription, chaddress, chphone, chemail, pos, chsearch_tag, ImageLocationid);

                            }
                            c.close();

                        } else {


                            Toast.makeText(getActivity(), "There is no location data ", Toast.LENGTH_LONG).show();

                        }


                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                            @Override
                            public boolean onMarkerClick(final Marker marker) {
                                car.hide();
                                walk.hide();
                                routefab.show();
                                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                                mMap.getUiSettings().setMapToolbarEnabled(false);
                                final double dmarkLat = marker.getPosition().latitude;
                                final double dmarkLong = marker.getPosition().longitude;
                                assert routefab != null;
                                //on click get Current location
                                routefab.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        walk.show();
                                        car.show();
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

                                        routefab.hide();
                                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                                        mMap.getUiSettings().setMapToolbarEnabled(false);

                                        car.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                walk.hide();
                                                progressWheel.setVisibility(View.VISIBLE);
                                                getRoutefromMarkerDriving(dmarkLat, dmarkLong);

                                            }
                                        });


                                        walk.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                car.hide();
                                                progressWheel.setVisibility(View.VISIBLE);
                                                getRoutefromMarkerwalking(dmarkLat, dmarkLong);

                                            }
                                        });


                                    }

                                });
                                return false;
                            }

                        });








                    }


                });









                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {


                        Cursor cursor =DbHelper.getMarkerDataRowByname(marker.getTitle());
                        Bundle args = new Bundle();
                        if (cursor.getCount()!=0) {
                            String churchidbyname = cursor.getString(cursor.getColumnIndex(DbHelper.ID));
                            args.putString("Keyid", churchidbyname);
                        }
                        ChurchDetail secondFrag = new ChurchDetail();
                        args.putString("MarkerName", marker.getTitle());
                        Utils.psLog("Selected Item Name : " + chcat_id);
                        secondFrag.setArguments(args);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, secondFrag)
                                .addToBackStack("tag_back_map_fragment")
                                .commit();

                    }
                });






            }});
    }

    @TargetApi(23)
    private void checklocationpermission() {


        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(  new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION   },
                    MapFragment.MY_PERMISSION_ACCESS_FINE_LOCATION );
            requestPermissions(  new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    MapFragment.MY_PERMISSION_ACCESS_COURSE_LOCATION );

        }

    }

    /**------------------------------------------------------------------------------------------------
     * End Block - Private Functions
     **------------------------------------------------------------------------------------------------*/

    /**
     * ------------------------------------------------------------------------------------------------
     * Start Block - Public Functions
     * *------------------------------------------------------------------------------------------------
     */

    private void pinpoint(final String chid, final String chcat_id, final String chname, final String chdescription, String chaddress, final String chphone, String chemail, final LatLng pos, String chsearch_tag, final String imageLocation) {
        customMarker = mMap.addMarker(new MarkerOptions()
                .position(pos)
                .title(chname)
                .snippet(chdescription.substring(0, Math.min(chdescription.length(), 80)) + "...")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.church_icon))
                .anchor(0.5f, 1));


        if (imageLocation != null) {


            markerImages.put(customMarker.getId(), imageLocation);
        }

// markerImages.put(customMarker.getId(), Uri.parse(Config.APP_IMAGES_URL + itd.images.get(0).path));


        if (markerInfo != null) {
//                                markerInfo.put(customMarker, itd);
        }

        if (markerAddress != null) {
            markerAddress.put(customMarker.getId(), chphone);
        }

        if (churchid != null) {
            churchid.put(customMarker.getId(), chid);
        }

        mMap.setInfoWindowAdapter(new MapPopupAdapter(getActivity(), getActivity().getLayoutInflater(), markerImages, markerAddress,churchid));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                return true;
            }
        });


    }



    public void getcurrentlocation(View view) {
        checkingLatLng = false;

        getCurrentLocation(addressTextView);
        if( checkingLatLng==false) {
            addressTextView.setVisibility(View.VISIBLE);
            progressWheel.setVisibility(View.GONE);
            ploteCurrentLocation(currentLatitude, currentLongitude);
        }

    }

    private void ploteCurrentLocation(double currentLatitude, double currentLongitude) {

        checklocationpermission();
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

        // getting GPS status
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        /** check if GPS enabled**/

            if (isNetworkEnabled) {
                Toast.makeText(getActivity(), "please wait..." , Toast.LENGTH_SHORT).show();

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                //  Location location = mMap.getMyLocation();

                LatLng target = new LatLng(currentLatitude, currentLongitude);

                if (target!= null) {

                    FloatingActionButton fab2 = (FloatingActionButton) getView().findViewById(R.id.route1);
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
                            progressWheel.setVisibility(View.GONE);
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            return true;
                        }
                    });

                }
            }





    }


//    public void getcurrentlocation(View view) {
//        checkingLatLng = false;
//        boolean wrapInScrollView = true;
//        dialog = new MaterialDialog.Builder(getActivity())
//                .title(R.string.location_search_title)
//                .customView(R.layout.slider, wrapInScrollView)
//                .show();
//
//        view = dialog.getCustomView();
//        Button BtnSearch = (Button) view.findViewById(R.id.button_search);
//        final TextView addressTextView = (TextView) view.findViewById(R.id.complete_address);
//        getCurrentLocation(addressTextView);
//        final Slider slider = (Slider) view.findViewById(R.id.location_slider);
//        BtnSearch.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                dialog.hide();
//                Utils.psLog(String.valueOf(slider.getValue()));
//               // googleMap.clear();
//                Utils.psLog(Config.APP_API_URL + Config.SEARCH_BY_GEO + slider.getValue() + "/userLat/" + currentLatitude + "/userLong/" + currentLongitude + "/city_id/" + selectedCityId + "/sub_cat_id/" + selectedSubCatId);
////                requestData(Config.APP_API_URL + Config.SEARCH_BY_GEO + slider.getValue() + "/userLat/" + currentLatitude + "/userLong/" + currentLongitude + "/city_id/" + selectedCityId + "/sub_cat_id/" + selectedSubCatId, marker);
//
//            }
//        });
//    }

    public void showWaitPopup() {

        GPSTracker gps = new GPSTracker(getActivity());


        if (gps.canGetLocation()) {

            new MaterialDialog.Builder(getActivity())
                    .title(R.string.pls_wait)
                    .content(R.string.gps_not_ready)
                    .positiveText(R.string.OK)
                    .show();
        } else {
            gps.showSettingsAlert();


        }

    }

    public void showNoServicePopup() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.sorry_title)
                .content(R.string.no_google_play)
                .positiveText(R.string.OK)
                .show();
    }

    public boolean readyLatLng() {
        GPSTracker gps = new GPSTracker(getActivity());

        if (gps.canGetLocation()) {
            currentLatitude = gps.getLatitude();
            currentLongitude = gps.getLongitude();
            if (currentLatitude != 0.0 && currentLongitude != 0.0) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }

    }

    public void getCurrentLocation(TextView tv) {
        GPSTracker gps = new GPSTracker(getActivity());


        if (gps.canGetLocation()) {
            currentLatitude = gps.getLatitude();
            currentLongitude = gps.getLongitude();
             addressTextView.setVisibility(View.VISIBLE);
        } else {
            gps.showSettingsAlert();
            checkingLatLng = true;
            addressTextView.setVisibility(View.INVISIBLE);

        }

        tv.setText(getCompleteAddressString(currentLatitude, currentLongitude));

    }



    public void getRoutewalking() {


        checkingLatLng = false;

        getCurrentLocation(addressTextView);
        if( checkingLatLng==false) {


            LatLng Currentlocation = new LatLng(currentLatitude, currentLongitude);
            // Location location = mMap.getMyLocation();
            if (Currentlocation != null) {
                // if (location != null) {
//                                                double latitude = location.getLatitude();
//                                                double longitude = location.getLongitude();


                Intent intent = getActivity().getIntent();
                String dLon = intent.getStringExtra("Lon");
                final String dLat = intent.getStringExtra("Lat");

                double dlatitude = Double.parseDouble(dLat);
                double dlongitude = Double.parseDouble(dLon);
                final LatLng destLocation = new LatLng(dlatitude, dlongitude);


                String modewalking = "walking";
                route(Currentlocation, destLocation, modewalking);


                ploteCurrentLocation(currentLatitude, currentLongitude);
            }


            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            checklocationpermission();


        }

    }



    public void getRouteCurrentLocation() {


        checkingLatLng = false;

        getCurrentLocation(addressTextView);
        if( checkingLatLng==false) {


            LatLng Currentlocation = new LatLng(currentLatitude, currentLongitude);
            // Location location = mMap.getMyLocation();
            if (Currentlocation != null) {
                // if (location != null) {
//                                                double latitude = location.getLatitude();
//                                                double longitude = location.getLongitude();




                Intent intent = getActivity().getIntent();
                String dLon = intent.getStringExtra("Lon");
                final String dLat = intent.getStringExtra("Lat");

                double dlatitude = Double.parseDouble(dLat);
                double dlongitude = Double.parseDouble(dLon);
                final LatLng destLocation = new LatLng(dlatitude, dlongitude);

                String modedriving = "driving";
                route(Currentlocation, destLocation, modedriving);


                ploteCurrentLocation(currentLatitude, currentLongitude);
            }


            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            checklocationpermission();






        }

    }






    public void getRoutefromMarkerwalking(double dlatitude,double dlongitude) {


        checkingLatLng = false;

        getCurrentLocation(addressTextView);
        if( checkingLatLng==false) {


            car.hide();
            checklocationpermission();

            LatLng Currentlocation = new LatLng(currentLatitude, currentLongitude);
            // Location location = mMap.getMyLocation();
            if (Currentlocation != null) {

                final LatLng destLocation = new LatLng(dlatitude, dlongitude);
                String modewalking = "walking";
                route(Currentlocation, destLocation, modewalking);
                ploteCurrentLocation(currentLatitude, currentLongitude);
            }


            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
        }


    }



    public void getRoutefromMarkerDriving(double dmarkLat, double dmarkLong) {


        checkingLatLng = false;

        getCurrentLocation(addressTextView);
        if( checkingLatLng==false) {

            walk.hide();
            checklocationpermission();

            LatLng Currentlocation = new LatLng(currentLatitude, currentLongitude);
            if (Currentlocation != null) {
            final LatLng destLocation = new LatLng(dmarkLat, dmarkLong);
                        String modedriving = "driving";
                        route(Currentlocation, destLocation, modedriving);
            }

        }

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

    /**------------------------------------------------------------------------------------------------
     * End Block - Public Functions
     **------------------------------------------------------------------------------------------------*/


}
