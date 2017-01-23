package com.gcme.addischurch.addischurch.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Panacea-Soft on 8/2/15.
 * Contact Email : teamps.is.cool@gmail.com
 */
public class PItemDatalocal implements Parcelable {

    public String id;

    public String cat_id;

    public String name;

    public String description;

    public String address;

    public String phone;

    public String email;

    public String lat;

    public String lng;

    public String search_tag;


    public ArrayList<PImageData> images;

    protected PItemDatalocal(Parcel in) {
        id = in.readString();
        cat_id = in.readString();
        name = in.readString();
        description = in.readString();
        address = in.readString();
        phone = in.readString();
        email = in.readString();
        lat = in.readString();
        lng = in.readString();
        search_tag = "search_tag";




        if (in.readByte() == 0x01) {
            images = new ArrayList<PImageData>();
            in.readList(images, PImageData.class.getClassLoader());
        } else {
            images = null;
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(cat_id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(search_tag);

    }

    @SuppressWarnings("unused")
    public static final Creator<PItemDatalocal> CREATOR = new Creator<PItemDatalocal>() {
        @Override
        public PItemDatalocal createFromParcel(Parcel in) {
            return new PItemDatalocal(in);
        }

        @Override
        public PItemDatalocal[] newArray(int size) {
            return new PItemDatalocal[size];
        }
    };
}