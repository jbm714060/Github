package com.bmj.greader.ui.module.user;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.webkit.URLUtil;
import android.webkit.WebView;

import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.data.model.gist.GistFile;
import com.bmj.greader.presenter.gist.GistFilePresenter;
import com.bmj.greader.ui.module.main.UserDetailActivity;
import com.bmj.mvp.lce.LceView;
import com.pddstudio.highlightjs.HighlightJsView;
import com.pddstudio.highlightjs.models.Language;
import com.pddstudio.highlightjs.models.Theme;
import com.protectsoft.webviewcode.Codeview;
import com.protectsoft.webviewcode.Settings;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.inject.Inject;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by Administrator on 2016/12/20 0020.
 */
public class FileReadPopup extends BasePopupWindow {

    public FileReadPopup(Activity activity, GistFile gistFile){
        super(activity);
        HighlightJsView webView = (HighlightJsView)findViewById(R.id.pgr_content);
        webView.setTheme(Theme.VS);
        webView.setHighlightLanguage(Language.AUTO_DETECT);
        try {
            webView.setSource(URI.create(gistFile.getRaw_url()).toURL());
        }catch(MalformedURLException e){

        }
    }

    @Override
    protected Animation initShowAnimation() {
        return getScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
    }

    @Override
    public Animation initExitAnimation() {
        return getScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.popup_gistfile_read);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.pgr_cardview);
    }
}
