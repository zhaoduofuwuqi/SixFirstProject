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
public class FragmentqbyjAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<FragmentqbyjData> fragmentqbyjDatas;

    public FragmentqbyjAdapter(Context context, ArrayList<FragmentqbyjData> fragmentqbyjDatas) {
        this.context = context;
        this.fragmentqbyjDatas = fragmentqbyjDatas;
    }


    @Override
    public int getCount() {

        return fragmentqbyjDatas.size();

    }

    @Override
    public FragmentqbyjData getItem(int position) {
        return fragmentqbyjDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QbyjViewHolder qholder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.for_qbyj_list, null);
            qholder = new QbyjViewHolder();
            qholder.textView = (TextView) convertView.findViewById(R.id.for_qbyj_text);
            qholder.imageView = (ImageView) convertView.findViewById(R.id.for_qbyj_image);
            qholder.textMessage = (TextView) convertView.findViewById(R.id.for_qbyj_message);
            convertView.setTag(qholder);
        } else {
            qholder = (QbyjViewHolder) convertView.getTag();
        }
        FragmentqbyjData f = fragmentqbyjDatas.get(position);
        qholder.textView.setText(f.getName());
        qholder.textMessage.setText(f.getDescription());
        BitmapUtils bitmapUtils = NetUtils.getBitmapUtils(context);
        bitmapUtils.display(qholder.imageView,f.getCover().getImage_url());
        return convertView;
    }
    class QbyjViewHolder {
        ImageView imageView;
        TextView textView;
        TextView textMessage;
    }
}
