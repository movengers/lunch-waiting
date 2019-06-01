package com.example.gcrestaurant;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.zip.Inflater;

public class Menu_Ranking extends Fragment {
    String[] category = new String[]{"한식", "분식", "돈까스,회,일식", "치킨", "피자", "중식"};
    private Context mContext;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ContentsPagerAdapter mContentsPagerAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_ranking,container, false);
        mContext = getContext();

        mTabLayout = (TabLayout) view.findViewById(R.id.layout_tab);

        for(String i : category)
        {
            TabLayout.Tab tab = mTabLayout.newTab();
            tab.setText(i);
            mTabLayout.addTab(tab);
        }

        mViewPager = (ViewPager) view.findViewById(R.id.pager_content);
        mContentsPagerAdapter = new ContentsPagerAdapter(
                getActivity().getSupportFragmentManager(), category);

        mViewPager.setAdapter(mContentsPagerAdapter);
       // mViewPager.setCurrentItem(0);

        mViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition(),true);
                Toast.makeText(getContext(), "ㅠㅠ"+ tab.getPosition(),Toast.LENGTH_LONG).show();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Toast.makeText(getContext(), "ㅎㅎ",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Toast.makeText(getContext(), "ㅊㅊ",Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
