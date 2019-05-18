package com.example.gcrestaurant;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class Menu_WaitingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Toast.makeText(getContext(), "눌림", Toast.LENGTH_LONG).show();
        return inflater.inflate(R.layout.fragment_menu_waiting,container, false);
    }
}
