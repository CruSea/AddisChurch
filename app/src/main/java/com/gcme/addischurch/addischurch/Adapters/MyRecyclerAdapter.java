package com.gcme.addischurch.addischurch.Adapters;



import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
//import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import com.gcme.addischurch.addischurch.Fragments.AllChurches;
import com.gcme.addischurch.addischurch.Fragments.ChurchDenomination;
import com.gcme.addischurch.addischurch.Fragments.ChurchDetail;
import com.gcme.addischurch.addischurch.MainActivity;
import com.gcme.addischurch.addischurch.R;

import java.io.File;
import java.util.List;

/**
 * Created by buty on 11/15/16.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>  {

    private List<NewsFeeds> feedsList;
    private Context context;
    private LayoutInflater inflater;
    AllChurches allchurches;
    DatabaseAdaptor DbHelper;

    public MyRecyclerAdapter(Context context, List<NewsFeeds> feedsList, AllChurches allchurches) {
        this.allchurches=allchurches;
        this.context = context;
        this.feedsList = feedsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.singleitem_recyclerview, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final NewsFeeds feeds = feedsList.get(position);
        //Pass the values of feeds object to Views
        holder.title.setText(feeds.getFeedName());
        holder.numberofchurches.setText((feeds.getnumber()));
        File file = new File(feeds.getImageLocation());
        holder.imageview.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        holder. imageview .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



//                Cursor cursor = DbHelper.getSelectedRows();
//
//
//                String rowId = cursor.getString(cursor.getColumnIndex(DbHelper.CATEGORY));







                android.support.v4.app.Fragment fragment = new ChurchDenomination();
                android.support.v4.app.FragmentManager manager= allchurches.getActivity().getSupportFragmentManager();
                Bundle args = new Bundle();
                args.putString("denominations",feeds.getFeedName());
                fragment .setArguments(args);

                android.support.v4.app.FragmentTransaction transaction =   manager.beginTransaction();
                transaction.replace(R.id.fragment_container,fragment);
                transaction.addToBackStack("tag_back_Events");
                transaction.commit();

               }
        });
    }


//    /**
//     * Showing popup menu when tapping on 3 dots
//     */
//    private void showPopupMenu(View view) {
//        // inflate menu
//        PopupMenu popup = new PopupMenu(mContext, view);
//        MenuInflater inflater = popup.getMenuInflater();
//        //inflater.inflate(R.menu.menu_album, popup.getMenu());
//        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
//        popup.show();
//    }
//
//    /**
//     * Click listener for popup menu items
//     */
//    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
//
//        public MyMenuItemClickListener() {
//
//
////            android.support.v4.app.FragmentManager fragmentManager =getSupportFragmentManager();
////            android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
////            fragmentTransaction.replace(R.id.fragment_container, new ChurchDetail());
////            fragmentTransaction.addToBackStack("tag_back_Events");
////            fragmentTransaction.commit();
//
//
//        }
//
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//
//            return false;
//        }
//    }

    @Override
    public int getItemCount() {
        return feedsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView numberofchurches;
        private ImageView imageview;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_view);
            numberofchurches = (TextView) itemView.findViewById(R.id.numberofchurches);
            // Volley's NetworkImageView which will load Image from URL
            imageview = (ImageView) itemView.findViewById(R.id.thumbnail);



        }
    }

}