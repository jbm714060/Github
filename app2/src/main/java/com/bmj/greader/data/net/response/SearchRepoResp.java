package com.bmj.greader.data.net.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import com.bmj.greader.data.model.Repo;

/**
 * Created by mingjun on 16/7/18.
 */
public class SearchRepoResp implements Parcelable {

    private long total_count;
    private boolean incomplete_results;
    private ArrayList<Repo> items;

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

    public ArrayList<Repo> getItems() {
        return items;
    }

    public void setItems(ArrayList<Repo> items) {
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

    public SearchRepoResp() {
    }

    protected SearchRepoResp(Parcel in) {
        this.total_count = in.readLong();
        this.incomplete_results = in.readByte() != 0;
        this.items = in.createTypedArrayList(Repo.CREATOR);
    }

    public static final Creator<SearchRepoResp> CREATOR = new Creator<SearchRepoResp>() {
        @Override
        public SearchRepoResp createFromParcel(Parcel source) {
            return new SearchRepoResp(source);
        }

        @Override
        public SearchRepoResp[] newArray(int size) {
            return new SearchRepoResp[size];
        }
    };
}
