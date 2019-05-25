package com.example.gcrestaurant;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo("com.example.gcrestaurant", PackageManager.GET_SIGNATURES);
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
        if (packageInfo == null)
            return "데이터 없음";

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.d("A", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //프래그먼트
        SwitchView(new Menu_HomeFragment());




        //왼쪽 메뉴
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Log.d("디버그","키 : " + getKeyHash(getApplicationContext()));

        // 프로필 이름, 사진 등록
        TextView nameview = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        ImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.user_icon);
        nameview.setText(GlobalApplication.user_name);
        if (GlobalApplication.user_icon != null) {
            Glide.with(this).load(GlobalApplication.user_icon).into(imageView);
        }
        else
        {
            imageView.setImageDrawable(getDrawable(R.drawable.kakao_default_profile_image));
        }
        imageView.setBackground(new ShapeDrawable(new OvalShape()));
        imageView.setClipToOutline(true);


        //아래 메뉴
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_menu);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        SwitchView(Menu_RestaurantDetail.newInstance(36358225));
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_home:
                    Toast.makeText(getApplicationContext(),"홈",Toast.LENGTH_LONG).show();
                    SwitchView(new Menu_HomeFragment());
                    return true;
                case R.id.menu_waiting:
                    SwitchView(new Menu_WaitingFragment());
                    Toast.makeText(getApplicationContext(),"대기",Toast.LENGTH_LONG).show();
                    return true;
                case R.id.menu_ranking:
                    SwitchView(new Menu_Ranking());
                    Toast.makeText(getApplicationContext(),"랭킹",Toast.LENGTH_LONG).show();
                    return true;
                case R.id.menu_boarding:
                    SwitchView(new Menu_Board());
                    Toast.makeText(getApplicationContext(),"게시판",Toast.LENGTH_LONG).show();
                    return true;
                case R.id.menu_setting:
                    SwitchView(new Menu_Setting());
                    Toast.makeText(getApplicationContext(),"세팅",Toast.LENGTH_LONG).show();
                    return true;
            }
            return false;
        }
    };


    private void SwitchView(android.support.v4.app.Fragment fragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        try {
            transaction.replace(R.id.menu_home, fragment);
        }
        catch (Exception e)
        {
            Log.d("메뉴 이동", "에러1 : " + e.toString());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        transaction.commit();
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //왼쪽 네비게이션 bar
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_logout) {
            // 로그아웃
            UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                    NetworkService.Connect();
                    redirectLoginActivity();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
