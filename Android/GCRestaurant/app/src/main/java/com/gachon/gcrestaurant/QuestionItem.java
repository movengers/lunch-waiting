package com.gachon.gcrestaurant;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class QuestionItem {
    public int No;
    public String Time;
    public String Name;
    public String Content;
    public Boolean Opened = false;
    public List<QuestionItem> Comment = new ArrayList<>();
    public QuestionItem(int no, String Name, String Content, String Time)
    {
        this.No = no;
        this.Name = Name;
        this.Content = Content;
        this.Time = Time;
    }
    public RecyclerView comment_view;
}

