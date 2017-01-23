package com.gcme.addischurch.addischurch.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Panacea-Soft on 18/7/15.
 * Contact Email : teamps.is.cool@gmail.com
 */
public class PImageDataLocal implements Parcelable {

    public int id;

    public int parent_id;

    public int shop_id;

    public String type;

    public String path;

    public float width;

    public float height;

    public String description;


    protected PImageDataLocal(Parcel in) {
        id = in.readInt();
        parent_id = in.readInt();
        shop_id = in.readInt();
        type = in.readString();
        path = in.readString();
        width = in.readFloat();
        height = in.readFloat();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(parent_id);
        dest.writeInt(shop_id);
        dest.writeString(type);
        dest.writeString(path);
        dest.writeFloat(width);
        dest.writeFloat(height);
        dest.writeString(description);
    }

    @SuppressWarnings("unused")
    public static final Creator<PImageDataLocal> CREATOR = new Creator<PImageDataLocal>() {
        @Override
        public PImageDataLocal createFromParcel(Parcel in) {
            return new PImageDataLocal(in);
        }

        @Override
        public PImageDataLocal[] newArray(int size) {
            return new PImageDataLocal[size];
        }
    };
}
