package com.example.stylebook;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.stylebook.db.Cloth;
import com.example.stylebook.db.Match;
import com.example.stylebook.service.AutoUpdateService;
import com.example.stylebook.util.LocationUtils;
import com.google.gson.Gson;

import org.litepal.LitePal;
import org.litepal.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.zelory.compressor.Compressor;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(new HomepageFragment());
                    return true;
                case R.id.navigation_closet:
                    replaceFragment(new ClosetFragment());
                    return true;
                case R.id.navigation_notifications:
                    replaceFragment(new ScheduleFragment());
                    return true;
            }
            return false;
        }
    };

    public void FragmentScheduleClick(View v) {
        try {
            BottomNavigationItemView menuItem = (BottomNavigationItemView) findViewById(R.id.navigation_notifications);
            menuItem.performClick();
        }catch (Exception e){
            Log.i(TAG,"fail to change fragment");
        }
    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.current_page, fragment);
        transaction.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent service_intent=new Intent(MainActivity.this, AutoUpdateService.class);
        //startService(service_intent);
        Log.i(TAG, "startSERVICE");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs==null){
            initDatabase();
        }
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Intent intent = getIntent();
        int data = intent.getIntExtra("fragment",0);
        //未解决回首页的切换问题
        if (data==2){
            replaceFragment(new ScheduleFragment());
        }
        else{
            replaceFragment(new HomepageFragment());
        }
        HeConfig.init("HE1905190345261281", "7fe7e27ab3f5470ca5450332fe5c8e14");
        HeConfig.switchToFreeServerNode();
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            requestLocation();
        }
    }

    private void requestLocation() {
    }
    private void initDatabase(){
        //数据库初始化
        LitePal.getDatabase();
        String loginddate = Calendar.getInstance().toString();
        Cloth[] clothes = {
                new Cloth("hoodie_red", R.drawable.hoddie_red_cotton_spring, Color.RED,"cotton",1,loginddate,"Hoodie",3),
                new Cloth("hoodie_coffee", R.drawable.hoodie_coffee_cotton_spring,Color.DKGRAY,"cotton",1,loginddate,"Hoodie",3),
                new Cloth("hoodie_oliver", R.drawable.hoodie_oliver_cotton_spring,Color.GREEN,"cotton",1,loginddate,"Hoodie",3),
                new Cloth("hoodie_white", R.drawable.hoodie_white_cotton_spring,Color.WHITE,"cotton",1,loginddate,"Hoodie",3),
                new Cloth("Coat", R.drawable.coat,Color.BLUE,"尼龙",1,loginddate,"Coat",3),
                new Cloth("Sweater", R.drawable.sweater,Color.GRAY,"羊毛",3,loginddate,"Sweater",4),
                new Cloth("Shirt", R.drawable.shirt,Color.DKGRAY,"棉",1,loginddate,"Shirt",1),
                new Cloth("Hoodie", R.drawable.hoodie,Color.WHITE,"棉",1,loginddate,"Hoodie",2),
                new Cloth("Tshirt", R.drawable.tshirt,Color.WHITE,"棉",2,loginddate,"Tshirt",1),
                new Cloth("Dress", R.drawable.dress,Color.rgb(247,237,214),"棉",1,loginddate,"Dress",2),
                new Cloth("Jeans", R.drawable.jeans,Color.BLUE,"牛仔",1,loginddate,"Jean",2),
                new Cloth("Trouser", R.drawable.trouser,Color.BLUE,"聚酯纤维",3,loginddate,"Trouser",2),
                new Cloth("Shorts", R.drawable.shorts,Color.BLACK,"牛仔",2,loginddate,"Shorts",1),
                new Cloth("Skirt", R.drawable.skirt,Color.rgb(247,237,214),"聚酯纤维",1,loginddate,"Skirt",2)};
        for (int i = 0;i<clothes.length;i++) {
            try {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), clothes[i].getImageId());
                clothes[i].setBitmap(bitmap);
                File file = new File(getResources().getResourceName(clothes[i].getImageId()));
                Bitmap compressedImageBitmap = new Compressor(this).compressToBitmap(file);
                clothes[i].setBitmap(compressedImageBitmap);
                clothes[i].save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}