package com.gcme.addischurch.addischurch.Adapters;

/**
 * Created by buty on 11/15/16.
 */

public class EventsFeeds {

    private String imgURL, feedName;

    public EventsFeeds(String name, String imgurl) {
        this.feedName = name;
        this.imgURL = imgurl;
    }



    public String getImgURL() {
        return imgURL;
    }

    public String getFeedName() {
        return feedName;
    }



}
