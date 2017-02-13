package com.bmj.greader.common.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
public class TransitionHelper {
    /**
     * @param activity         The activity used as start for the transition.
     * @param includeStatusBar if false，状态栏将不会被添加为过渡参与者
     * @return All transition participants.
     */
    public static Pair<View, String>[] createSafeTransitionParticipants(@NonNull Activity activity, boolean
            includeStatusBar,boolean includeNavBar, @Nullable Pair... otherParticipants) {
        View decor = activity.getWindow().getDecorView();
        View statusBar = null;
        View navBar = null;
        if (includeStatusBar)
            statusBar = decor.findViewById(android.R.id.statusBarBackground);
        if(includeNavBar)
             navBar = decor.findViewById(android.R.id.navigationBarBackground);

        List<Pair> participants = new ArrayList<>(3);
        addNonNullViewToTransitionParticipants(statusBar, participants);
        addNonNullViewToTransitionParticipants(navBar, participants);
        // only add transition participants if there's at least one none-null element
        if (otherParticipants != null && !(otherParticipants.length == 1 && otherParticipants[0] == null)) {
            participants.addAll(Arrays.asList(otherParticipants));
        }
        return participants.toArray(new Pair[participants.size()]);
    }

    private static void addNonNullViewToTransitionParticipants(View view, List<Pair> participants) {
        if (view == null) {
            return;
        }
        participants.add(new Pair<>(view, view.getTransitionName()));
    }

}

