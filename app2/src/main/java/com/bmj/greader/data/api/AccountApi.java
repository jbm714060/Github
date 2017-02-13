package com.bmj.greader.data.api;

import com.bmj.greader.data.model.User;
import rx.Observable;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public interface AccountApi {
    Observable<User> login(String userName,String userPassword);
}
