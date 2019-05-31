package com.example.gcrestaurant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class ExpandableRecyclerViewAdpater extends RecyclerView.Adapter<ExpandableRecyclerViewAdpater.ViewHolder> {
    private ArrayList<QuestionItem> items = new ArrayList<QuestionItem>();

    private Context context;

    public ExpandableRecyclerViewAdpater(Context context) {
        this.context = context;
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

    public void AddWithAnimation(QuestionItem item)
    {
        items.add(0,item);
        notifyItemInserted(0);
        //expanderRecyclerView.scrollToPosition(position);
    }
    public void Add(QuestionItem item)
    {
        items.add(item);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final QuestionItem item = items.get(position);
        holder.writing.setText(item.Content);

        InnerRecyclerViewAdapter itemInnerRecyclerView = new InnerRecyclerViewAdapter(item);

        LinearLayoutManager linearVertical = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.cardRecyclerView.setLayoutManager(linearVertical);

        holder.cardView.setOnClickListener(new dropOnClickListener(position, holder) {
            @Override
            public void onClick(View view) {
                if (item.Opened == false) {
                    holder.cardRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    holder.cardRecyclerView.setVisibility(View.GONE);
                }
                item.Opened = !item.Opened;
            }
        });
        holder.cardRecyclerView.setAdapter(itemInnerRecyclerView);

        holder.dropBtn.setOnClickListener(new dropOnClickListener(position, holder) {
            @Override
            public void onClick(View v) { // 버튼 누르면 답글 추가

            }
        });
    }

    public abstract class dropOnClickListener implements View.OnClickListener { // onclicklistener로 parameter 전달
        protected int position;
        protected ViewHolder holder;

        public dropOnClickListener(int position, ViewHolder holder) {
            this.position = position;
            this.holder = holder;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
