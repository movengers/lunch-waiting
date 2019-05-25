package com.example.gcrestaurant;

import android.graphics.drawable.Drawable;

public class ListViewItem {
    private Drawable iconDrawable;
    private int no;
    private String item_name_str;
    private String item_price_str;

    public void setNo(int no) {this.no = no;}
    public void setIcon(Drawable icon)
    {
        iconDrawable = icon;
    }
    public void setItemName(String item_name) { item_name_str = item_name; }
    public void setItemPrice(String item_price) {item_price_str = item_price;}
    public int getNo() {return no;}

    public Drawable getIcon()
    {
        return this.iconDrawable;
    }
    public String getItemName()
    {
        return this.item_name_str;
    }
    public String getItemPrice()
    {
        return this.item_price_str;
    }
}
