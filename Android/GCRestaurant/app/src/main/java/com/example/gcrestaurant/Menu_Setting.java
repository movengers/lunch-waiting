package com.example.gcrestaurant;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class Menu_Setting extends Fragment {
    private ListViewAdapter adapter;
    ListView listview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restaurant_detail, null) ;
        listview = view.findViewById(R.id.menu_list);

        adapter = new ListViewAdapter();
        adapter.addItem(getResources().getDrawable(R.drawable.doughnut), 1, "피자헛","삼마넌");
        adapter.addItem(getResources().getDrawable(R.drawable.doughnut), 2, "피자헛","삼마넌");
        adapter.addItem(getResources().getDrawable(R.drawable.doughnut), 3, "피자헛","삼마넌");
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);

                String titleStr =  item.getItemName();
                Drawable iconDrawable = item.getIcon();
            }
        });

        return view;
    }



}
