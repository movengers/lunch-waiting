package com.gachon.gcrestaurant;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ContentsPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment_RankingList> pages = new ArrayList<Fragment_RankingList>();
    public ContentsPagerAdapter(FragmentManager fm, String[] Categories) {
        super(fm);
        for(int i = 0 ;i < Categories.length; i ++)
        {
            pages.add(Fragment_RankingList.newInstance(Categories[i]));
        }
    }

    @Override
    public Fragment_RankingList getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
