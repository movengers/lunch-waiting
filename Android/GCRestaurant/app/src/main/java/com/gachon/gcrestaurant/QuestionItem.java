package com.gachon.gcrestaurant;

import java.util.ArrayList;
import java.util.List;

public class QuestionItem {
    public String Time;
    public String Name;
    public String Content;
    public Boolean Opened = false;
    public List<QuestionItem> Comment = new ArrayList<>();
    public QuestionItem(String Name, String Content, String Time)
    {
        this.Name = Name;
        this.Content = Content;
        this.Time = Time;
    }
}

