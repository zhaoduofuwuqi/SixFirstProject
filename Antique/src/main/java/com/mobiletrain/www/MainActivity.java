package com.mobiletrain.www;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.mobiletrain.www.activitybeen.MySelectionData;
import com.mobiletrain.www.alladapter.MySelectionAdapter;
import com.mobiletrain.www.fragments.FragmentLiveTaste;
import com.mobiletrain.www.fragments.FragmentQiJuGongYi;
import com.mobiletrain.www.fragments.FragmentZhuBao;
import com.mobiletrain.www.fragments.Fragment_ShuFa;
import com.mobiletrain.www.fragments.Fragment_ShuJiJiNian;
import com.mobiletrain.www.fragments.Fragment_qinbiyoujian;
import com.mobiletrain.www.utils.NetUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends ActionBarActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private SlidingMenu slidingMenu;
    private long lastTime;
    private ArrayList<Fragment> fragments;
    private RadioGroup radioGroup;
    private ViewPager pager;
    private ListView myselectionlist;
    private DbUtils dbUtils;
    private List<MySelectionData> mySelectionDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onCreateSlidingMenu();
        setActionBarStyle();

        fragments = new ArrayList<Fragment>();
        onCreateFragmentForPager();
        pager = (ViewPager) findViewById(R.id.pager_fragment);

        MyFragmentPager adapter = new MyFragmentPager(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);
        ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
        dbUtils = NetUtils.getDbUtils(this);

    }

    private void onCreateFragmentForPager(){
        for (int i = 0; i <9; i++) {
            Fragment f=null;
            switch (i) {
                case 0:
                    f = new Fragment_qinbiyoujian();
                    break;
                case 1:
                    f = new FragmentQiJuGongYi();
                    break;
                case 2:
                    f = new FragmentZhuBao();
                    break;
                case 3:
                    f = FragmentLiveTaste.newInstance(
                            "http://www.tangpin.me/api/v2/collections?page=1&by_category=10&by_sorting=last_updated_at"
                            ,"生活品味");
                    break;
                case 4:
                    f = FragmentLiveTaste.newInstance(
                            "http://www.tangpin.me/api/v2/collections?page=1&by_category=9&by_sorting=last_updated_at",
                            "自然万物");
                    break;
                case 5:
                    f = Fragment_ShuFa.newInstance(
                            "http://www.tangpin.me/api/v2/collections?page=1&by_category=5&by_sorting=last_updated_at","");
                    break;
                case 6:
                    f = Fragment_ShuFa.newInstance(
                            "http://www.tangpin.me/api/v2/collections?page=1&by_category=6&by_sorting=last_updated_at","");
                    break;
                case 7:
                    f = new Fragment_qinbiyoujian();
                    break;
                case 8:
                    f = new Fragment_ShuJiJiNian();
                    break;
            }
            fragments.add(f);
        }
    }


    //设置actionbar显示样式
    private void setActionBarStyle(){
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.bar_back));
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayUseLogoEnabled(true);
        bar.setDisplayShowTitleEnabled(true);
        bar.setLogo(R.drawable.appicon);
    }

    //左右侧滑菜单
    private void onCreateSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        //侧滑方式(左右)
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        slidingMenu.setMenu(R.layout.left_menu);
        slidingMenu.setSecondaryMenu(R.layout.right_menu);
        //布局宽度
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        slidingMenu.setBehindWidth(metrics.widthPixels*3/4);
        //侧滑响应方式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //阴影宽度
        slidingMenu.setShadowWidth(50);
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setBehindScrollScale(0.35f);
        //设置滑动效果
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //找到布局,设置事件监听
        radioGroup = (RadioGroup) slidingMenu.findViewById(R.id.radioGroup1);
        radioGroup.setOnCheckedChangeListener(this);
        slidingMenu.findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        slidingMenu.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });

        LinearLayout myselection = (LinearLayout) slidingMenu.findViewById(R.id.myselection);
        myselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "myselection", Toast.LENGTH_SHORT).show();
                //创建popupwindow
                PopupWindow window = new PopupWindow(MainActivity.this);
                //window宽高
                DisplayMetrics metrics1 = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics1);
                int width = metrics1.widthPixels;
                int height = metrics1.heightPixels;

                window.setWidth(width / 5 * 4);
                window.setHeight(height / 5 * 2);
                //window布局
                View view = getLayoutInflater().inflate(R.layout.myselection, null);

                myselectionlist = (ListView) view.findViewById(R.id.myselectionlist);

                try {

                    mySelectionDatas = dbUtils.findAll(MySelectionData.class);
                    MySelectionAdapter adapter = new MySelectionAdapter(MainActivity.this, mySelectionDatas);
                    myselectionlist.setAdapter(adapter);

                } catch (DbException e) {
                    e.printStackTrace();
                }
                //window外点击，取消
                window.setOutsideTouchable(true);
                //设置view
                window.setContentView(view);
                //window位置
                View v3 = findViewById(R.id.view);

                //  window.showAsDropDown(v3);
                // window.showAtLocation();
                window.showAtLocation(v3, Gravity.CENTER, 5, 10);
                slidingMenu.toggle(true);
            }
        });
    }

    /**
     * onCheckedChanged 侧滑菜单中radioGroup的监听事件
     * 用于显示Fragment(ID为 "main_fragment")
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment f = null;
        switch (checkedId) {
            case R.id.button0:
                pager.setCurrentItem(0);
                break;
            case R.id.button1:
                pager.setCurrentItem(1);
                break;
            case R.id.button2:
                pager.setCurrentItem(2);
                break;
            case R.id.button3:
                pager.setCurrentItem(3);
                break;
            case R.id.button4:
                pager.setCurrentItem(4);
                break;
            case R.id.button5:
                pager.setCurrentItem(5);
                break;
            case R.id.button6:
                pager.setCurrentItem(6);
                break;
            case R.id.button7:
                pager.setCurrentItem(7);
                break;
            case R.id.button8:
                pager.setCurrentItem(8);
                break;
        }
        if(slidingMenu.isMenuShowing()){
            slidingMenu.toggle();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        long thisTime = new Date().getTime();
        if(thisTime-lastTime<2000){
            finish();
        }else{
            lastTime = thisTime;
            Toast.makeText(this,"再次点击退出",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageSelected(int position) {
        RadioButton child = (RadioButton) radioGroup.getChildAt(position);
        child.setChecked(true);
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    class MyFragmentPager extends FragmentPagerAdapter {
        public MyFragmentPager(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
