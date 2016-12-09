package com.gcme.addischurch.addischurch.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.gcme.addischurch.addischurch.Adapters.NewsFeeds;
import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.R;

import java.io.File;


public class MyFav extends Fragment {

    DatabaseAdaptor DbHelper;
    ListView favList;
    public MyFav() {
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
        View view= inflater.inflate(R.layout.fragment_my_fav, container, false);
//        Cursor c=DbHelper.getAllfav();
       // if(c.getCount()!=0){
        getallfavonlist();
     //   }
        favList= (ListView) view.findViewById(R.id.fav_list);

        return view;
    }

    private void getallfavonlist() {

        Cursor cur=DbHelper.getAllfav();

        cur.moveToFirst();
        if(cur.getCount()>0){

            try {

                while (!cur.isAfterLast()) {

                    final String favid = cur.getString(cur.getColumnIndex(DbHelper.FavSelected));

                    Cursor cu = DbHelper.getChurchName(favid);

                    // get church name where churchid is favid
                    String[] churchname = new String[]{DbHelper.NAME};
                    int[] toViewId = new int[]{R.id.titleappointment};
                    SimpleCursorAdapter CurAdapter = new SimpleCursorAdapter(getActivity(), R.layout.child_list_item, cu, churchname, toViewId, 0);

                    favList.setAdapter(CurAdapter);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }}

}