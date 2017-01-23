package com.gcme.addischurch.addischurch.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.Fragments.AllChurches;
import com.gcme.addischurch.addischurch.Fragments.ChurchDenomination;
import com.gcme.addischurch.addischurch.Fragments.ChurchDetail;
import com.gcme.addischurch.addischurch.Fragments.MapFragment;
import com.gcme.addischurch.addischurch.Model.favorites;
import com.gcme.addischurch.addischurch.Model.search;
import com.gcme.addischurch.addischurch.R;

import java.util.List;

import static com.gcme.addischurch.addischurch.Fragments.MyFav.getfavlist;

/**
 * Created by kzone on 12/10/2016.
 */

public class SearchAdapter extends BaseAdapter {
    private List<search> searchchurch;
    private Context myContext;
    DatabaseAdaptor DBhelper;
    int id;
    MapFragment mapfrag;
    String rowName;
    public SearchAdapter(List<search> Searchchurch, Context myContext,MapFragment mapfrag) {
        this.searchchurch = Searchchurch;
        this.myContext = myContext;
        DBhelper=new DatabaseAdaptor(myContext);
        this.mapfrag=mapfrag;
    }

    @Override
    public int getCount() {
        return searchchurch.size();
    }

    @Override
    public Object getItem(int i) {
        return searchchurch.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater= (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.child_list_item, null, false);
        TextView chName= (TextView) view.findViewById(R.id.searchchurchname);
        TextView chLocation= (TextView) view.findViewById(R.id.searchlocation);
        ImageView delbutt= (ImageView) view.findViewById(R.id.ic_go_select);
        delbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                id = searchchurch.get(i).getId();
                rowName = searchchurch.get(i).getchName();



                android.support.v4.app.Fragment fragment = new ChurchDetail();
                android.support.v4.app.FragmentManager manager= mapfrag.getActivity().getSupportFragmentManager();
                Bundle args = new Bundle();
                args.putString("Key",String.valueOf(rowName));
                args.putString("Keyid",String.valueOf(id));
                fragment .setArguments(args);

                android.support.v4.app.FragmentTransaction transaction =   manager.beginTransaction();
                transaction.replace(R.id.fragment_container,fragment);
                transaction.addToBackStack("tag_back_mapfrag");
                transaction.commit();








            }
        });
        chName.setText(searchchurch.get(i).getchName());
        chLocation.setText(searchchurch.get(i).getDeno());

        return view;
    }












}
