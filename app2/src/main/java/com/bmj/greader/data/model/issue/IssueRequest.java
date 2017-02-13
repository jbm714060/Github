package com.bmj.greader.data.model.issue;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/12/22 0022.
 */
public class IssueRequest implements Parcelable {
    public static final Creator<IssueRequest> CREATOR = new Creator<IssueRequest>() {
        public IssueRequest createFromParcel(Parcel source) {
            return new IssueRequest(source);
        }

        public IssueRequest[] newArray(int size) {
            return new IssueRequest[size];
        }
    };
    public String title;
    public String body;

    public IssueRequest() {
    }

    protected IssueRequest(Parcel in) {
        this.title = in.readString();
        this.body = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.body);
    }
}

