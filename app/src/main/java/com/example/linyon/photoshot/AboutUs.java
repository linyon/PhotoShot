package com.example.linyon.photoshot;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutUs extends Activity{
    private android.support.design.widget.TabLayout mTabs;
    private ViewPager mViewPager;
    private String[] members = {"巫孟哲","許庭章","林佑恩"};
    private ImageView img_members;
    private int[] imgIds = {
            R.mipmap.member08, R.mipmap.member31,R.mipmap.member36,
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        mTabs = (android.support.design.widget.TabLayout) findViewById(R.id.tabs);
        mTabs.addTab(mTabs.newTab().setText(members[0]));
        mTabs.addTab(mTabs.newTab().setText(members[1]));
        mTabs.addTab(mTabs.newTab().setText(members[2]));
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        initListener();
    }
    private void initListener() {
        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
    }
    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Item " + (position + 1);
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.viewpager,
                    container, false);
            container.addView(view);
            img_members = (ImageView) view.findViewById(R.id.item_img);
            TextView title = (TextView) view.findViewById(R.id.item_title);
            title.setText(members[position]);
            Log.i("imgIds",String.valueOf(position));
            img_members.setImageResource(imgIds[position]);
            return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
