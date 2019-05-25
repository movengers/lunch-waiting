package com.example.gcrestaurant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Fragment_RankingList extends Fragment {
    private ListViewRankingAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toast.makeText(getContext(),"새로운 객체 생성",Toast.LENGTH_LONG).show();
        View view = inflater.inflate(R.layout.fragment_menu_ranking_list, null) ;

        ListView listView = (ListView)view.findViewById(R.id.ranking_list);
        adapter = new ListViewRankingAdapter();
        adapter.addItem(getResources().getDrawable(R.drawable.like), 1, "맛있는 술집","51");
        adapter.addItem(getResources().getDrawable(R.drawable.like), 2, "태평 돈가스","23");
        adapter.addItem(getResources().getDrawable(R.drawable.like), 3, "피자헛","43");
        adapter.addItem(getResources().getDrawable(R.drawable.like), 4, "피자헛","54");
        adapter.addItem(getResources().getDrawable(R.drawable.like), 5, "피자헛","33");
        adapter.addItem(getResources().getDrawable(R.drawable.like), 6, "피자헛","22");
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);



        return view;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
