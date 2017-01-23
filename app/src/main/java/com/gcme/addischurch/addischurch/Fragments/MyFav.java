package com.gcme.addischurch.addischurch.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.gcme.addischurch.addischurch.Adapters.FavoriteAdapter;
import com.gcme.addischurch.addischurch.Adapters.NewsFeeds;
import com.gcme.addischurch.addischurch.Adapters.ScheduleAdapter;
import com.gcme.addischurch.addischurch.Adapters.UIUtils;
import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.Model.Schedules;
import com.gcme.addischurch.addischurch.Model.favorites;
import com.gcme.addischurch.addischurch.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MyFav extends Fragment {

    public static DatabaseAdaptor DbHelper;
    public static ListView favList;
    public static Context context;
    public static TextView Nofav;
    private FloatingSearchView mSearchView4;
    private String mLastQuery = "";
    public MyFav() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbHelper = new DatabaseAdaptor(getActivity());
        context=getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_fav, container, false);
        favList= (ListView) view.findViewById(R.id.fav_list);
        Nofav= (TextView) view.findViewById(R.id.nofav);
        getfavlist();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchView4 = (FloatingSearchView) view.findViewById(R.id.floating_search_view4);
        final DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        mSearchView4.attachNavigationDrawerToMenuButton(mDrawerLayout);




        mSearchView4.setOnLeftMenuClickListener(
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
        mSearchView4.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                DbHelper = new DatabaseAdaptor(getActivity());
                Cursor cursor = DbHelper.getCategoryListByKeyword(newQuery);
                ListView activeList = (ListView) getView().findViewById(R.id.fav_list);



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


        mSearchView4.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
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


    public static void getfavlist() {

        Cursor cursor=DbHelper.getAllfav();
        String favid;

        if(cursor.getCount()>0 && cursor.moveToFirst()) {
            List<favorites> favoritelist = new ArrayList<favorites>();
            Cursor cu;
            for (int i = 0; i < cursor.getCount(); i++) {

                cursor.moveToPosition(i);
            //    cursor.moveToNext();
                favid=cursor.getString(cursor.getColumnIndex(DbHelper.FavSelected));

                    cu = DbHelper.getMarkerDataRowByID(favid);

                favorites favorite1 = new favorites();

                favorite1.setchName(cu.getString(cu.getColumnIndex(DbHelper.NAME)));
                favorite1.setDeno(cu.getString(cu.getColumnIndex(DbHelper.CATEGORY)));
                favorite1.setId(cu.getInt(cu.getColumnIndex(DbHelper.ID)));
                    favoritelist.add(favorite1);


            }

            cursor.close();

            FavoriteAdapter favoriteAdapter = new FavoriteAdapter(favoritelist, context);
            favList.setAdapter(favoriteAdapter);
            UIUtils.setListViewHeightBasedOnItems(favList);
            favList.setVisibility(View.VISIBLE);
            Nofav.setVisibility(View.INVISIBLE);

        }else {
            Toast.makeText(context, "There is no schedule!", Toast.LENGTH_LONG).show();
            favList.setVisibility(View.INVISIBLE);
            Nofav.setVisibility(View.VISIBLE);
        }


    }

}