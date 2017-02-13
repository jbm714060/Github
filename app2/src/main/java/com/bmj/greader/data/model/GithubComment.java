package com.bmj.greader.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/12/19 0019.
 {
 "url": "https://api.github.com/gists/ea4982e491262639884e/comments/1665934",
 "id": 1665934,
 "user": {
 "login": "wiibaa",
 "id": 659227,
 "avatar_url": "https://avatars.githubusercontent.com/u/659227?v=3",
 "gravatar_id": "",
 "url": "https://api.github.com/users/wiibaa",
 "html_url": "https://github.com/wiibaa",
 "followers_url": "https://api.github.com/users/wiibaa/followers",
 "following_url": "https://api.github.com/users/wiibaa/following{/other_user}",
 "gists_url": "https://api.github.com/users/wiibaa/gists{/gist_id}",
 "starred_url": "https://api.github.com/users/wiibaa/starred{/owner}{/repo}",
 "subscriptions_url": "https://api.github.com/users/wiibaa/subscriptions",
 "organizations_url": "https://api.github.com/users/wiibaa/orgs",
 "repos_url": "https://api.github.com/users/wiibaa/repos",
 "events_url": "https://api.github.com/users/wiibaa/events{/privacy}",
 "received_events_url": "https://api.github.com/users/wiibaa/received_events",
 "type": "User",
 "site_admin": false
 },
 "created_at": "2016-01-08T20:54:12Z",
 "updated_at": "2016-01-08T20:54:12Z",
 "body": "Sorry for the silly question, but did you want to say \"Java **Seven** with Retrolambda\" ?\n"
 }
 */
public class GithubComment implements Parcelable{

    private String url;
    public String getUrl(){return url;}
    public void setUrl(String url){this.url = url;}

    private int id;
    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    private User user;
    public User getUser(){return user;}
    public void setUser(User user){this.user = user;}

    private String created_at;
    public String getCreated_at(){return created_at;}
    public void setCreated_at(String created_at){this.created_at = created_at;}

    private String updated_at;
    public String getUpdated_at(){return updated_at;}
    public void setUpdated_at(String updated_at){this.updated_at = updated_at;}

    private String body;
    public String getBody(){return body;}
    public void setBody(String body){this.body = body==null?"":body;}

    public GithubComment(){}

    public GithubComment(Parcel in){
        url = in.readString();
        id = in.readInt();
        user = in.readParcelable(User.class.getClassLoader());
        created_at = in.readString();
        updated_at = in.readString();
        body = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public static Parcelable.Creator<GithubComment> CREATOR = new Parcelable.Creator<GithubComment>(){

        @Override
        public GithubComment createFromParcel(Parcel parcel) {
            return new GithubComment(parcel);
        }

        @Override
        public GithubComment[] newArray(int i) {
            return new GithubComment[i];
        }
    };
}
