package com.gcme.addischurch.addischurch.Adapters;

/**
 * Created by buty on 11/15/16.
 */

public class NewsFeeds {

    private String ImageLocationid, feedName;
    private String number;

    public NewsFeeds(String name, String ImageLoc,String numberofchurches) {
        this.feedName = name;
        this.ImageLocationid = ImageLoc;
        this.number=numberofchurches;
    }

    public String getFeedName() {
        return feedName;
    }

    public String getImageLocation() {
        return ImageLocationid;

    }

    public String getnumber() {
        return number;
    }

}

//package com.gcme.addischurch.addischurch.Adapters;
//
///**
// * Created by buty on 11/15/16.
// */
//
//public class EventsFeeds {
//
//    private String imgURL, feedName;
//
//    public EventsFeeds(String name, String imgurl) {
//        this.feedName = name;
//        this.imgURL = imgurl;
//    }
//
//
//
//    public String getImgURL() {
//        return imgURL;
//    }
//
//    public String getFeedName() {
//        return feedName;
//    }
//
//
//
//}
//
//
//
