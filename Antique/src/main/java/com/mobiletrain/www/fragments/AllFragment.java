package com.mobiletrain.www.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mobiletrain.www.R;
import com.mobiletrain.www.activitybeen.FragmentUser;
import com.mobiletrain.www.activitybeen.FragmentqbyjCover;
import com.mobiletrain.www.activitybeen.FragmentqbyjData;
import com.mobiletrain.www.utils.NetUtils;
import com.mobiletrain.www.utils.PullToRefreshUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllFragment extends Fragment implements AdapterView.OnItemClickListener {


    private PullToRefreshListView qRefreshlist;
    private ArrayList<FragmentqbyjData> datas;
    private int page =1;
    private String uri1="http://www.tangpin.me/api/v2/collections?page=";
    private String uri2="&by_category=0&by_sorting=last_updated_at";

    public AllFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qinbiyoujian, container, false);
        qRefreshlist = (PullToRefreshListView) view.findViewById(R.id.f_qbyj_list);

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
        datas = new ArrayList<FragmentqbyjData>();
        ListView listView = qRefreshlist.getRefreshableView();

        //点击跳转详情
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
                qRefreshlist.onRefreshComplete();
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
//                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
