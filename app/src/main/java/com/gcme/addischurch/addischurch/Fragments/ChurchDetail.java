package com.gcme.addischurch.addischurch.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gcme.addischurch.addischurch.R;
import com.gcme.addischurch.addischurch.Testimony.App;
import com.gcme.addischurch.addischurch.Testimony.Snap;
import com.gcme.addischurch.addischurch.Testimony.SnapAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChurchDetail extends Fragment {

    public static final String ORIENTATION = "orientation";

    private RecyclerView mRecyclerView;
    private boolean mHorizontal;
    ProgressDialog pd;
    ArrayList<String> aa = new ArrayList<String>();
    ArrayList<String> num= new ArrayList<String>();

    public ChurchDetail() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_church_detail, container, false);
/**recieve the church name**/
        if(getArguments().getString("Key")!=null) {
    String SelectedSearchitem = getArguments().getString("Key");
    Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbar.inflateMenu(R.menu.main);

    toolbar.setTitle(SelectedSearchitem);
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
