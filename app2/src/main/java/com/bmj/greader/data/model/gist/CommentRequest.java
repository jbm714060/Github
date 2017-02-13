package com.bmj.greader.data.model.gist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/12/21 0021.
 */

public class CommentRequest implements Parcelable {

    public static final Parcelable.Creator<CommentRequest> CREATOR =
            new Parcelable.Creator<CommentRequest>() {
                public CommentRequest createFromParcel(Parcel source) {
                    return new CommentRequest(source);
                }

                public CommentRequest[] newArray(int size) {
                    return new CommentRequest[size];
                }
            };
    public String body;

    public CommentRequest(String body) {
        this.body = body;
    }

    protected CommentRequest(Parcel in) {
        this.body = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
    }
}
