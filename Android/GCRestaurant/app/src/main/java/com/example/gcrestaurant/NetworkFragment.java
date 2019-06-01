package com.example.gcrestaurant;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public abstract class NetworkFragment extends Fragment implements NetworkReceiveInterface {
    protected void SetText(@IdRes int id, String data)
    {
        TextView textView = getView().findViewById(id);
        textView.setText(data);
    }

    protected void SetImage(@IdRes int id, String URL)
    {
        Glide.with(this).load(URL).into((ImageView)getView().findViewById(id));
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkService.setListener(this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        NetworkService.removeListener(this);
    }

    public void SwitchView(Fragment fragment)
    {
        MainActivity activity = (MainActivity)getActivity();
        activity.SwitchView(fragment);
    }
}
