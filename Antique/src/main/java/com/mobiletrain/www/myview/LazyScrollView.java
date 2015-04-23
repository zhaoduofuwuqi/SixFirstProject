package com.mobiletrain.www.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by aaa on 15-4-22.
 */
public class LazyScrollView extends ScrollView {
    public LazyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LazyScrollView(Context context) {
        super(context);
    }
    public interface OnLoadListener{
        public void load();
    }

    private OnLoadListener l;

    public void setOnLoadListener(OnLoadListener l) {
        this.l = l;
    }

    public void init(){
        final View child= getChildAt(0);
        if(child==null)
            return;

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(child.getMeasuredHeight()<=getHeight()+getScrollY()){
                    //滑动到ScrollView底部
                    if(event.getAction()==MotionEvent.ACTION_UP){
                        //上拉加载
                        if(l!=null){
                            l.load();
                        }
                    }
                }
                return false;
            }
        });
    }
}
