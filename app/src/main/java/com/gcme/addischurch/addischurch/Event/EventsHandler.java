package com.gcme.addischurch.addischurch.Event;

/**
 * Created by buty on 11/27/16.
 */

public class EventsHandler {

    private String imgURL, feedName;
    private String location;
    private String churchId,longtude,latitude,catagory,description,contact,startdate,enddate;
    private String evid;



    public EventsHandler(String name,String imgurl, String cat, String desc,  String loc ) {

//        public EventsHandler(String eid,String name, String imgurl, String loc, String chId, String lng, String lat, String cat, String desc, String cont
//                , String stdate, String endate) {

       // this.evid = eid;
        this.feedName = name;
        this.imgURL = imgurl;
        this.location = loc;
//        this.churchId = chId;
//        this.longtude = lng;
//        this.latitude = lat;
        this.catagory = cat;
        this.description = desc;
//        this.contact = cont;
//        this.startdate = stdate;
//        this.enddate = endate;

    }

    public String getEvid() {
        return evid;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getFeedName() {
        return feedName;
    }

    public String getLocation() {
        return location;
    }

    public String getChurchId() {
        return churchId;
    }

    public String getEnddate() {
        return enddate;
    }

    public String getContact() {
        return contact;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getDescription() {
        return description;
    }

    public String getCatagory() {
        return catagory;
    }

    public String getLongtude() {
        return longtude;
    }

    public String getLatitude() {
        return latitude;
    }
}
