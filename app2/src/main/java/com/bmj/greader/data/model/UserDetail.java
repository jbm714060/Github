package com.bmj.greader.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/28 0028.
 */
public class UserDetail implements Parcelable{

    private User baseUser;
    private ArrayList<User> followerList;
    private ArrayList<User> followingList;
    private ArrayList<Repo> selfRepoList;
    private ArrayList<Repo> starredRepoList;

    public void setBaseUser(User user){
        baseUser = user;
    }
    public User getBaseUser(){
        return baseUser;
    }

    public void setFollowerList(ArrayList<User> userList){
        followerList = userList;
    }
    public ArrayList<User> getFollowerList(){
        return followerList;
    }

    public void setFollowingList(ArrayList<User> userList){
        followingList = userList;
    }
    public ArrayList<User> getFollowingList(){
        return followingList;
    }

    public void setSelfRepoList(ArrayList<Repo> repoList){
        selfRepoList = repoList;
    }
    public ArrayList<Repo> getSelfRepoList(){
        return selfRepoList;
    }

    public void setStarredRepoList(ArrayList<Repo> repoList){
        starredRepoList = repoList;
    }
    public ArrayList<Repo> getStarredRepoList(){
        return starredRepoList;
    }

    public UserDetail(){}

    public UserDetail(Parcel parcel){
        baseUser = parcel.readParcelable(User.class.getClassLoader());
        followerList = parcel.createTypedArrayList(User.CREATOR);
        followingList = parcel.createTypedArrayList(User.CREATOR);
        selfRepoList = parcel.createTypedArrayList(Repo.CREATOR);
        starredRepoList= parcel.createTypedArrayList(Repo.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(baseUser,flags);
        dest.writeTypedList(followerList);
        dest.writeTypedList(followingList);
        dest.writeTypedList(selfRepoList);
        dest.writeTypedList(starredRepoList);
    }

    public static final Parcelable.Creator<UserDetail> CREATOR =
            new Parcelable.Creator<UserDetail>(){
                @Override
                public UserDetail createFromParcel(Parcel source) {
                    return new UserDetail(source);
                }

                @Override
                public UserDetail[] newArray(int size) {
                    return new UserDetail[size];
                }
            };
}
