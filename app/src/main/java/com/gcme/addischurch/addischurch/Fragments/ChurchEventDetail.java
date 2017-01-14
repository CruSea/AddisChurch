package com.gcme.addischurch.addischurch.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.gcme.addischurch.addischurch.Event.NetworkEventController;
import com.gcme.addischurch.addischurch.R;


public class ChurchEventDetail extends Fragment {


    TextView churchnamedetail, churchsourcedetail;
    NetworkImageView churchdetailimage;
    Context context;

    public ChurchEventDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_church_event_detail, container, false);


        churchnamedetail = (TextView) view.findViewById(R.id.church_detail_title);
        churchsourcedetail = (TextView) view.findViewById(R.id.church_detail_source);
        churchdetailimage = (NetworkImageView) view.findViewById(R.id.church_detail_image);

        Bundle arguments = getArguments();
        String name = arguments.getString("fedname");
        String sorce = arguments.getString("sorce");
       // String imge = arguments.getString("imge");

        ImageLoader imageLoader = com.gcme.addischurch.addischurch.Testimony.NetworkEventController.getInstance(context).getImageLoader();
        String bitmap = arguments.getString("imge");
        churchdetailimage.setImageUrl(bitmap, imageLoader);

        churchnamedetail.setText(name);
        churchsourcedetail.setText(sorce);
     //   churchdetailimage.setText(name);




//        if(getArguments().getString("feedname")!=null) {
//            String Selecteditemid = getArguments().getString("feedname");
//            churchnamedetail.setText(Selecteditemid);
//           // Toast.makeText(getActivity(), Selecteditemid, Toast.LENGTH_SHORT).show();
//
//        }
//        churchnamedetail = getIntent().getExtras().getString("feedname");
//        churchsourcedetail = getIntent().getExtras().getString("source");
//
//
//        ImageLoader imageLoader = com.gcme.addischurch.addischurch.Testimony.NetworkEventController.getInstance(context).getImageLoader();
//        String bitmap = getIntent().getStringExtra("image");
//        churchdetailimage.setImageUrl(bitmap, imageLoader);
//
// churchnamedetail.setText(strtext);
    //    churchsourcedetail.setText(source);

        return view;
    }


}
