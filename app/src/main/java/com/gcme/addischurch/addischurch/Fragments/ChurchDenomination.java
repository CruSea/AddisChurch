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
import com.gcme.addischurch.addischurch.Adapters.DenominationAdapter;
import com.gcme.addischurch.addischurch.Adapters.MyRecyclerAdapter;
import com.gcme.addischurch.addischurch.Adapters.NetworkController;
import com.gcme.addischurch.addischurch.Adapters.NewsFeeds;
import com.gcme.addischurch.addischurch.Adapters.SearchAdapter;
import com.gcme.addischurch.addischurch.Adapters.UIUtils;
import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.FileManager.FileManager;
import com.gcme.addischurch.addischurch.Model.denomination_list;
import com.gcme.addischurch.addischurch.Model.search;
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
    public static Context context;
    private String mLastQuery = "";
    DatabaseAdaptor DbHelper;
    String url = "http://api.myjson.com/bins/w86a";
    RecyclerView recyclerView;
    List<NewsFeeds> feedsList = new ArrayList<NewsFeeds>();
    MyRecyclerAdapter adapter;
    String Selecteditem;
    ListView listView;
    public ChurchDenomination() {

      //  denominations
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_church_denomination, container, false);

        context=getActivity();
        if(getArguments().getString("denominations")!=null) {
             Selecteditem = getArguments().getString("denominations");
        }


        mSearchView3 = (FloatingSearchView) view.findViewById(R.id.floating_search_view3);
        listView = (ListView) view.findViewById(R.id.activelist1);



        DbHelper = new DatabaseAdaptor(getContext());



        Cursor cu = DbHelper.getSelectedRows(Selecteditem);


        if(cu.getCount()>0 && cu.moveToFirst()) {
            List<denomination_list> denoList = new ArrayList<denomination_list>();


            for (int i = 0; i < cu.getCount(); i++) {
                denomination_list deno1 = new denomination_list();
                cu.moveToPosition(i);
                deno1.setchName(cu.getString(cu.getColumnIndex(DbHelper.NAME)));
                deno1.setDeno(cu.getString(cu.getColumnIndex(DbHelper.CHURCH_LOCATION)));
                deno1.setId(cu.getInt(cu.getColumnIndex(DbHelper.ID)));
                denoList.add(deno1);


            }

            cu.close();

            DenominationAdapter denoAdapter = new DenominationAdapter(denoList, context,ChurchDenomination.this);
            listView.setAdapter(denoAdapter);
            UIUtils.setListViewHeightBasedOnItems(listView);
            listView.setTextFilterEnabled(true);


        }


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
                        mDrawerLayout.openDrawer(Gravity.LEFT);
                    }

                    @Override
                    public void onMenuClosed() {
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
                Cursor cursor = DbHelper.gedenominationListByKeyword(newQuery,Selecteditem);
                ListView activeList = (ListView) getView().findViewById(R.id.search_suggestion_list);


                if(cursor.getCount()>0 && cursor.moveToFirst()) {
                    List<denomination_list> denoList = new ArrayList<denomination_list>();


                    for (int i = 0; i < cursor.getCount(); i++) {
                        denomination_list deno1 = new denomination_list();
                        cursor.moveToPosition(i);
                        deno1.setchName(cursor.getString(cursor.getColumnIndex(DbHelper.NAME)));
                        deno1.setDeno(cursor.getString(cursor.getColumnIndex(DbHelper.CHURCH_LOCATION)));
                        deno1.setId(cursor.getInt(cursor.getColumnIndex(DbHelper.ID)));
                        denoList.add(deno1);


                    }

                    cursor.close();

                    DenominationAdapter denoAdapter = new DenominationAdapter(denoList, context,ChurchDenomination.this);
                    activeList.setAdapter(denoAdapter);
                    UIUtils.setListViewHeightBasedOnItems(activeList);
                    activeList.setTextFilterEnabled(true);


                }



                if (newQuery.length() > 0) {

                    activeList.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);

                    if (cursor == null) {

                        activeList.setVisibility(View.INVISIBLE);

                    } else {

                        activeList.setVisibility(View.VISIBLE);
                    }


                } else {
                    listView.setVisibility(View.VISIBLE);
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

            }
        });

    }


}