package com.gachon.gcrestaurant;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONObject;

public class Menu_Ranking extends NetworkFragment {
    String[] category = new String[]{"한식", "분식", "돈까스,회,일식", "치킨", "피자", "중식"};
    private Context mContext;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ContentsPagerAdapter mContentsPagerAdapter;

    private boolean new_ = true;
    @Override
    public void onStart()
    {
        super.onStart();
        if (new_ == true)
        {
            new_ = false;
        }
        else
        {
            MainActivity a = (MainActivity)getActivity();
            a.SwitchView(new Menu_Ranking(), true);
        }
    }

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

        SetFullHeight(mViewPager, GetHeight() - 170);

        mContentsPagerAdapter = new ContentsPagerAdapter(
                getActivity().getSupportFragmentManager(), category);

        mViewPager.setAdapter(mContentsPagerAdapter);

        mViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition(),true);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        return view;
    }

    @Override
    public void ReceivePacket(JSONObject json) {

    }
}
