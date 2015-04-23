package com.mobiletrain.www.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.mobiletrain.www.R;

/**
 * Created by aaa on 15-4-21.
 */
public class PullToRefreshUtils {

    public static void setPullToRefreshBaseAattribute(Context context,PullToRefreshBase qRefreshlist) {
        qRefreshlist.setMode(PullToRefreshBase.Mode.BOTH);
        ILoadingLayout loadingLayoutProxy = qRefreshlist.getLoadingLayoutProxy();
        Drawable drawable = context.getResources().getDrawable(R.drawable.refresh);
        loadingLayoutProxy.setLoadingDrawable(drawable);
        loadingLayoutProxy.setPullLabel("下拉刷新");
        loadingLayoutProxy.setRefreshingLabel("正在刷新");
        loadingLayoutProxy.setReleaseLabel("放开刷新");
    }
}
