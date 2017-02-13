package com.bmj.greader.common.util;

import android.text.TextUtils;

import com.blankj.utilcode.utils.FileUtils;

/**
 * Created by Administrator on 2017/1/21 0021.
 */
public class FileUtil {

    public static String getNoRepeatFileName(String fileDir,String fileName){
        if(TextUtils.isEmpty(fileDir) || TextUtils.isEmpty(fileName))
            return null;

        if(fileDir.endsWith("/"))
            fileDir = fileDir.substring(0,fileDir.length()-2);
        if(!FileUtils.isFileExists(fileDir+"/"+fileName))
            return fileName;

        int i=1;
        int index = fileName.lastIndexOf(".");
        StringBuilder filename = new StringBuilder(fileName);
        filename.insert(index,"("+i+")");
        boolean isKeep = true;
        do{
            if(FileUtils.isFileExists(fileDir+"/"+filename)){
                filename.delete(0,filename.length()).append(fileName);
                filename.insert(index,"("+ ++i +")");
            }else{
                isKeep = false;
            }
        }while (isKeep);
        return filename.toString();
    }

}
