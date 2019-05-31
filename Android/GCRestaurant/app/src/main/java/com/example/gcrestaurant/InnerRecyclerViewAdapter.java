package com.example.gcrestaurant;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class InnerRecyclerViewAdapter extends RecyclerView.Adapter<InnerRecyclerViewAdapter.ViewHolder> {
    public ArrayList<String> childList = new ArrayList<String>();

    public InnerRecyclerViewAdapter(ArrayList<String> childList) {
        this.childList = childList;
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
        Log.d("answerwritingPosition", String.valueOf(position));
        Log.d("answerwriting", childList.get(position));
        holder.writing2.setText(childList.get(position));
    }

    @Override
    public int getItemCount() {
        return childList.size();
    }

}
