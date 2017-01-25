package com.gcme.addischurch.addischurch.Event;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gcme.addischurch.addischurch.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainEvent extends AppCompatActivity {
    Spinner spinner;
    RequestQueue queue;
    String url = "http://localchurch-001-site1.btempurl.com/api/churchEvents";
    String locationurl = "http://localchurch-001-site1.btempurl.com/api/churchEvents";

    RecyclerView recyclerView;
    List<EventsHandler> feedsList = new ArrayList<EventsHandler>();
    RecyclerEventAdapter adapter;
    ArrayList<String> spinner_data;
    ArrayList<String> spinner_data_bylocation;
    private JSONArray result;
    public static final String JSON_ARRAY = "result";
    String selectedLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_event);
        spinner_data= new ArrayList<String>();
        spinner_data_bylocation= new ArrayList<String>();
        spinner = (Spinner) findViewById(R.id.spinner);
        getspinnerData();
        //Initialize RecyclerView
        getEventData();
spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {



        selectedLocation=arg0.getItemAtPosition(arg2).toString();
        Toast.makeText(arg0.getContext(),arg0.getItemAtPosition(arg2).toString(), Toast.LENGTH_LONG).show();




        JsonArrayRequest filterReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject obj = response.getJSONObject(i);
                        String eventlocation = obj.getString("location");

                        if(eventlocation == selectedLocation) {

                            EventsHandler feeds = new EventsHandler(obj.getString("name"), obj.getString("banner"),obj.getString("catagory"));

                            // adding movie to movies array
                            feedsList.add(feeds);


                        }


                    } catch (Exception e) {
                        System.out.println(e.getMessage());

                    } finally {
                        //Notify adapter about data changes
                        adapter.notifyItemChanged(i);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
        //Adding JsonArrayRequest to Request Queue
        queue.add(filterReq);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
});


    }

    private void getEventData() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        adapter = new RecyclerEventAdapter(this, feedsList);
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);
        //Getting Instance of Volley Request Queue
        queue = NetworkEventController.getInstance(this).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        JsonArrayRequest newsReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject obj = response.getJSONObject(i);
                        EventsHandler feeds = new EventsHandler(obj.getString("name"), obj.getString("banner"),obj.getString("catagory"));

                        // adding movie to movies array
                        feedsList.add(feeds);

                    } catch (Exception e) {
                        System.out.println(e.getMessage());

                    } finally {
                        //Notify adapter about data changes
                        adapter.notifyItemChanged(i);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
        //Adding JsonArrayRequest to Request Queue
        queue.add(newsReq);



    }

    private void getspinnerData() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest reqloc = new JsonArrayRequest(locationurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        try {

                            for (int i = 0; i < response.length(); i++) {


                                JSONObject Jo = (JSONObject) response.get(i);
                                String eventlocation = Jo.getString("location");



                                if (spinner_data.contains(eventlocation)) {

                                } else {
                                    spinner_data.add(eventlocation);
                                }


                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }




                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item ,spinner_data );
                requestQueue.add(reqloc);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);


    }













//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//
//        selectedLocation=parent.getItemAtPosition(position).toString();
//        Toast.makeText(parent.getContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
//
//
//
//
//        JsonArrayRequest filterReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//
//                for (int i = 0; i < response.length(); i++) {
//                    try {
//
//                        JSONObject obj = response.getJSONObject(i);
//                        String eventlocation = obj.getString("location");
//
//                        if(eventlocation == selectedLocation) {
//
//                            EventsHandler feeds = new EventsHandler(obj.getString("name"), obj.getString("banner"),obj.getString("catagory"));
//
//                            // adding movie to movies array
//                            feedsList.add(feeds);
//
//
//                        }
//
//
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//
//                    } finally {
//                        //Notify adapter about data changes
//                        adapter.notifyItemChanged(i);
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                System.out.println(error.getMessage());
//            }
//        });
//        //Adding JsonArrayRequest to Request Queue
//        queue.add(filterReq);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////        switch (position) {
////            case 0:
//
//                //                break;
////            case 1:
////                Toast.makeText(parent.getContext(), "Spinner item 2!", Toast.LENGTH_SHORT).show();
////                break;
////            case 2:
////                Toast.makeText(parent.getContext(), "Spinner item 3!", Toast.LENGTH_SHORT).show();
////                break;
////        }
//
//
//
//
//
//
//
//    }

//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private void getlocations() {



    }


}