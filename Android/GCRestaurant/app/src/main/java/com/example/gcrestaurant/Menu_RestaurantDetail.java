package com.example.gcrestaurant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONObject;

public class Menu_RestaurantDetail extends NetworkFragment{
    private ListViewAdapter adapter;
    TextView textHeart;
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
        final int no = getArguments().getInt("no");
        NetworkService.SendMessage(PacketType.RestaurantInfo,"no", String.valueOf(no));
        NetworkService.SendMessage(PacketType.GetLikes,"no", String.valueOf(no));

        NetworkService.SendMessage(PacketType.ContainsWaitingListener,"no", String.valueOf(no));
        Button request = view.findViewById(R.id.request_button);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkService.SendMessage(PacketType.RequestWaitingToServer,"no", String.valueOf(no));
            }
        });
        toggleHeart = view.findViewById(R.id.toggleButton_heart);
        toggleHeart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try
                {
                    json.put("type", PacketType.ClickLikes);
                    json.put("no", no);
                    json.put("positive", !toggleHeart.isChecked());
                }
                catch ( Exception e)
                {

                }
                NetworkService.SendMessage(json);
            }
        });
        return view;
    }
    private void heart_on()
    {
        toggleHeart.setBackgroundDrawable(
                getResources().
                        getDrawable(R.drawable.hearton)
        );
    }
    private void heart_off()
    {
        toggleHeart.setBackgroundDrawable(
                getResources().
                        getDrawable(R.drawable.heartoff)
        );
    }
    @Override
    public void ReceivePacket(JSONObject json)
    {
        try
        {
            switch (json.getInt("type"))
            {
                case PacketType.ContainsWaitingListener:
                    if (json.getBoolean("contains"))
                    {
                        SetText(R.id.request_button, "대기열 정보를 요청했습니다.");
                    }
                    break;
                case PacketType.GetLikes:
                    SetText(R.id.likes, json.getString("likes"));
                    if (json.getBoolean("positive"))
                        heart_on();
                    else
                        heart_off();
                    break;
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
