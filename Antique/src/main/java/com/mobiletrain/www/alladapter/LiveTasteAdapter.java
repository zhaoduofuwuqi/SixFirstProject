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
import com.mobiletrain.www.activitybeen.FragmentUser;
import com.mobiletrain.www.activitybeen.FragmentqbyjCover;
import com.mobiletrain.www.activitybeen.FragmentqbyjData;
import com.mobiletrain.www.utils.NetUtils;

import java.util.ArrayList;

/**
 * Created by aaa on 15-4-20.
 */
public class LiveTasteAdapter extends BaseAdapter{

    /**
     * 适配器
     */
    private Context context;
    private ArrayList<FragmentqbyjData> data;
    private LayoutInflater inflater;

    public LiveTasteAdapter(){}
    public LiveTasteAdapter(Context context, ArrayList<FragmentqbyjData> data) {
        this.context = context;
        this.data = data;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (data!=null){
            return data.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
                 ViewHolder holder=null;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.livetaste,null);
            holder=new ViewHolder();
            holder.livetaste_content_name= (TextView) convertView.findViewById(R.id.livetaste_content_name);
            holder.livetaste_content_description= (TextView) convertView.findViewById(R.id.livetaste_content_description);
            holder.livetaste_content_valuation_range= (TextView) convertView.findViewById(R.id.livetaste_content_valuation_range);
            holder.livetaste_content_image= (ImageView) convertView.findViewById(R.id.livetaste_content_image);
            holder.livetaste_content_avatar= (ImageView) convertView.findViewById(R.id.livetaste_content_avatar);
            holder.livetaste_content_user_name= (TextView) convertView.findViewById(R.id.livetaste_content_user_name);
            holder.livetaste_content_gender= (TextView) convertView.findViewById(R.id.livetaste_content_gender);
            holder.livetaste_content_bio= (TextView) convertView.findViewById(R.id.livetaste_content_bio);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        FragmentqbyjData entries = (FragmentqbyjData) getItem(position);
        holder.livetaste_content_name.setText("商品:"+entries.getName());
        holder.livetaste_content_description.setText("描述："+entries.getDescription());
        holder.livetaste_content_valuation_range.setText("估价："+entries.getValuation_range());
        FragmentqbyjCover cover = entries.getCover();
        FragmentUser user = entries.getUser();
        //加载图片
        BitmapUtils bitmapUtils = NetUtils.getBitmapUtils(context);
        bitmapUtils.display(holder.livetaste_content_image,cover.getImage_url());


        //加载图片（用户图标）
        bitmapUtils.display(holder.livetaste_content_avatar,user.getAvatar());

        holder.livetaste_content_user_name.setText("用户："+user.getName());
        holder.livetaste_content_gender.setText("性别："+user.getGender());
        holder.livetaste_content_bio.setText("签名："+user.getBio());

        return convertView;
    }
    class ViewHolder {
        TextView livetaste_content_name;
        TextView livetaste_content_description;
        TextView livetaste_content_valuation_range;
        ImageView livetaste_content_image;
        TextView livetaste_content_laizi;
        ImageView livetaste_content_avatar;
        TextView livetaste_content_user_name;
        TextView livetaste_content_gender;
        TextView livetaste_content_bio;
    }
}
