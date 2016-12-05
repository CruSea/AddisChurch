package com.gcme.addischurch.addischurch.Testimony;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.gcme.addischurch.addischurch.R;

import java.util.List;

/**
 * Created by buty on 11/27/16.
 */

public class RecyclerEventAdapter extends RecyclerView.Adapter<RecyclerEventAdapter.MyViewHolder> {

    private List<EventHandler> feedsList;
    private Context context;
    private LayoutInflater inflater;

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
        EventHandler feeds = feedsList.get(position);
        //Pass the values of feeds object to Views
        holder.title.setText(feeds.getFeedName());
        holder.source.setText(feeds.getSource());
        holder.imageview.setImageUrl(feeds.getImgURL(), NetworkEventController.getInstance(context).getImageLoader());

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
        View mView;
        private TextView title, source;
        private NetworkImageView imageview;

        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            title = (TextView) mView.findViewById(R.id.title_view);
            source = (TextView) mView.findViewById(R.id.source);

            // Volley's NetworkImageView which will load Image from URL
            imageview = (NetworkImageView) mView.findViewById(R.id.thumbnail);


        }
    }
}

