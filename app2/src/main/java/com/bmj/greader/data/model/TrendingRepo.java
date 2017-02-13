package com.bmj.greader.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bmj.greader.R;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public class TrendingRepo implements Parcelable{
    private String ownUser;
    private String repoName;
    private String description;
    private String language;
    private String repoStarCount;
    private String repoForksCount;
    private String newStars;

    public String getOwnUser(){return ownUser;}
    public void setOwnUser(String userLogin){ownUser = userLogin;}
    public String getRepoName(){return repoName;}
    public void setRepoName(String reponame){repoName = reponame;}
    public String getLanguage(){return language;}
    public void setLanguage(String lang){language = lang;}
    public String getRepoStarCount(){return repoStarCount;}
    public void setRepoStarCount(String repostars){repoStarCount = repostars==null?"0":repostars;}
    public String getRepoForksCount(){return repoForksCount;}
    public void setRepoForksCount(String forkscount){repoForksCount = forkscount==null?"0":forkscount;}
    public String getNewStars(){return newStars;}
    public void setNewStars(String newstars){newStars = newstars;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description = description;}

    public TrendingRepo(){}
    public TrendingRepo(Parcel in){
        ownUser = in.readString();
        repoName = in.readString();
        description = in.readString();
        language = in.readString();
        repoStarCount = in.readString();
        repoForksCount = in.readString();
        newStars = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ownUser);
        dest.writeString(repoName);
        dest.writeString(description);
        dest.writeString(language);
        dest.writeString(repoStarCount);
        dest.writeString(repoForksCount);
        dest.writeString(newStars);
    }

    public static final Creator<TrendingRepo> CREATOR = new Creator<TrendingRepo>() {
        @Override
        public TrendingRepo createFromParcel(Parcel source) {
            return new TrendingRepo(source);
        }

        @Override
        public TrendingRepo[] newArray(int size) {
            return new TrendingRepo[size];
        }
    };
}
