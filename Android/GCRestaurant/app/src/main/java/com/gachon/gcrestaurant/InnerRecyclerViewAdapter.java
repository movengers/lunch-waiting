package com.gachon.gcrestaurant;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class InnerRecyclerViewAdapter extends RecyclerView.Adapter<InnerRecyclerViewAdapter.ViewHolder> {
    public QuestionItem parent = null;

    public InnerRecyclerViewAdapter(QuestionItem item) {
        this.parent = item;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView writing2;

        public ViewHolder(View itemView) {
            super(itemView);
            writing2 = (TextView) itemView.findViewById(R.id.child_view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_board, parent, false);

        InnerRecyclerViewAdapter.ViewHolder vh = new InnerRecyclerViewAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.writing2.setText(parent.Comment.get(position).Content);
    }

    @Override
    public int getItemCount() {
        return parent.Comment.size();
    }

}
