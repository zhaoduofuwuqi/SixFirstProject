package com.mobiletrain.www.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
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
import com.mobiletrain.www.utils.NetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentZhuBao#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentZhuBao extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private ArrayList<FragmentqbyjData> datas;
    private MyAdapter adapter;
    private String uri1 = "http://www.tangpin.me/api/v2/collections?page=";
    private String uri2 = "&by_category=3&by_sortin%20g=last_updated_at";
    private int page = 1;

    public static FragmentZhuBao newInstance(String param1, String param2) {
        FragmentZhuBao fragment = new FragmentZhuBao();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentZhuBao() {
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
        View view = inflater.inflate(R.layout.fragment_zhubao, container, false);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_refersh);
        refreshLayout.setOnRefreshListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        GridLayoutManager gmgr = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gmgr);
        datas = new ArrayList<FragmentqbyjData>();
        adapter = new MyAdapter(new MyOnItemClickListener() {
            @Override
            public void setMyOnItemClickListener(View v, String uri) {
                Intent i = new Intent(getActivity(), QbyjActivity.class);
                i.putExtra("uri",uri);
                startActivity(i);
            }
        });
        recyclerView.setAdapter(adapter);
        getFrgmentqbyjData(page);
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
                refreshLayout.setRefreshing(false);
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
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        getFrgmentqbyjData(1);
        page=1;
    }

    interface MyOnItemClickListener{
        void setMyOnItemClickListener(View v,String uri);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        private MyOnItemClickListener listener;
        public MyAdapter(){
            super();
        }
        public MyAdapter(MyOnItemClickListener listener){
            super();
            this.listener = listener;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().
                    inflate(R.layout.list_zhubao_icon, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final FragmentqbyjData f = datas.get(position);

            BitmapUtils bitmapUtils = NetUtils.getBitmapUtils(getActivity());
            bitmapUtils.display(holder.image, f.getCover().getImage_url());

            holder.name.setText(f.getName());
            holder.valuation_range.setText("估价:"+f.getValuation_range());
            holder.user_name.setText("持宝人:"+f.getUser().getName());
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.setMyOnItemClickListener(v, f.getUrl());
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            return datas.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;
        TextView valuation_range;
        TextView user_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            valuation_range = (TextView) itemView.findViewById(R.id.valuation_range);
        }
    }
}
