package com.gcme.addischurch.addischurch.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.gcme.addischurch.addischurch.Adapters.MyRecyclerAdapter;
import com.gcme.addischurch.addischurch.Adapters.NetworkController;
import com.gcme.addischurch.addischurch.Adapters.NewsFeeds;
import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.FileManager.FileManager;
import com.gcme.addischurch.addischurch.MainActivity;
import com.gcme.addischurch.addischurch.R;
import org.json.JSONArray;
import org.json.JSONObject;
import android.support.v7.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Rect;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.maps.model.LatLng;


public class AllChurches extends Fragment {


    DatabaseAdaptor DbHelper;
    String url = "http://api.myjson.com/bins/w86a";
    RecyclerView recyclerView;
    List<NewsFeeds> feedsList = new ArrayList<NewsFeeds>();
    MyRecyclerAdapter adapter;
    private FloatingSearchView mSearchView2;
    String no="10";
    public AllChurches() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_churches, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        FileManager FM;
        FM = new FileManager(getActivity());
       // MainActivity ma=new MainActivity();


        adapter = new MyRecyclerAdapter(getActivity(), feedsList,this);
        final RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        int i=0;
        DbHelper=new DatabaseAdaptor(getActivity());
        Cursor c=DbHelper.getAlldenominations();

        c.moveToFirst();
        if(c.getCount()>0){
            i++;
            try {

                while (!c.isAfterLast()) {

                    final String cname = c.getString(c.getColumnIndex(DbHelper.CATEGORY));





                    Cursor cu= DbHelper.getSelectedRows(cname);
                    final int numberofchurches = cu.getCount();
                    if(numberofchurches>0) {

                        no = String.valueOf(numberofchurches);
                        //
                    }else {
                         no= "0";
                    }

                    String imagename=c.getString(c.getColumnIndex(DbHelper.IDCAT))+".jpg";
                    String ImageLocationid = FM.getFileAt("denominations", imagename).getAbsolutePath();
                    File file = new File(ImageLocationid);


                    if (cname!=null && ImageLocationid!=null) {
                        if (file.exists()) {
                        NewsFeeds feeds = new NewsFeeds(cname, ImageLocationid,no);

                        // adding movie to movies array
                        feedsList.add(feeds);

                        }else {
                            ImageLocationid=FM.getFileAt("denominations","0.jpg").getAbsolutePath();
                            NewsFeeds feeds = new NewsFeeds(cname, ImageLocationid,no);

                            // adding movie to movies array
                            feedsList.add(feeds);

                        }


                }
                    c.moveToNext();

                }
                c.close();
            }catch (Exception e) {
                System.out.println(e.getMessage());

            } finally {
                //Notify adapter about data changes
                adapter.notifyItemChanged(i);
            }

        }else {

              Toast.makeText(getActivity(),"There is no location data " , Toast.LENGTH_LONG).show();

        }

        /**Recicler view on click listner**/



        return view;

    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchView2 = (FloatingSearchView) view.findViewById(R.id.floating_search_view2);
        final DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        mSearchView2.attachNavigationDrawerToMenuButton(mDrawerLayout);




        mSearchView2.setOnLeftMenuClickListener(
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
        mSearchView2.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                if ( TextUtils.isEmpty ( newQuery ) ) {

                } else {

                }

            }






        });


        mSearchView2.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {


            }

            @Override
            public void onSearchAction(String query) {

            }
        });






    }











}