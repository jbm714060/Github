package com.bmj.greader.data.model.gist;

import android.os.Parcel;
import android.os.Parcelable;

import com.bmj.greader.data.model.GithubComment;
import com.bmj.greader.data.model.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/19 0019.
 {
 "url": "https://api.github.com/gists/f8c15f9113743824b694",
 "forks_url": "https://api.github.com/gists/f8c15f9113743824b694/forks",
 "commits_url": "https://api.github.com/gists/f8c15f9113743824b694/commits",
 "id": "f8c15f9113743824b694",
 "git_pull_url": "https://gist.github.com/f8c15f9113743824b694.git",
 "git_push_url": "https://gist.github.com/f8c15f9113743824b694.git",
 "html_url": "https://gist.github.com/f8c15f9113743824b694",
 "files": {
 "visibilities.sh": {
 "filename": "visibilities.sh",
 "type": "application/x-sh",
 "language": "Shell",
 "raw_url": "https://gist.githubusercontent.com/JakeWharton/f8c15f9113743824b694/raw/a97c8bf4d6631020da7a7d9412650dfdcfad45d6/visibilities.sh",
 "size": 2931
 },
 "zoutput.txt": {
 "filename": "zoutput.txt",
 "type": "text/plain",
 "language": "Text",
 "raw_url": "https://gist.githubusercontent.com/JakeWharton/f8c15f9113743824b694/raw/eed93c702796fead4fce0089355b3f8104129d6d/zoutput.txt",
 "size": 596
 }
 },
 "public": true,
 "created_at": "2016-01-08T17:41:41Z",
 "updated_at": "2016-01-08T17:44:12Z",
 "description": "A bash script to count the visibility modifiers in a jar file.",
 "comments": 0,
 "user": null,
 "comments_url": "https://api.github.com/gists/f8c15f9113743824b694/comments",
 "owner": {
 "login": "JakeWharton",
 "id": 66577,
 "avatar_url": "https://avatars.githubusercontent.com/u/66577?v=3",
 "gravatar_id": "",
 "url": "https://api.github.com/users/JakeWharton",
 "html_url": "https://github.com/JakeWharton",
 "followers_url": "https://api.github.com/users/JakeWharton/followers",
 "following_url": "https://api.github.com/users/JakeWharton/following{/other_user}",
 "gists_url": "https://api.github.com/users/JakeWharton/gists{/gist_id}",
 "starred_url": "https://api.github.com/users/JakeWharton/starred{/owner}{/repo}",
 "subscriptions_url": "https://api.github.com/users/JakeWharton/subscriptions",
 "organizations_url": "https://api.github.com/users/JakeWharton/orgs",
 "repos_url": "https://api.github.com/users/JakeWharton/repos",
 "events_url": "https://api.github.com/users/JakeWharton/events{/privacy}",
 "received_events_url": "https://api.github.com/users/JakeWharton/received_events",
 "type": "User",
 "site_admin": false
 },
 "truncated": false
 }
 */
public class Gist implements Parcelable{
    private String url;
    public String getUrl() {return url;}
    public void setUrl(String url){this.url = url;}

    private String forks_url;
    public String getForks_url(){return this.forks_url;}
    public void setForks_url(String forks_url){this.forks_url=forks_url;}

    private String commits_url;
    public String getCommits_url(){return this.commits_url;}
    public void setCommits_url(String commits_url){this.commits_url = commits_url;}

    private String id;
    public String getId(){return this.id;}
    public void setId(String id){this.id = id;}

    private String git_pull_url;
    public String getGit_pull_url(){return this.git_pull_url;}
    public void setGit_pull_url(String git_pull_url){this.git_pull_url = git_pull_url;}

    private String git_push_url;
    public String getGit_push_url(){return this.git_push_url;}
    public void setGit_push_url(String git_push_url){this.git_push_url = git_push_url;}

    private String html_url;
    public String getHtml_url(){return this.html_url;}
    public void setHtml_url(String html_url){this.html_url = html_url;}

    private GistFilesMap files;
    public GistFilesMap getFiles(){return this.files;}
    public void setFiles(GistFilesMap files){this.files = files;}

    @SerializedName("public")
    private boolean publicFlag;
    public boolean getPublicFlag(){return this.publicFlag;}
    public void setPublicFlag(boolean publicFlag){this.publicFlag = publicFlag;}

    private String created_at;
    public String getCreated_at(){return this.created_at;}
    public void setCreated_at(String created_at){this.created_at = created_at;}

    private String updated_at;
    public String getUpdated_at(){return this.updated_at;}
    public void setUpdated_at(String updated_at){this.updated_at = updated_at;}

    private String description;
    public String getDescription(){return this.description;}
    public void setDescription(String description){this.description = description;}

    private int comments;
    public int getComments(){return this.comments;}
    public void setComments(int comments){this.comments = comments;}

    private User user;
    public User getUser(){return this.user;}
    public void setUser(User user){this.user = user;}

    private String comments_url;
    public String getComments_url(){return this.comments_url;}
    public void setComments_url(String comments_url){this.comments_url = comments_url;}

    private User owner;
    public User getOwner(){return this.owner;}
    public void setOwner(User owner){this.owner = owner;}

    private boolean truncated;
    public boolean getTruncated(){return this.truncated;}
    public void setTruncated(boolean truncated){this.truncated = truncated;}

    private ArrayList<GithubComment> githubComments;
    public ArrayList<GithubComment> getGithubComments(){return this.githubComments;}
    public void setGithubComments(ArrayList<GithubComment> githubComments){this.githubComments = githubComments;}

    public boolean isStarred = false;

    public Gist(){}

    public Gist(Parcel in){
        this.url = in.readString();
        this.forks_url = in.readString();
        this.commits_url = in.readString();
        this.id = in.readString();
        this.git_pull_url = in.readString();
        this.git_push_url = in.readString();
        this.html_url = in.readString();
        this.files = in.readParcelable(GistFilesMap.class.getClassLoader());
        this.publicFlag = in.readByte()!=0;
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.description = in.readString();
        this.comments = in.readInt();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.comments_url = in.readString();
        this.owner = in.readParcelable(User.class.getClassLoader());
        this.truncated = in.readByte()!=0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.url);
        out.writeString(this.forks_url);
        out.writeString(this.commits_url);
        out.writeString(this.id);
        out.writeString(this.git_pull_url);
        out.writeString(this.git_push_url);
        out.writeString(this.html_url);
        out.writeParcelable(this.files,flags);
        out.writeByte(this.publicFlag?(byte)1:(byte)0);
        out.writeString(this.created_at);
        out.writeString(this.updated_at);
        out.writeString(this.description);
        out.writeInt(this.comments);
        out.writeParcelable(this.user,flags);
        out.writeString(this.comments_url);
        out.writeParcelable(this.owner,flags);
        out.writeByte(this.truncated?(byte)1:(byte)0);
    }

    public static Creator<Gist> CREATOR = new Creator<Gist>(){

        @Override
        public Gist createFromParcel(Parcel parcel) {
            return new Gist(parcel);
        }

        @Override
        public Gist[] newArray(int i) {
            return new Gist[i];
        }
    };
}
