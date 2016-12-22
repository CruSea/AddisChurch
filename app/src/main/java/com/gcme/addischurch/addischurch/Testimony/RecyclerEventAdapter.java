package com.gcme.addischurch.addischurch.Testimony;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.gcme.addischurch.addischurch.Event.EventDetail;
import com.gcme.addischurch.addischurch.Fragments.AllChurches;
import com.gcme.addischurch.addischurch.Fragments.ChurchDenomination;
import com.gcme.addischurch.addischurch.Fragments.ChurchDetail;
import com.gcme.addischurch.addischurch.Fragments.ChurchEventDetail;
import com.gcme.addischurch.addischurch.R;

import java.util.List;

/**
 * Created by buty on 11/27/16.
 */

public class RecyclerEventAdapter extends RecyclerView.Adapter<RecyclerEventAdapter.MyViewHolder> {

    private List<EventHandler> feedsList;
    private Context context;
    private LayoutInflater inflater;
    ChurchDetail churchdetail;

    public RecyclerEventAdapter(Context context, List<EventHandler> feedsList) {

        this.context = context;
        this.feedsList = feedsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.singleitem1_recyclerview, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final EventHandler feeds = feedsList.get(position);
        //Pass the values of feeds object to Views
        holder.title.setText(feeds.getFeedName());
        holder.source.setText(feeds.getSource());
        holder.imageview.setImageUrl(feeds.getImgURL(), NetworkEventController.getInstance(context).getImageLoader());
        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"clicked" + feeds.getFeedName(),Toast.LENGTH_SHORT).show();
//                Bundle args = new Bundle();
//                args.putString("denominations",feeds.getFeedName());



                //start the second activity
                android.support.v4.app.Fragment fragment = new ChurchEventDetail();
                android.support.v4.app.FragmentManager manager= ((AppCompatActivity) context).getSupportFragmentManager();
                Bundle args = new Bundle();
                args.putString("fedname",feeds.getFeedName());
                args.putString("sorce", feeds.getSource());
                args.putString("imge", feeds.getImgURL());
                fragment.setArguments(args);

                android.support.v4.app.FragmentTransaction transaction =   manager.beginTransaction();
                transaction.replace(R.id.fragment_container,fragment);
                transaction.addToBackStack("tag_back_Events");
                transaction.commit();

            }
        });
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Toast.makeText(Main2Activity.this, post_key , Toast.LENGTH_SHORT).show();
//                Intent singleblog = new Intent(RecyclerEventAdapter.this, SingleEventView.class);
//                singleblog.putExtra("blog_id", post_key);
//                startActivity(singleblog);
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return feedsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, source;
        private NetworkImageView imageview;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title_viewchurchdetail);
            source = (TextView) itemView.findViewById(R.id.sourcechurchdetail);

            // Volley's NetworkImageView which will load Image from URL
            imageview = (NetworkImageView) itemView.findViewById(R.id.thumbnailchurchdetail);


        }
    }
}

