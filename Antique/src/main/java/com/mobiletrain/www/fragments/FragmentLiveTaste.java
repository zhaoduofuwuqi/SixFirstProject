package com.mobiletrain.www.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mobiletrain.www.QbyjActivity;
import com.mobiletrain.www.R;
import com.mobiletrain.www.activitybeen.FragmentUser;
import com.mobiletrain.www.activitybeen.FragmentqbyjCover;
import com.mobiletrain.www.activitybeen.FragmentqbyjData;
import com.mobiletrain.www.alladapter.LiveTasteAdapter;
import com.mobiletrain.www.utils.NetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentLiveTaste extends Fragment implements AdapterView.OnItemClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ArrayList<FragmentqbyjData> data=new ArrayList<FragmentqbyjData>();
    private LiveTasteAdapter adapter;

    public static FragmentLiveTaste newInstance(String param1, String param2) {
        FragmentLiveTaste fragment = new FragmentLiveTaste();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentLiveTaste(){}
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
        View view = inflater.inflate(R.layout.fragment_live_taste, container, false);
        ((TextView)view.findViewById(R.id.livetaste_text)).setText(mParam2);
        ListView livetaste_listview = (ListView) view.findViewById(R.id.livetaste_listview);
        getData();
        adapter = new LiveTasteAdapter(getActivity(), data);
        livetaste_listview.setAdapter(adapter);
        livetaste_listview.setOnItemClickListener(this);
        return view;
    }

    //解析json
    public void getData(){
        HttpUtils http = NetUtils.getHttpUtils();
        HttpHandler<Object> send = http.send(HttpRequest.HttpMethod.GET, mParam1, new RequestCallBack<Object>() {
            @Override
            public void onSuccess(ResponseInfo<Object> objectResponseInfo) {
                String result = (String) objectResponseInfo.result;
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray entries = object.getJSONArray("entries");
                    for (int i = 0; i < entries.length(); i++) {
                        JSONObject jsonObject = entries.getJSONObject(i);

                        FragmentqbyjData fragmentqbyjData = new FragmentqbyjData();
                        fragmentqbyjData.setId(jsonObject.getString("id"));
                        fragmentqbyjData.setName(jsonObject.getString("name"));
                        fragmentqbyjData.setUrl(jsonObject.getString("url"));
                        fragmentqbyjData.setDescription(jsonObject.getString("description"));
                        fragmentqbyjData.setPhotos_count(jsonObject.getString("photos_count"));
                        fragmentqbyjData.setValuation_range(jsonObject.getString("valuation_range"));
                        fragmentqbyjData.setLast_updated_at(jsonObject.getString("last_updated_at"));

                        JSONObject cover = jsonObject.getJSONObject("cover");
                        String id1 = cover.getString("id");
                        String image_url = cover.getString("image_url");
                        FragmentqbyjCover cover1 = new FragmentqbyjCover(id1, image_url);

                        JSONObject user = jsonObject.getJSONObject("user");
                        String id2 = user.getString("id");
                        String name1 = user.getString("name");
                        String gender = user.getString("gender");
                        String bio = user.getString("bio");
                        String avatar = user.getString("avatar");
                        FragmentUser user1 = new FragmentUser(id2, name1, gender, bio, avatar);

                        fragmentqbyjData.setCover(cover1);
                        fragmentqbyjData.setUser(user1);

                        data.add(fragmentqbyjData);
                    }
                    adapter.notifyDataSetChanged();
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
        Intent intent = new Intent(getActivity(), QbyjActivity.class);
        intent.putExtra("uri",data.get(position).getUrl());
        startActivity(intent);
    }
}
