package com.bmj.greader.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import com.bmj.greader.data.net.response.Content;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public class RepoDetail implements Parcelable {
    private Repo baseRepo;
    private Content readMe;
    private ArrayList<Repo> forks;
    private ArrayList<User> contributors;

    public Repo getBaseRepo(){return baseRepo;}
    public void setBaseRepo(Repo baseRepo){this.baseRepo = baseRepo;}
    public Content getReadMe(){return readMe;}
    public void setReadMe(Content readMe){this.readMe = readMe;}
    public ArrayList<Repo> getForks(){return forks;}
    public void setForks(ArrayList<Repo> forks){this.forks = forks;}
    public ArrayList<User> getContributors(){return contributors;}
    public void setContributors(ArrayList<User> contributors){this.contributors = contributors;}

    public RepoDetail(){

    }
    public RepoDetail(Parcel in){
        this.baseRepo = in.readParcelable(Repo.class.getClassLoader());
        this.readMe = in.readParcelable(Content.class.getClassLoader());
        this.forks = in.createTypedArrayList(Repo.CREATOR);
        this.contributors = in.createTypedArrayList(User.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.baseRepo,flags);
        dest.writeParcelable(this.readMe,flags);
        dest.writeTypedList(forks);
        dest.writeTypedList(contributors);
    }

    public static final Creator<RepoDetail> CREATOR = new Creator<RepoDetail>() {
        @Override
        public RepoDetail createFromParcel(Parcel source) {
            return new RepoDetail(source);
        }

        @Override
        public RepoDetail[] newArray(int size) {
            return new RepoDetail[size];
        }
    };
}

