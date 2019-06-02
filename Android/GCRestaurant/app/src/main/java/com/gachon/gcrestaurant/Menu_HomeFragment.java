package com.gachon.gcrestaurant;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Menu_HomeFragment extends NetworkFragment {
    private ListViewHomeRestAdapter adapter;
    private ListView listView;
    private Button inputButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_home, null) ;

        listView = view.findViewById(R.id.menu_rest_list);
        adapter = new ListViewHomeRestAdapter();
        listView.setAdapter(adapter);

        inputButton = view.findViewById(R.id.inputbutton);
        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final List<String> ListItems = new ArrayList<>();
                ListItems.add("없음 [0팀]");
                ListItems.add("조금 [1~3팀]");
                ListItems.add("많음 [4팀 이상]");
                final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("대기시간 정보 입력");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int pos) {
                        String selectedText = items[pos].toString();
                        Toast.makeText(getContext(), selectedText, Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("닫기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                            }
                        });

                builder.show();
            }
        });
        NetworkService.SendMessage(PacketType.ReadBoard);
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
                case PacketType.ReadBoard: {
                    JSONArray array = json.getJSONArray("list");
                    for (int i = 0; i < array.length() && i < 3; i++) {
                        JSONObject item = array.getJSONObject(i);
                        String content = item.getString("name") +  " (" + item.getString("time") + ")\n";
                        content += item.getString("content");
                            if (i == 0)
                            SetText(R.id.answer1, content);
                        if (i == 1)
                            SetText(R.id.answer2, content);
                        if (i == 2)
                            SetText(R.id.answer3, content);
                    }
                    break;
                }
                case PacketType.WriteBoardItem: {
                    JSONObject item = json.getJSONObject("item");
                    String content = item.getString("name") +  " (" + item.getString("time") + ")\n";
                    content += item.getString("content");

                    SetText(R.id.answer3, ReadText(R.id.answer2));
                    SetText(R.id.answer2, ReadText(R.id.answer1));
                    SetText(R.id.answer1, content);
                    break;
                }
            }
        } catch (Exception e) {
            NetworkService.SendDebugMessage(e.toString());
        }
    }
}
