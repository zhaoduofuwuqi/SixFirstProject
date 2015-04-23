package com.mobiletrain.www.utils;

import android.content.Context;
import android.net.NetworkRequest;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.mobiletrain.www.R;
import com.mobiletrain.www.activitybeen.FragmentqbyjData;
import com.mobiletrain.www.activitybeen.MySelectionData;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by aaa on 15-4-23.
 */
public class DataManager {

    //收藏方法
    public static void shouCang(Context context, ArrayList<FragmentqbyjData> fragmentqbyjDatas, int position) {
        DbUtils dbUtils = NetUtils.getDbUtils(context);
        MySelectionData mySelectionData;
        List<MySelectionData> all = null;
        String name = fragmentqbyjDatas.get(position).getName();
        String url = fragmentqbyjDatas.get(position).getUrl();
        String image_url = fragmentqbyjDatas.get(position).getCover().getImage_url();
        try {
            //从数据库中查找
            all = dbUtils.findAll(MySelectionData.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        //若为0，则数据库无数据，可以直接添加
        if (all == null) {
            mySelectionData = new MySelectionData(name, image_url, url);
        } else {
            //不为0时，则遍历已有数据
            for (int i = 0; i < all.size(); i++) {
                MySelectionData data = all.get(i);
                String findurl = data.getUrl();
                //若数据相同，则不再次添加
                if (url.equals(findurl)) {
                    Toast.makeText(context, "该数据已收藏", Toast.LENGTH_SHORT).show();
                    return;
                }
                //若数据不同，则添加
            }
            mySelectionData = new MySelectionData(name, image_url, url);
        }
        try {
            dbUtils.save(mySelectionData);
            Toast.makeText(context, "数据已添加到收藏夹", Toast.LENGTH_SHORT).show();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public static void showShare(Context context,String url,String text) {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(context.getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(context);
    }
}
