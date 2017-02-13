package com.bmj.greader.data.net.pagination;

import android.support.annotation.IntDef;

/**
 * Created by Administrator on 2016/11/29 0029.
 */
public interface RelType {
    int first = 1;
    int next = 2;
    int prev = 3;
    int last = 4;

    @IntDef({first, next, prev, last})
    @interface Type{}
}
