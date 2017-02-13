package com.bmj.greader.data.net.response;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public class Content extends ShaUrl implements Parcelable,Comparable<Content>{

    public static final int FILE_CODE = 1;
    public static final int FILE_MEDIA = 2;
    public static final int FILE_TEXT= 3;
    public static final int FILE_BINARY = 5;
    public static final int FILE_PDF = 4;
    public static final int FILE_ZIP = 6;
    public static final int FILE_EXE = 7;

    public ContentType type;
    public int size;
    public String name;
    public String content;
    public String path;
    public String git_url;
    public Links _links;
    public String encoding;
    public String download_url;
    public List<Content> children;
    public Content parent;

    public Content(){}

    public void setType(ContentType type){this.type = type;}
    public void setName(String name){this.name=name;}

    public Content(Parcel in){
        super(in);
        int tmpType = in.readInt();
        type = tmpType!= -1?ContentType.values()[tmpType]:null;
        size = in.readInt();
        name = in.readString();
        content = in.readString();
        path = in.readString();
        git_url = in.readString();
        _links = in.readParcelable(Links.class.getClassLoader());
        encoding = in.readString();
        children = in.createTypedArrayList(CREATOR);
        parent = in.readParcelable(this.getClass().getClassLoader());
        download_url = in.readString();
    }

    public boolean isDir(){
        return ContentType.dir.equals(type);
    }
    public boolean isFile(){return ContentType.file.equals(type);}
    public boolean isSubmodule(){return ContentType.symlink.equals(type);}
    public int getFileType(){
        if(TextUtils.isEmpty(name))
            return FILE_BINARY;

        int index = name.lastIndexOf(".");
        if(index < 0)
            return FILE_BINARY;

        String ext = name.substring(index+1);
        if(TextUtils.isEmpty(ext))
            return FILE_BINARY;

        if(ext.equals("cpp") ||
                ext.equals("java") ||
                ext.equals("c") ||
                ext.equals("h") ||
                ext.equals("js") ||
                ext.equals("html") ||
                ext.equals("css") ||
                ext.equals("m") ||
                ext.equals("cc") ||
                ext.equals("php"))
            return FILE_CODE;
        else if(ext.equals("md") ||
                ext.equals("xml") ||
                ext.equals("pro") ||
                ext.equals("gradle") ||
                ext.equals("json") ||
                ext.equals("init") ||
                ext.equals("sh") ||
                ext.equals("conf"))
            return FILE_TEXT;
        else if(ext.equals("gif") ||
                ext.equals("png") ||
                ext.equals("jpg") ||
                ext.equals("ico"))
            return FILE_MEDIA;
        else if(ext.equals("rar") ||
                ext.equals("jar") ||
                ext.equals("zip"))
            return FILE_ZIP;
        else if(ext.equals("pdf"))
            return FILE_PDF;
        else if(ext.equals("apk") ||
                ext.equals("exe"))
            return FILE_EXE;
        else
            return FILE_BINARY;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeInt(this.type==null?-1:type.ordinal());
        dest.writeInt(size);
        dest.writeString(name);
        dest.writeString(content);
        dest.writeString(path);
        dest.writeString(git_url);
        dest.writeParcelable(_links,flags);
        dest.writeString(encoding);
        dest.writeTypedList(children);
        dest.writeParcelable(parent,flags);
        dest.writeString(download_url);
    }

    public static Creator<Content> CREATOR = new Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel source) {
            return new Content(source);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };

    @Override
    public int compareTo(Content another) {
        if(isDir()){
            return another.isDir()?-name.compareTo(another.name):1;
        }else if(another.isDir())
            return -1;
        return -name.compareTo(another.name);  //目录最大
    }
}
