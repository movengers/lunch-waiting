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

import java.util.ArrayList;

public class Menu_Board extends Fragment {
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
        final ArrayList<String> parentList = new ArrayList<>();
        final ArrayList<ArrayList> childListHolder = new ArrayList<>();
        qBtnAdd =  view.findViewById(R.id.qBtn);
        questionAdd = view.findViewById(R.id.edit_question);

        parentList.add("Question");
        parentList.add("Question2");
        parentList.add("Question3");
        parentList.add("Question4");
        parentList.add("Question5");
        parentList.add("Question6");
        parentList.add("Question7");
        parentList.add("Question8");

        ArrayList<String> childList = new ArrayList<>();
        childList.add("answer1");
        childList.add("answer1-2");
        childList.add("answer1-3");

        childListHolder.add(childList);

        childList = new ArrayList<>();
        childList.add("answer2");
        childList.add("answer2-2");
        childList.add("answer2-3");

        childListHolder.add(childList);

        childList = new ArrayList<>();
        childList.add("answer3");
        childList.add("answer3-2");
        childList.add("answer3-3");

        childListHolder.add(childList);

        childList = new ArrayList<>();
        childList.add("answer4");
        childList.add("answer4-2");
        childList.add("answer4-3");

        childListHolder.add(childList);

        childList = new ArrayList<>();
        childList.add("answer5");
        childList.add("answer5-2");
        childList.add("answer5-3");

        childListHolder.add(childList);

        childList = new ArrayList<>();
        childList.add("answer6");
        childList.add("answer6-2");
        childList.add("answer6-3");

        childListHolder.add(childList);

        childList = new ArrayList<>();
        childList.add("answer7");
        childList.add("answer7-2");
        childList.add("answer7-3");

        childListHolder.add(childList);

        childList = new ArrayList<>();
        childList.add("answer8");
        childList.add("answer8-2");
        childList.add("answer8-3");

        childListHolder.add(childList);

        final ExpandableRecyclerViewAdpater expandableCategoryRecyclerViewAdapter =
                new ExpandableRecyclerViewAdpater(getActivity().getApplicationContext(), parentList,
                        childListHolder);

        expanderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        expanderRecyclerView.setAdapter(expandableCategoryRecyclerViewAdapter);

        qBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = 0;
                ArrayList<String> childList = new ArrayList<>(); // 댓글 arrayList

                String question = questionAdd.getText().toString(); // 질문 edittext 값 받아오기
                parentList.add(position, "" + question); // 질문 parentList에 추가
                childListHolder.add(position, childList); // 빈 댓글 arrayList 추가

                expandableCategoryRecyclerViewAdapter.notifyItemInserted(position);
                expanderRecyclerView.scrollToPosition(position);
            }
        });
    }
}
