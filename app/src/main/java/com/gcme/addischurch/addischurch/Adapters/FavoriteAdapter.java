package com.gcme.addischurch.addischurch.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.Fragments.AllChurches;
import com.gcme.addischurch.addischurch.Fragments.Home_fragment;
import com.gcme.addischurch.addischurch.Model.Schedules;
import com.gcme.addischurch.addischurch.Model.favorites;
import com.gcme.addischurch.addischurch.R;

import java.util.List;

import static com.gcme.addischurch.addischurch.Fragments.MyFav.getfavlist;

/**
 * Created by kzone on 12/10/2016.
 */

public class FavoriteAdapter extends BaseAdapter {
    private List<favorites> favoritechurch;
    private Context myContext;
    DatabaseAdaptor DBhelper;
    int id;
    public FavoriteAdapter(List<favorites> favoritechurch, Context myContext) {
        this.favoritechurch = favoritechurch;
        this.myContext = myContext;
        DBhelper=new DatabaseAdaptor(myContext);
    }

    @Override
    public int getCount() {
        return favoritechurch.size();
    }

    @Override
    public Object getItem(int i) {
        return favoritechurch.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater= (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.favorite_list_item, null, false);
        TextView chName= (TextView) view.findViewById(R.id.fav_church_name);
        TextView denName= (TextView) view.findViewById(R.id.fav_denomination);
        ImageButton delbutt= (ImageButton) view.findViewById(R.id.delbutt);
        delbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(myContext);
                alertDialog.setCancelable(false);
                // Setting Dialog Title
                alertDialog.setTitle("Wede Church");

                // Setting Dialog Message
                alertDialog.setMessage("Do you want To delete "+ favoritechurch.get(i).getchName()+ " From your Favorites? ");

                // On pressing Settings button
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        id=favoritechurch.get(i).getId();

                        DBhelper.deletefav(Integer.toString(id));
                        getfavlist();

                    }
                });

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    }
                });


                // Showing Alert Message
                alertDialog.show();

            }
        });
        chName.setText(favoritechurch.get(i).getchName());
        denName.setText(favoritechurch.get(i).getDeno());

        return view;
    }












}
