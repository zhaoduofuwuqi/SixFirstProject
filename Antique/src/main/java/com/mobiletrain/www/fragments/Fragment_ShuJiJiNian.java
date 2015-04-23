package com.mobiletrain.www.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mobiletrain.www.QbyjActivity;
import com.mobiletrain.www.R;
import com.mobiletrain.www.activitybeen.FragmentqbyjCover;
import com.mobiletrain.www.activitybeen.FragmentqbyjData;
import com.mobiletrain.www.alladapter.FragmentsjjnAdapter;
import com.mobiletrain.www.utils.NetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment_ShuJiJiNian extends Fragment implements AdapterView.OnItemClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ArrayList<FragmentqbyjData> fragmentsjjnDatas;
    private FragmentsjjnAdapter fragmentsjjnAdapter;
    private String uri1 = "http://www.tangpin.me/api/v2/collections?page=";
    private String uri2 = "&by_category=8&by_sortin";
    private int page = 1;
    private PullToRefreshListView sRefreshlist;


    public static Fragment_ShuJiJiNian newInstance(String param1, String param2) {
        Fragment_ShuJiJiNian fragment = new Fragment_ShuJiJiNian();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_ShuJiJiNian() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shujijinian, container, false);
        //找到下拉刷新组件
        sRefreshlist = (PullToRefreshListView) view.findViewById(R.id.f_sjjn_list);

        sRefreshlist.setMode(PullToRefreshBase.Mode.BOTH);
        ILoadingLayout loadingLayoutProxy = sRefreshlist.getLoadingLayoutProxy();
        Drawable drawable = getResources().getDrawable(R.drawable.refresh);
        loadingLayoutProxy.setLoadingDrawable(drawable);
        loadingLayoutProxy.setPullLabel("下拉刷新");
        loadingLayoutProxy.setRefreshingLabel("正在刷新");
        loadingLayoutProxy.setReleaseLabel("放开刷新");
        sRefreshlist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                page = 1;
                getFrgmentsjjnData(page);
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                getFrgmentsjjnData(page++);
            }
        });

        ListView slist = sRefreshlist.getRefreshableView();

        fragmentsjjnDatas = new ArrayList<>();
        getFrgmentsjjnData(1);
        fragmentsjjnAdapter = new FragmentsjjnAdapter(getActivity(), fragmentsjjnDatas);
        slist.setAdapter(fragmentsjjnAdapter);
        //listview 监听事件
        slist.setOnItemClickListener(this);
        return view;
    }

    //下载listview的数据
    public void getFrgmentsjjnData(final int page) {
        HttpUtils httpUtils = NetUtils.getHttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, uri1 + page + uri2, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (page == 1) {
                    //下拉刷新时将原有数据清空
                    fragmentsjjnDatas.clear();
                }
                //数据加载完后
                sRefreshlist.onRefreshComplete();
                String json = responseInfo.result;
                try {
                    JSONObject object = new JSONObject(json);

                    JSONArray entries = object.getJSONArray("entries");

                    for (int i = 0; i < entries.length(); i++) {
                        JSONObject jsonObject = entries.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String url = jsonObject.getString("url");
                        String description = jsonObject.getString("description");
                        String photos_count = jsonObject.getString("photos_count");
                        String wishes_count = jsonObject.getString("wishes_count");
                        String fans_count = jsonObject.getString("fans_count");
                        String comments_count = jsonObject.getString("comments_count");
                        String is_valuable = jsonObject.getString("is_valuable");
                        String valuation_range = jsonObject.getString("valuation_range");
                        String last_updated_at = jsonObject.getString("last_updated_at");
                        String views_count = jsonObject.getString("views_count");


                        JSONObject cover = jsonObject.getJSONObject("cover");

                        String id1 = cover.getString("id");
                        String image_url = cover.getString("image_url");
                        FragmentqbyjCover fragmentsjjnCover = new FragmentqbyjCover(id1, image_url);

                        FragmentqbyjData fragmentsjjnData = new FragmentqbyjData(id,
                                name, url, description, photos_count,
                                wishes_count, fans_count, comments_count,
                                is_valuable, valuation_range, last_updated_at,
                                views_count, fragmentsjjnCover);
                        fragmentsjjnDatas.add(fragmentsjjnData);

                    }
                    fragmentsjjnAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    //点击跳转详情
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), QbyjActivity.class);
        i.putExtra("uri", fragmentsjjnDatas.get(position).getUrl());
        startActivity(i);
    }
}
