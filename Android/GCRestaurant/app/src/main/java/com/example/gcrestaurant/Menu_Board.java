package com.example.gcrestaurant;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Menu_Board extends NetworkFragment {
    RecyclerView expanderRecyclerView;
    ImageButton qBtnAdd;
    EditText questionAdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_board, container, false);
        expanderRecyclerView = view.findViewById(R.id.card_view);
        initiateExpander(view);
        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkService.SendMessage(PacketType.ReadBoard);
    }

    private void initiateExpander(View view) {
        qBtnAdd =  view.findViewById(R.id.qBtn);
        questionAdd = view.findViewById(R.id.edit_question);

        final ExpandableRecyclerViewAdpater expandableCategoryRecyclerViewAdapter =
                new ExpandableRecyclerViewAdpater(getContext());
        expandableCategoryRecyclerViewAdapter.notifyDataSetChanged();
        expanderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        expanderRecyclerView.setAdapter(expandableCategoryRecyclerViewAdapter);

        qBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = questionAdd.getText().toString(); // 질문 edittext 값 받아오기
                NetworkService.SendMessage(PacketType.WriteBoardItem, "content", question);

                //expandableCategoryRecyclerViewAdapter.AddWithAnimation(new QuestionItem("글 올린사람", question, "시간"));
            }
        });

    }

    @Override
    public void ReceivePacket(JSONObject json) {
        try {
            switch (json.getInt("type")) {
                case PacketType.ReadBoard: {
                    ExpandableRecyclerViewAdpater adpater = (ExpandableRecyclerViewAdpater) expanderRecyclerView.getAdapter();
                    JSONArray array = json.getJSONArray("list");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject item = array.getJSONObject(i);
                        adpater.Add(new QuestionItem(item.getString("name"), item.getString("content"), item.getString("time")));
                    }
                    adpater.notifyDataSetChanged();
                    break;
                }
                case PacketType.WriteBoardItem: {
                    ExpandableRecyclerViewAdpater adpater = (ExpandableRecyclerViewAdpater)expanderRecyclerView.getAdapter();
                    JSONObject item = json.getJSONObject("item");
                    adpater.AddWithAnimation(new QuestionItem(item.getString("name"), item.getString("content"), item.getString("time")));
                    if (item.getInt("user_id") == GlobalApplication.user_id)
                    {
                        questionAdd.setText("");
                    }
                    break;
                }
            }
        } catch (Exception e) {

        }
    }
}
