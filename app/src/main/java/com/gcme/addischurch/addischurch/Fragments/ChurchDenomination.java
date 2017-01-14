package com.gcme.addischurch.addischurch.Fragments;



import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.gcme.addischurch.addischurch.Adapters.MyRecyclerAdapter;
import com.gcme.addischurch.addischurch.Adapters.NetworkController;
import com.gcme.addischurch.addischurch.Adapters.NewsFeeds;
import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.FileManager.FileManager;
import com.gcme.addischurch.addischurch.R;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Rect;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.gcme.addischurch.addischurch.Adapters.MyRecyclerAdapter;
import com.gcme.addischurch.addischurch.Adapters.NewsFeeds;
import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.FileManager.FileManager;
import com.gcme.addischurch.addischurch.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ChurchDenomination extends Fragment {

    private FloatingSearchView mSearchView3;

    private String mLastQuery = "";
    DatabaseAdaptor DbHelper;
    String url = "http://api.myjson.com/bins/w86a";
    RecyclerView recyclerView;
    List<NewsFeeds> feedsList = new ArrayList<NewsFeeds>();
    MyRecyclerAdapter adapter;
    String Selecteditem;
    public ChurchDenomination() {

      //  denominations
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_church_denomination, container, false);

        if(getArguments().getString("denominations")!=null) {
             Selecteditem = getArguments().getString("denominations");
        }


        mSearchView3 = (FloatingSearchView) view.findViewById(R.id.floating_search_view3);
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
                        .addToBackStack("tag_back_churchdetail")
                        .commit();


        }
        });


        DbHelper = new DatabaseAdaptor(getContext());



        Cursor cursor = DbHelper.getSelectedRows(Selecteditem);
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

        mSearchView3.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
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




        return view;

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchView3 = (FloatingSearchView) view.findViewById(R.id.floating_search_view3);
        final DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        mSearchView3.attachNavigationDrawerToMenuButton(mDrawerLayout);




        mSearchView3.setOnLeftMenuClickListener(
                new FloatingSearchView.OnLeftMenuClickListener() {
                    @Override
                    public void onMenuOpened() {
//                        Toast.makeText(getActivity(), "menu opened" , Toast.LENGTH_SHORT).show();

                        mDrawerLayout.openDrawer(Gravity.LEFT);


                    }

                    @Override
                    public void onMenuClosed() {
                    //    Toast.makeText(getActivity(), "menu closed" , Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }
                });





        setupSearchBar();
    }

    private void setupSearchBar() {
        mSearchView3.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

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


        mSearchView3.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
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



}