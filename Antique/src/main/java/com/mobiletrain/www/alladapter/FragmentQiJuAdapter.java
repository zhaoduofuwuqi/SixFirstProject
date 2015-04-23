package com.mobiletrain.www.alladapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.mobiletrain.www.R;
import com.mobiletrain.www.activitybeen.FragmentqbyjData;
import com.mobiletrain.www.utils.NetUtils;

import java.util.ArrayList;

/**
 * Created by aaa on 15-4-21.
 */
public class FragmentQiJuAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<FragmentqbyjData> data;
    private LayoutInflater inflater;
    public FragmentQiJuAdapter(Context context, ArrayList<FragmentqbyjData> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public FragmentqbyjData getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_left_icom_text,null);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        FragmentqbyjData fragmentqbyjData = data.get(position);
        holder.text.setText(fragmentqbyjData.getName());
        holder.description.setText(fragmentqbyjData.getDescription());
        BitmapUtils bitmapUtils = NetUtils.getBitmapUtils(context);
        bitmapUtils.display(holder.icon,fragmentqbyjData.getCover().getImage_url());
        return convertView;
    }

    class ViewHolder{
        TextView text;
        TextView description;
        ImageView icon;
    }
}
