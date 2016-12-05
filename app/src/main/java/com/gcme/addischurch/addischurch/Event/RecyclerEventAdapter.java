package com.gcme.addischurch.addischurch.Event;

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

    private List<EventsHandler> feedsList;
    private Context context;
    private LayoutInflater inflater;

    public RecyclerEventAdapter(Context context, List<EventsHandler> feedsList) {

        this.context = context;
        this.feedsList = feedsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.eventitem_recyclerview, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EventsHandler feeds = feedsList.get(position);
        //Pass the values of feeds object to Views
        holder.title.setText(feeds.getFeedName());
        holder.source.setText(feeds.getSource());
        holder.imageview.setImageUrl(feeds.getImgURL(), NetworkEventController.getInstance(context).getImageLoader());
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


            title = (TextView) itemView.findViewById(R.id.title_view);
            source = (TextView) itemView.findViewById(R.id.source);

            // Volley's NetworkImageView which will load Image from URL
            imageview = (NetworkImageView) itemView.findViewById(R.id.thumbnail);


        }
    }
}

