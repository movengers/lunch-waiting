package com.gachon.gcrestaurant;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.URL;

public abstract class NetworkFragment extends Fragment implements NetworkReceiveInterface {
    protected void SetText(@IdRes int id, String data)
    {
        if (!(data.isEmpty() || data.equals("null"))) {
            TextView textView = getView().findViewById(id);
            textView.setText(data);
        }
    }
    protected String ReadText(@IdRes int id)
    {
            TextView textView = getView().findViewById(id);
            return textView.getText().toString();
    }

    protected void ScrollTop()
    {
        MainActivity activity = (MainActivity)getActivity();
        activity.ScrollTop();
    }

    public int GetHeight()
    {
        ScrollView frameLayout = getActivity().findViewById(R.id.main_scroll);
        int height_main =  frameLayout.getMeasuredHeight();
        return height_main;
    }
    protected void SetFullHeight(@IdRes int id)
    {
        View a = getView().findViewById(R.id.map_f);
        SetFullHeight(a);
    }

    protected void SetFullHeight(View view)
    {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = GetHeight();
        view.setLayoutParams(params);
    }
    protected void SetFullHeight(View view,int height)
    {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
    }

    protected void SetImage(@IdRes int id, String URL)
    {
        if (!(URL.isEmpty() || URL.equals("null")))
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
