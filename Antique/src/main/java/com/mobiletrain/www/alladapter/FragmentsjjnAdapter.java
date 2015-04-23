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

/**
 * Created by aaa on 15-4-20.
 */
public class FragmentsjjnAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<FragmentqbyjData> fragmentsjjnDatas;
    private final BitmapUtils bitmapUtils;

    public FragmentsjjnAdapter(Context context, ArrayList<FragmentqbyjData> fragmentsjjnDatas) {
        this.context = context;
        this.fragmentsjjnDatas = fragmentsjjnDatas;
        bitmapUtils = NetUtils.getBitmapUtils(context);
    }


    @Override
    public int getCount() {

        return fragmentsjjnDatas.size();

    }

    @Override
    public FragmentqbyjData getItem(int position) {
        return fragmentsjjnDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SbyjViewHolder sholder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.for_qbyj_list, null);
            sholder = new SbyjViewHolder();
            sholder.textView = (TextView) convertView.findViewById(R.id.for_qbyj_text);
            sholder.imageView = (ImageView) convertView.findViewById(R.id.for_qbyj_image);
            convertView.setTag(sholder);
        } else {
            sholder = (SbyjViewHolder) convertView.getTag();
        }
        bitmapUtils.display(sholder.imageView, fragmentsjjnDatas.get(position).getCover().getImage_url());
        sholder.textView.setText(fragmentsjjnDatas.get(position).getName());

        return convertView;
    }

    class SbyjViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
