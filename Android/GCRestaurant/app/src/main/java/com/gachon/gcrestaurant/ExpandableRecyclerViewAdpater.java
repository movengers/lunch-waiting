package com.gachon.gcrestaurant;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpandableRecyclerViewAdpater extends RecyclerView.Adapter<ExpandableRecyclerViewAdpater.ViewHolder> {
    private ArrayList<QuestionItem> items = new ArrayList<QuestionItem>();

    private Context context;

    public ExpandableRecyclerViewAdpater(Context context) {
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView writing;
        Button dropBtn;
        RecyclerView cardRecyclerView;
        CardView cardView;
        Button commentAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            writing = (TextView) itemView.findViewById(R.id.board_content);
            dropBtn = itemView.findViewById(R.id.add_comment);
            cardRecyclerView = itemView.findViewById(R.id.innerRecyclerView);
            cardView = itemView.findViewById(R.id.cardView);
            commentAdd = itemView.findViewById(R.id.add_comment);
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

        holder.commentAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(context);

                ad.setTitle("댓글 달기");
                ad.setMessage(" ");

                final EditText et = new EditText(context);
                ad.setView(et);



                ad.setNegativeButton("등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String value = et.getText().toString();

                        dialogInterface.dismiss();
                    }
                });

                ad.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });



                ad.show();

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
