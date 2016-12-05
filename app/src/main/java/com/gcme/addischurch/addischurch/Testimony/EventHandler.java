package com.gcme.addischurch.addischurch.Testimony;

/**
 * Created by buty on 11/27/16.
 */

public class EventHandler {

    private String imgURL, feedName;
    private String sourceNa;
    private String worth;



    public EventHandler(String name, String imgurl, String source) {
        this.feedName = name;
        this.imgURL = imgurl;
        this.sourceNa = source;
    }



    public String getImgURL() {
        return imgURL;
    }

    public String getFeedName() {
        return feedName;
    }

    public String getSource() {
        return sourceNa;
    }

    public String getWorth() {
        return worth;
    }

}
