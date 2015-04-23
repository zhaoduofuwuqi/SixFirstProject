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
import com.mobiletrain.www.utils.NetUtils;

import java.util.ArrayList;

public class FragmentsfAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<FragmentqbyjData> fragmentsfDatas;
    private final BitmapUtils bitmapUtils;

    public FragmentsfAdapter(Context context, ArrayList<FragmentqbyjData> fragmensfjDatas) {
        this.context = context;
        this.fragmentsfDatas = fragmensfjDatas;
        bitmapUtils = NetUtils.getBitmapUtils(context);
    }
    @Override
    public int getCount() {
        return fragmentsfDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return fragmentsfDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SfViewHolder sholder=null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.for_sf_grid, null);
            sholder = new SfViewHolder();
            sholder.textView = (TextView) convertView.findViewById(R.id.for_sf_text);
            sholder.imageView = (ImageView) convertView.findViewById(R.id.for_sf_image);
            convertView.setTag(sholder);
        } else {
            sholder = (SfViewHolder) convertView.getTag();
        }
        bitmapUtils.display(sholder.imageView,fragmentsfDatas.get(position).getCover().getImage_url());
        sholder.textView.setText(fragmentsfDatas.get(position).getName());
        return convertView;
    }

    class SfViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
