package com.gachon.gcrestaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewHomeRestAdapter extends BaseAdapter {
    public static class Item {
        public int no;
        public String rest_name;
        public String time;
        public String distance;

        public Item(int no, String rest_name, String time, String distance) {
            this.no = no;
            this.rest_name = rest_name;
            this.time = time;
            this.distance = distance;
        }
    }

    private ArrayList<Item> listViewItemList = new ArrayList<Item>() ;

    //생성자
    public ListViewHomeRestAdapter(){

    }

    @Override
    public int getCount()  {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_home_rest_item, parent, false);
        }

        //item 참조
        TextView restNameView = convertView.findViewById(R.id.list_home_rest_name);
        TextView restTimeView = convertView.findViewById(R.id.list_home_rest_time);
        TextView restDistView = convertView.findViewById(R.id.list_home_rest_distance);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Item listViewItem = listViewItemList.get(position);

        restNameView.setText(String.valueOf(listViewItem.rest_name));
        restTimeView.setText(String.valueOf(listViewItem.time));
        restDistView.setText(String.valueOf(listViewItem.distance));

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) { return getItem(position).no ;}

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public ListViewHomeRestAdapter.Item getItem(int position) {
        return listViewItemList.get(position) ;
    }


    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Item item) {
        listViewItemList.add(item);
    }

}
