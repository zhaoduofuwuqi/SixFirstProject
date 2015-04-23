package com.mobiletrain.www.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mobiletrain.www.MainActivity;
import com.mobiletrain.www.QbyjActivity;
import com.mobiletrain.www.R;
import com.mobiletrain.www.activitybeen.FragmentqbyjCover;
import com.mobiletrain.www.activitybeen.FragmentqbyjData;
import com.mobiletrain.www.alladapter.FragmentqbyjAdapter;
import com.mobiletrain.www.alladapter.MySelectionAdapter;
import com.mobiletrain.www.utils.DataManager;
import com.mobiletrain.www.utils.NetUtils;
import com.mobiletrain.www.utils.PullToRefreshUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Fragment_qinbiyoujian extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private String uri1 = "http://www.tangpin.me/api/v2/collections?page=";
    private String uri2 = "&by_category=4&by_sorting=last_updated_at";
    int page = 1;
    private ArrayList<FragmentqbyjData> fragmentqbyjDatas;
    private FragmentqbyjAdapter fragmentqbyjAdapter;
    private PullToRefreshListView qRefreshlist;


    public static Fragment_qinbiyoujian newInstance(String param1, String param2) {
        Fragment_qinbiyoujian fragment = new Fragment_qinbiyoujian();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_qinbiyoujian() {
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
        View inflate = inflater.inflate(R.layout.fragment_qinbiyoujian, container, false);

        qRefreshlist = (PullToRefreshListView) inflate.findViewById(R.id.f_qbyj_list);

        qRefreshlist.setMode(PullToRefreshBase.Mode.BOTH);
        PullToRefreshUtils.setPullToRefreshBaseAattribute(getActivity(), qRefreshlist);
        qRefreshlist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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

        ListView qlist = qRefreshlist.getRefreshableView();
        fragmentqbyjDatas = new ArrayList<FragmentqbyjData>();
        getFrgmentqbyjData(1);
        fragmentqbyjAdapter = new FragmentqbyjAdapter(getActivity(), fragmentqbyjDatas);
        qlist.setAdapter(fragmentqbyjAdapter);
        //点击跳转详情
        qlist.setOnItemClickListener(this);
        //listview长按事件，点击后保存到数据库
        qlist.setOnItemLongClickListener(this);
        return inflate;


    }

    public void getFrgmentqbyjData(final int page) {
        HttpUtils httpUtils = NetUtils.getHttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, uri1 + page + uri2, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (page == 1) {
                    //pger为1，清空已有数据
                    fragmentqbyjDatas.clear();
                }
                //加载完后
                qRefreshlist.onRefreshComplete();
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
                        FragmentqbyjCover fragmentqbyjCover = new FragmentqbyjCover(id1, image_url);

                        FragmentqbyjData fragmentqbyjData = new FragmentqbyjData(id,
                                name, url, description, photos_count,
                                wishes_count, fans_count, comments_count,
                                is_valuable, valuation_range, last_updated_at,
                                views_count, fragmentqbyjCover);
                        fragmentqbyjDatas.add(fragmentqbyjData);

                    }
                    fragmentqbyjAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), QbyjActivity.class);
        i.putExtra("uri", fragmentqbyjDatas.get(position).getUrl());
        startActivity(i);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] strr = getResources().getStringArray(R.array.diloglist);
        final FragmentqbyjData datafrag = fragmentqbyjDatas.get(position);
        builder.setItems(strr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        DataManager.shouCang(getActivity(),fragmentqbyjDatas,position-1);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 1:
                        DataManager.showShare(getActivity(),datafrag.getUrl(),datafrag.getName());
                        break;
                }
            }
        });
        builder.show();

        //为false松开后会跳转，true不会跳转详情
        return true;
    }
}
