package com.bmj.greader.data.net.services;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/20 0020.
 */
public interface FileDownloadService {

    @Headers("Cache-Control:public,max-age=3600")
    @GET("{path}")
    Observable<ResponseBody> getGistFile(@Path("path")String fileUrl);
}
