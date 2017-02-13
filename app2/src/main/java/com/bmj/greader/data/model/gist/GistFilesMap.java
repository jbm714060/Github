package com.bmj.greader.data.model.gist;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/19 0019.
 */
public class GistFilesMap extends HashMap<String,GistFile> implements Parcelable{
    public static final Parcelable.Creator<GistFilesMap> CREATOR =
            new Parcelable.Creator<GistFilesMap>() {
                public GistFilesMap createFromParcel(Parcel source) {
                    return new GistFilesMap(source);
                }

                public GistFilesMap[] newArray(int size) {
                    return new GistFilesMap[size];
                }
            };

    public GistFilesMap() {
    }

    protected GistFilesMap(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
