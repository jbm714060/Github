package com.bmj.greader.data.net.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import com.bmj.greader.data.model.User;

/**
 * Created by mingjun on 16/7/18.
 */
public class SearchUserResp implements Parcelable {

    private long total_count;
    private boolean incomplete_results;
    private ArrayList<User> items;

    public long getTotal_count() {
        return total_count;
    }

    public void setTotal_count(long total_count) {
        this.total_count = total_count;
    }

    public boolean isIncomplete_results() {
        return incomplete_results;
    }

    public void setIncomplete_results(boolean incomplete_results) {
        this.incomplete_results = incomplete_results;
    }

    public ArrayList<User> getItems() {
        return items;
    }

    public void setItems(ArrayList<User> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.total_count);
        dest.writeByte(this.incomplete_results ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.items);
    }

    public SearchUserResp() {
    }

    protected SearchUserResp(Parcel in) {
        this.total_count = in.readLong();
        this.incomplete_results = in.readByte() != 0;
        this.items = in.createTypedArrayList(User.CREATOR);
    }

    public static final Creator<SearchUserResp> CREATOR = new Creator<SearchUserResp>() {
        @Override
        public SearchUserResp createFromParcel(Parcel source) {
            return new SearchUserResp(source);
        }

        @Override
        public SearchUserResp[] newArray(int size) {
            return new SearchUserResp[size];
        }
    };
}
