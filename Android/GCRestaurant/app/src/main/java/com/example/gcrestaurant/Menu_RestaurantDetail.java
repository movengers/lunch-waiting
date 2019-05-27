package com.example.gcrestaurant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONObject;

public class Menu_RestaurantDetail extends NetworkFragment{
    private ListViewAdapter adapter;
    ToggleButton toggleHeart;

    public static Menu_RestaurantDetail newInstance(int no)
    {
        Menu_RestaurantDetail instance = new Menu_RestaurantDetail();

        Bundle bundle = new Bundle();
        bundle.putInt("no", no);
        instance.setArguments(bundle);

        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restaurant_detail, null) ;

        int no = getArguments().getInt("no");
        NetworkService.SendMessage(PacketType.RestaurantInfo,"no", String.valueOf(no));
        return view;
    }
    @Override
    public void ReceivePacket(JSONObject json)
    {
        try
        {
            switch (json.getInt("type"))
            {
                case PacketType.RestaurantInfo:
                    SetText(R.id.rest_detail_title, json.getString("title"));
                    SetText(R.id.rest_detail_category, json.getString("category"));
                    SetImage(R.id.rest_detail_image, json.getString("image"));
                    SetText(R.id.rest_detail_desc, json.getString("description"));



                    JSONArray menus = json.getJSONArray("menus");
                    ListView listview = getView().findViewById(R.id.menu_list);
                    adapter = new ListViewAdapter();
                    for (int i = 0; i < menus.length(); i++)
                    {
                        JSONObject item = menus.getJSONObject(i);
                        adapter.addItem(new  ListViewAdapter.ListViewItem(item.getString("image") ,i, item.getString("name"),item.getString("price"),item.getString("description")));
                    }
                    listview.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(listview);


                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ListViewAdapter.ListViewItem item = ( ListViewAdapter.ListViewItem) parent.getItemAtPosition(position);

                            SetText(R.id.rest_detail_menu_desc, item.desc);
                        }
                    });

                    toggleHeart = getView().findViewById(R.id.toggleButton_heart);
                    toggleHeart.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if(toggleHeart.isChecked()){
                                toggleHeart.setBackgroundDrawable(
                                        getResources().
                                                getDrawable(R.drawable.hearton)
                                );
                            }else{
                                toggleHeart.setBackgroundDrawable(
                                        getResources().
                                                getDrawable(R.drawable.heartoff)
                                );
                            }
                        }
                    });

                    break;
            }
        }
        catch (Exception e)
        {

        }
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
