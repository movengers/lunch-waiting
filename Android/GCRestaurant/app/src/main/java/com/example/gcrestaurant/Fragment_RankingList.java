package com.example.gcrestaurant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Fragment_RankingList extends NetworkFragment {
    private ListViewRankingAdapter adapter;
    private ListView listView;
    private String category;
    public static Fragment_RankingList newInstance(String category)
    {
        Fragment_RankingList temp = new Fragment_RankingList();
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        temp.setArguments(bundle);
        return temp;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_ranking_list, null) ;

        listView = (ListView)view.findViewById(R.id.ranking_list);
        adapter = new ListViewRankingAdapter();

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                SwitchView(Menu_RestaurantDetail.newInstance((int)id));
            }
        });
        category = getArguments().getString("category");

        NetworkService.SendMessage(PacketType.RestaurantRankingList,"category",category);
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
    @Override
    public void ReceivePacket(JSONObject json) {
        try {
            switch (json.getInt("type")) {
                case PacketType.RestaurantRankingList:
                    if (json.getString("category").equals(category)) {
                        JSONArray array = json.getJSONArray("list");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject item = array.getJSONObject(i);

                            adapter.addItem(new ListViewRankingAdapter.Item(i + 1,
                                    item.getInt("no"),
                                    item.getString("title"),
                                    item.getInt("likes")));
                        }
                        adapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(listView);
                    }
                    break;
            }
        } catch (Exception e) {
            NetworkService.SendDebugMessage(e.toString());
        }
    }
}
