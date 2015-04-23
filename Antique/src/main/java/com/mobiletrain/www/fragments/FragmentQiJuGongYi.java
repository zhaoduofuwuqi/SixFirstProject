package com.mobiletrain.www.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mobiletrain.www.QbyjActivity;
import com.mobiletrain.www.R;
import com.mobiletrain.www.activitybeen.FragmentUser;
import com.mobiletrain.www.activitybeen.FragmentqbyjCover;
import com.mobiletrain.www.activitybeen.FragmentqbyjData;
import com.mobiletrain.www.alladapter.FragmentQiJuAdapter;
import com.mobiletrain.www.utils.NetUtils;
import com.mobiletrain.www.utils.PullToRefreshUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentQiJuGongYi extends Fragment implements AdapterView.OnItemClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String uri1 = "http://www.tangpin.me/api/v2/collections?page=";
    private String uri2 = "&by_category=2&by_sorting=last_updated_at";
    private ArrayList<FragmentqbyjData> datas;
    private FragmentQiJuAdapter adapter;
    private int page =1;
    private PullToRefreshListView refreshListView;

    public static FragmentQiJuGongYi newInstance(String param1, String param2) {
        FragmentQiJuGongYi fragment = new FragmentQiJuGongYi();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentQiJuGongYi() {
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
        View view = inflater.inflate(R.layout.fragment_qinbiyoujian, container, false);
        refreshListView = (PullToRefreshListView) view.findViewById(R.id.f_qbyj_list);
        PullToRefreshUtils.setPullToRefreshBaseAattribute(getActivity(), refreshListView);
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                page = 1;
                getFrgmentqbyjData(page);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                getFrgmentqbyjData(page++);
            }
        });

        ListView listView = refreshListView.getRefreshableView();
        datas = new ArrayList<FragmentqbyjData>();
        getFrgmentqbyjData(page);
        adapter = new FragmentQiJuAdapter(getActivity(),datas);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        return view;
    }

    public void getFrgmentqbyjData(final int page) {
        HttpUtils httpUtils = NetUtils.getHttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET,uri1+page+uri2,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                String json = objectResponseInfo.result;
                if (page == 1) {
                    //下拉刷新时将原有数据清空
                    datas.clear();
                }
                //数据加载完后
                refreshListView.onRefreshComplete();
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
                        String cId = cover.getString("id");
                        String image_url = cover.getString("image_url");
                        FragmentqbyjCover fragmentqbyjCover = new FragmentqbyjCover(cId, image_url);

                        JSONObject user = jsonObject.getJSONObject("user");
                        String uId = user.getString("id");
                        String uName = user.getString("name");
                        String uGender = user.getString("gender");
                        String uBio = user.getString("bio");
                        String uAvatar = user.getString("avatar");
                        FragmentUser fragmentUser = new FragmentUser(uId,uName,uGender,uBio,uAvatar);

                        FragmentqbyjData fragmentqbyjData = new FragmentqbyjData(id,
                                name, url, description, photos_count,
                                wishes_count, fans_count, comments_count,
                                is_valuable, valuation_range, last_updated_at,
                                views_count, fragmentqbyjCover);
                        fragmentqbyjData.setUser(fragmentUser);
                        datas.add(fragmentqbyjData);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), QbyjActivity.class);
        i.putExtra("uri", datas.get(position).getUrl());
        startActivity(i);
    }
}
