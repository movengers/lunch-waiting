package com.example.gcrestaurant;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class Menu_Board extends Fragment {
    RecyclerView expanderRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_board, container, false);
        expanderRecyclerView = view.findViewById(R.id.card_view);
        initiateExpander();
        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initiateExpander() {
        ArrayList<String> parentList = new ArrayList<>();
        ArrayList<ArrayList> childListHolder = new ArrayList<>();

        parentList.add("Question");
        parentList.add("Question2");
        parentList.add("Question3");
        parentList.add("Question4");
        parentList.add("Question5");
        parentList.add("Question6");
        parentList.add("Question7");
        parentList.add("Question8");

        ArrayList<String> childList = new ArrayList<>();
        childList.add("answer");
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

        ExpandableRecyclerViewAdpater expandableCategoryRecyclerViewAdapter =
                new ExpandableRecyclerViewAdpater(getActivity().getApplicationContext(), parentList,
                        childListHolder);

        expanderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        expanderRecyclerView.setAdapter(expandableCategoryRecyclerViewAdapter);
    }
}
