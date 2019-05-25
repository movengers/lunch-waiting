package com.example.gcrestaurant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpandableRecyclerViewAdpater extends RecyclerView.Adapter<ExpandableRecyclerViewAdpater.ViewHolder> {
    ArrayList<String> childList = new ArrayList<String>();
    ArrayList<Integer> counter = new ArrayList<Integer>();
    ArrayList<ArrayList> itemNameList = new ArrayList<ArrayList>();
    Context context;

    public ExpandableRecyclerViewAdpater(Context context, ArrayList<String> childList, ArrayList<ArrayList> itemNameList) {
        this.childList = childList;
        this.context = context;
        this.itemNameList = itemNameList;

        Log.d("답변", childList.toString());

        Log.d("답변2", itemNameList.toString());

        for(int i = 0; i < childList.size(); i++) {
            counter.add(0);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView writing;
        ImageButton dropBtn;
        RecyclerView cardRecyclerView;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            writing = (TextView) itemView.findViewById(R.id.writingParent);
            dropBtn = itemView.findViewById(R.id.ExpandBtn);
            cardRecyclerView = itemView.findViewById(R.id.innerRecyclerView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_board, parent, false);

        ExpandableRecyclerViewAdpater.ViewHolder vh = new ExpandableRecyclerViewAdpater.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d("parentToChild", String.valueOf(itemNameList.get(position)));
        Log.d("parentString", String.valueOf(childList.get(position)));

        holder.writing.setText(childList.get(position));

        InnerRecyclerViewAdapter itemInnerRecyclerView = new InnerRecyclerViewAdapter(itemNameList.get(position));
        LinearLayoutManager linearVertical = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.cardRecyclerView.setLayoutManager(linearVertical);

        holder.dropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter.get(position) % 2 == 0) {
                    holder.cardRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    holder.cardRecyclerView.setVisibility(View.GONE);
                }

                counter.set(position, counter.get(position) + 1);
            }
        });
        holder.cardRecyclerView.setAdapter(itemInnerRecyclerView);

    }

    @Override
    public int getItemCount() {
        return childList.size();
    }

}
