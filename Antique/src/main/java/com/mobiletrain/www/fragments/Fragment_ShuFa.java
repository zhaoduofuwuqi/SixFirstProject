package com.mobiletrain.www.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mobiletrain.www.MainActivity;
import com.mobiletrain.www.QbyjActivity;
import com.mobiletrain.www.R;
import com.mobiletrain.www.activitybeen.FragmentUser;
import com.mobiletrain.www.activitybeen.FragmentqbyjCover;
import com.mobiletrain.www.activitybeen.FragmentqbyjData;
import com.mobiletrain.www.alladapter.FragmentsfAdapter;
import com.mobiletrain.www.utils.NetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment_ShuFa extends Fragment implements AdapterView.OnItemClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private String uri1 = "http://www.tangpin.me/api/v2/collections?page=";
    private String uri2 = "&by_category=5&by_sorting=last_updated_at";
    int page = 1;
    private ArrayList<FragmentqbyjData> fragmentsfDatas;
    private FragmentsfAdapter fragmentsfAdapter;

    public static Fragment_ShuFa newInstance(String param1, String param2) {
        Fragment_ShuFa fragment = new Fragment_ShuFa();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_ShuFa() {

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

        View inflate = inflater.inflate(R.layout.fragment_shufa, container, false);

        GridView sgrid = (GridView) inflate.findViewById(R.id.f_sf_grid);

        fragmentsfDatas = new ArrayList<FragmentqbyjData>();
        getFragmentsfData(1);
        fragmentsfAdapter = new FragmentsfAdapter(getActivity(), fragmentsfDatas);
        sgrid.setAdapter(fragmentsfAdapter);
        //点击跳转详情
        sgrid.setOnItemClickListener(this);
        return inflate;
    }

    public void getFragmentsfData(final int page){
        HttpUtils httpUtils = NetUtils.getHttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, mParam1, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

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
                        fragmentsfDatas.add(fragmentqbyjData);

                    }
                    fragmentsfAdapter.notifyDataSetChanged();
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
        i.putExtra("uri", fragmentsfDatas.get(position).getUrl());
        startActivity(i);
    }
}
