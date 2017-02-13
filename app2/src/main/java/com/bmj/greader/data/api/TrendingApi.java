package com.bmj.greader.data.api;

import java.util.ArrayList;

import com.bmj.greader.data.model.TrendingRepo;

import rx.Observable;

/**
 * Created by mingjun on 16/7/20.
 */
public interface TrendingApi {
    /**
     * Get trending repo base on type.
     * @param language
     * @return
     */
    Observable<ArrayList<TrendingRepo>> getTrendingRepo(String language,String time);
}
