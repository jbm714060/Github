package com.bmj.greader.data.model.gist;

import android.os.Parcel;
import android.os.Parcelable;

import com.bmj.greader.common.wrapper.AppLog;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

/**
 * Created by Administrator on 2016/12/19 0019.
 "GenericCovariants.java": {
 "filename": "GenericCovariants.java",
 "type": "text/plain",
 "language": "Java",
 "raw_url": "https://gist.githubusercontent.com/JakeWharton/5b3dbbc54c779a6bc6af/raw/3bc1bf0c14fb45c94ac81f4e90c1408c8aa26552/GenericCovariants.java",
 "size": 169
 }
 */
public class GistFile implements Parcelable{

    private String filename;
    public String getFilename(){return  filename;}
    public void setFilename(String filename){this.filename = filename;}

    private String type;
    public String getType(){return  type;}
    public void setType(String type){this.type = type;}

    private String language;
    public String getLanguage(){return  language;}
    public void setLanguage(String language){this.language = language;}

    private String raw_url;
    public String getRaw_url(){return  raw_url;}
    public void setRaw_url(String language){this.raw_url = raw_url;}

    private int size;
    public int getSize(){return  size;}
    public void setSize(int size){this.size = size;}

    public GistFile(){}

    public GistFile(Parcel in){
        filename = in.readString();
        type = in.readString();
        language = in.readString();
        raw_url = in.readString();
        size = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(filename);
        parcel.writeString(type);
        parcel.writeString(language);
        parcel.writeString(raw_url);
        parcel.writeInt(size);
    }

    public static Creator<GistFile> CREATOR = new Creator<GistFile>(){

        @Override
        public GistFile createFromParcel(Parcel parcel) {
            return new GistFile(parcel);
        }

        @Override
        public GistFile[] newArray(int i) {
            return new GistFile[i];
        }
    };
}
