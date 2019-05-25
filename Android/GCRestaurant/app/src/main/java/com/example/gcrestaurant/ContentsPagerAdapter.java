package com.example.gcrestaurant;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Toast;

public class ContentsPagerAdapter extends FragmentStatePagerAdapter {
    private int mPageCount;

    public ContentsPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Fragment_RankingList rank = new Fragment_RankingList();
                return rank;

            case 1:
                Fragment_RankingList rank2 = new Fragment_RankingList();
                return rank2;
            case 2:
                Fragment_RankingList rank3 = new Fragment_RankingList();
                return rank3;
            case 3:
                Fragment_RankingList rank4 = new Fragment_RankingList();
                return rank4;
            case 4:
                Fragment_RankingList rank5 = new Fragment_RankingList();
                return rank5;
            case 5:
                Fragment_RankingList rank6 = new Fragment_RankingList();
                return rank6;

                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return mPageCount;
    }
}
