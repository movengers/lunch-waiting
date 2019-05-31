package com.example.gcrestaurant;

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
    }

    private void initiateExpander(View view) {
        qBtnAdd =  view.findViewById(R.id.qBtn);
        questionAdd = view.findViewById(R.id.edit_question);

        ArrayList<QuestionItem> items = new ArrayList<>();
        QuestionItem item;


        item = new QuestionItem("이름", "질문1", "ㅁ");
        item.Comment.add(new QuestionItem("덧글 올린사람", "덧글1", "시간"));
        items.add(item);

        item = new QuestionItem("이름", "질문1", "ㅁ");
        item.Comment.add(new QuestionItem("덧글 올린사람", "덧글1", "시간"));
        item.Comment.add(new QuestionItem("덧글 올린사람", "덧글31", "시간"));
        item.Comment.add(new QuestionItem("덧글 올린사람", "덧글12323413412", "시간"));
        items.add(item);

        item = new QuestionItem("이름", "질문1", "ㅁ");
        item.Comment.add(new QuestionItem("덧글 올린사람", "덧글12341", "시간"));
        items.add(item);


        final ExpandableRecyclerViewAdpater expandableCategoryRecyclerViewAdapter =
                new ExpandableRecyclerViewAdpater(getContext(), items);

        expanderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        expanderRecyclerView.setAdapter(expandableCategoryRecyclerViewAdapter);

        qBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = questionAdd.getText().toString(); // 질문 edittext 값 받아오기

                expandableCategoryRecyclerViewAdapter.AddWithAnimation(new QuestionItem("글 올린사람", question, "시간"));
            }
        });
    }

    @Override
    public void ReceivePacket(JSONObject json) {

    }
}
