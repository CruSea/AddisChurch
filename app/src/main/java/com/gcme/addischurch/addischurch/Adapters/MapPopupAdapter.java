package com.gcme.addischurch.addischurch.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcme.addischurch.addischurch.Fragments.ChurchDenomination;
import com.gcme.addischurch.addischurch.Fragments.ChurchDetail;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.gcme.addischurch.addischurch.utilities.*;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.gcme.addischurch.addischurch.R;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Panacea-Soft on 26/7/15.
 * Contact Email : teamps.is.cool@gmail.com
 */
public class MapPopupAdapter implements GoogleMap.InfoWindowAdapter {
    private View popup=null;
    private LayoutInflater inflater=null;
    private HashMap<String, String> images=null;
    private HashMap<String, String> addressInfo = null;
    private HashMap<String, String> ChurchID = null;
    private Context ctxt=null;
    private int iconWidth=-1;
    private int iconHeight=-1;
    private Marker lastMarker=null;
    public static GoogleMap mMap;

    public MapPopupAdapter(Context ctxt, LayoutInflater inflater,
                           HashMap<String, String> images, HashMap<String, String> addressInfo,HashMap<String, String> churchid) {
        this.ctxt = ctxt;
        this.inflater = inflater;
        this.images = images;
        this.addressInfo = addressInfo;
        this.ChurchID = churchid;

        iconWidth=
                ctxt.getResources().getDimensionPixelSize(R.dimen.map_icon_width);
        iconHeight=
                ctxt.getResources().getDimensionPixelSize(R.dimen.map_icon_height);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return(null);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {
        if (popup == null) {
            popup=inflater.inflate(R.layout.popup_marker, null);
        }

        if (lastMarker == null
                || !lastMarker.getId().equals(marker.getId())) {
            lastMarker=marker;

            TextView txtTitle =(TextView)popup.findViewById(R.id.title);
            txtTitle.setText(marker.getTitle());
            txtTitle.setTypeface(Utils.getTypeFace(Utils.Fonts.ROBOTO));

            TextView txtDescription = (TextView)popup.findViewById(R.id.snippet);
            txtDescription.setText(marker.getSnippet());
            txtDescription.setTypeface(Utils.getTypeFace(Utils.Fonts.ROBOTO));

            TextView txtAddress = (TextView) popup.findViewById(R.id.address);
            txtAddress.setText(addressInfo.get(marker.getId()));
            txtAddress.setTypeface(Utils.getTypeFace(Utils.Fonts.ROBOTO));




            String image=images.get(marker.getId());
            File file = new File(image);
            ImageView icon=(ImageView)popup.findViewById(R.id.icon);
            if(image == null) {
                icon.setVisibility(View.GONE);
            } else {
//                menuImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
//
               // icon.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()))

                Picasso.with(ctxt).load(file).resize(iconWidth, iconHeight)
                        .centerCrop().noFade()
                        .placeholder(R.drawable.placeholder)
                        .into(icon, new MarkerCallback(marker));
            }

        }

        return(popup);
    }


//    @Override
//    public void onInfoWindowClick(Marker marker) {
//
//        ChurchDetail churchdetail = new ChurchDetail();
//
//        android.support.v4.app.Fragment fragment = new ChurchDenomination();
//        android.support.v4.app.FragmentManager manager= churchdetail.getActivity().getSupportFragmentManager();
//        Bundle args = new Bundle();
//        args.putString("MarkerName",marker.getTitle());
//        args.putString("Keyid", ChurchID.get(marker.getId()));
//        fragment .setArguments(args);
//        android.support.v4.app.FragmentTransaction transaction =   manager.beginTransaction();
//        transaction.replace(R.id.fragment_container,fragment);
//        transaction.addToBackStack("tag_back_home_tab");
//        transaction.commit();
//
//
//    }



    static class MarkerCallback implements Callback {
        Marker marker=null;

        MarkerCallback(Marker marker) {
            this.marker=marker;
        }

        @Override
        public void onError() {
            Utils.psLog(getClass().getSimpleName() +  "Error loading thumbnail!");
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.showInfoWindow();
            }
        }
    }


}
