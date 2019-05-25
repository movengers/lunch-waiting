package com.example.gcrestaurant;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;

public class Menu_Ranking extends Fragment {
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

        mTabLayout.addTab(mTabLayout.newTab().setText("한식"));
        mTabLayout.addTab(mTabLayout.newTab().setText("분식"));
        mTabLayout.addTab(mTabLayout.newTab().setText("돈까스,회,일식"));
        mTabLayout.addTab(mTabLayout.newTab().setText("치킨"));
        mTabLayout.addTab(mTabLayout.newTab().setText("피자"));
        mTabLayout.addTab(mTabLayout.newTab().setText("중국집"));

        mViewPager = (ViewPager) view.findViewById(R.id.pager_content);
        mContentsPagerAdapter = new ContentsPagerAdapter(
                getActivity().getSupportFragmentManager(), mTabLayout.getTabCount());

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

    private View createTabView(String tabName) {
        View tabView = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
        TextView txt_name = (TextView) tabView.findViewById(R.id.txt_name);
        txt_name.setText(tabName);
        return tabView;
    }
}
