package com.gcme.addischurch.addischurch.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.Fragments.ChurchDenomination;
import com.gcme.addischurch.addischurch.Fragments.ChurchDetail;
import com.gcme.addischurch.addischurch.Fragments.MapFragment;
import com.gcme.addischurch.addischurch.Model.denomination_list;
import com.gcme.addischurch.addischurch.Model.search;
import com.gcme.addischurch.addischurch.R;

import java.util.List;

/**
 * Created by kzone on 12/10/2016.
 */

public class DenominationAdapter extends BaseAdapter {
    private List<denomination_list> churchDeno;
    private Context myContext;
    DatabaseAdaptor DBhelper;
    int id;
    ChurchDenomination Denofrag;
    String rowName;
    public DenominationAdapter(List<denomination_list> churchdeno, Context myContext, ChurchDenomination denofrag) {
        this.churchDeno = churchdeno;
        this.myContext = myContext;
        DBhelper=new DatabaseAdaptor(myContext);
        this.Denofrag=denofrag;
    }

    @Override
    public int getCount() {
        return churchDeno.size();
    }

    @Override
    public Object getItem(int i) {
        return churchDeno.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater= (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.deno_church_list, null, false);
        TextView chName= (TextView) view.findViewById(R.id.denochurchname);
        TextView chLocation= (TextView) view.findViewById(R.id.denochurchlocation);
        ImageView delbutt= (ImageView) view.findViewById(R.id.ic_go_select_deno);
        delbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                id = churchDeno.get(i).getId();
                rowName = churchDeno.get(i).getchName();



                android.support.v4.app.Fragment fragment = new ChurchDetail();
                android.support.v4.app.FragmentManager manager= Denofrag.getActivity().getSupportFragmentManager();
                Bundle args = new Bundle();
                args.putString("Key",String.valueOf(rowName));
                args.putString("Keyid",String.valueOf(id));
                fragment .setArguments(args);

                android.support.v4.app.FragmentTransaction transaction =   manager.beginTransaction();
                transaction.replace(R.id.fragment_container,fragment);
                transaction.addToBackStack("tag_back_churchdetail");
                transaction.commit();










            }
        });
        chName.setText(churchDeno.get(i).getchName());
        chLocation.setText(churchDeno.get(i).getDeno());

        return view;
    }












}
