package com.gcme.addischurch.addischurch.JSON;


import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gcme.addischurch.addischurch.DB.DatabaseAdaptor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**This Class sync database with the server MYSQL Database**/
public class RequestJson extends JobService {
    DatabaseAdaptor DbHelper ;

    public RequestJson() {


    }

    @Override
    public boolean onStartJob(JobParameters params) {
        DbHelper = new DatabaseAdaptor(this);
        /**Sync Churchs**/
        getchurches();
        /**Sync Church Schedules**/
        getchurchschedules();


        jobFinished(params, false);
        return false;
    }

            /** This is a to insert a data into Church database from server**/
    private void getchurches() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://localchurch-001-site1.btempurl.com/api/churches";
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        try {

                    for (int i = 0; i < response.length(); i++) {


                        JSONObject Jo = (JSONObject) response.get(i);
                            String ID = Jo.getString("id");
                            String churchname = Jo.getString("name");
                            String churchlocation = Jo.getString("location");
                            String contacts = Jo.getString("phone");
                            String web = Jo.getString("weburl");
                            String sermons = " ";
                            String churchcategory = Jo.getString("denomination");
                            String longitude = Jo.getString("longitude");
                            String latitude = Jo.getString("latittude");
                            String ImageUrl = Jo.getString("image");
                            String ImageLoction = " ";


                        if(DbHelper.hasObjectChurch(ID)==false){

                            addData(ID, churchname, churchlocation, contacts, web, sermons, churchcategory, longitude, latitude, ImageLoction, ImageUrl);

                        }

                            }} catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(),
//                                    "Error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(req);

    }



    private void getchurchschedules() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://localchurch-001-site1.btempurl.com/api/schedules";
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        try {

                            for (int i = 0; i < response.length(); i++) {


                                JSONObject Jo = (JSONObject) response.get(i);
                                String ID = Jo.getString("id");
                                String churchId = Jo.getString("churchId");
                                String scheduledate = Jo.getString("date");
                                String scheduletime = Jo.getString("time");
                                String ScheduleCategory = Jo.getString("catagory");



                                if(DbHelper.hasObjectSchedule(ID)==false){

                                    addScheduleData(ID, churchId,scheduledate, scheduletime, ScheduleCategory);
                                }





                            }} catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(),
//                                    "Error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(req);

    }

    private void addScheduleData(String Id, String churchId, String scheduledate, String scheduletime, String scheduleCategory) {






        long id = DbHelper.InsertChurchSchedule(Id, churchId, scheduledate, scheduletime, scheduleCategory);
        if (id < 0) {
//            Toast.makeText(getApplicationContext(), "Scgedule JSON insert failed!", Toast.LENGTH_LONG).show();

        } else {
//            Toast.makeText(getApplicationContext(), "Scgedule JSON data successfully inserted!", Toast.LENGTH_LONG).show();

        }


    }




    private void addData(String Id, String name, String churchlocation, String contacts, String web, String sermons, String category, String longitude, String latitude, String ImageLoction, String ImageUrl) {
        {



            long id = DbHelper.InsertChurch(Id, name, churchlocation, contacts, web, sermons, category, longitude, latitude, ImageLoction, ImageUrl);
            if (id < 0) {
//                Toast.makeText(getApplicationContext(), "JSON insert failed!", Toast.LENGTH_LONG).show();

            } else {
//                Toast.makeText(getApplicationContext(), "JSON data successfully inserted!", Toast.LENGTH_LONG).show();

            }


        }


    }

    @Override
    public boolean onStopJob(JobParameters params) {

        return false;
    }

}
