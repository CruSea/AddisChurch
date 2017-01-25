package com.gcme.addischurch.addischurch.Event;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.gcme.addischurch.addischurch.R;

public class EventDetail extends AppCompatActivity {

    TextView title, sourcedetail, descrip;
    String feed, source,detdesc;
    NetworkImageView image;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        descrip = (TextView) findViewById(R.id.detail_description);
        title = (TextView) findViewById(R.id.detail_title);
        sourcedetail = (TextView) findViewById(R.id.detail_source);
        image = (NetworkImageView) findViewById(R.id.detail_image);

        feed = getIntent().getExtras().getString("name");
        source = getIntent().getExtras().getString("cat");
        detdesc = getIntent().getExtras().getString("desc");
//        final String path = getIntent().getStringExtra("imagePath");
//        image = BitmapFactory.decodeFile(path);
//        String imagerece = getIntent().getParcelableExtra("image");
//        image.setImageUrl(imagerece, NetworkEventController.getInstance(context).getImageLoader());

        ImageLoader imageLoader = NetworkEventController.getInstance(context).getImageLoader();
        String bitmap = getIntent().getStringExtra("image");
        image.setImageUrl(bitmap, imageLoader);

        descrip.setText(detdesc);
        title.setText(feed);
        sourcedetail.setText(source);


    }

}
