package com.mobiletrain.www.alladapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.mobiletrain.www.R;
import com.mobiletrain.www.activitybeen.FragmentqbyjData;
import com.mobiletrain.www.activitybeen.MySelectionData;
import com.mobiletrain.www.utils.NetUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaa on 15-4-21.
 */
public class MySelectionAdapter extends BaseAdapter {

    private Context context;
    private List<MySelectionData> data;
    private LayoutInflater inflater;

    public MySelectionAdapter(Context context, List<MySelectionData> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(data==null){
            return  0;
        }
        return data.size();
    }

    @Override
    public MySelectionData getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.for_popupwindow, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.url = (TextView) convertView.findViewById(R.id.url);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MySelectionData mySelectionDatas = data.get(position);
        holder.name.setText(mySelectionDatas.getName());
        holder.url.setText(mySelectionDatas.getUrl());
        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView url;
    }
}
