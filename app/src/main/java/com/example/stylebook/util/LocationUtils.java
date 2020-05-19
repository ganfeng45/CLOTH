package com.example.stylebook.util;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

/**
 * wuqingsen on 2020-04-12
 * Mailbox:1243411677@qq.com
 * annotation:
 * /**
 * * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 * * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
 * * <uses-permission android:name="android.permission.INTERNET" />
 * * targetSdkVersion 22 如果大于22请增加动态权限管理
 */
public class LocationUtils {
    private LocationManager locationManager;
    static LocationUtils locationUtils;

    public static LocationUtils getInstance() {
        if (locationUtils == null) {
            locationUtils = new LocationUtils();
        }
        return locationUtils;
    }

    public String getLocations(Context context) {
        String strLocation = "";
        DecimalFormat df = new DecimalFormat("#####0.0000");
        if (!checkPermission(context, permission.ACCESS_COARSE_LOCATION)) {
            Toast.makeText(context, "定位权限关闭，无法获取地理位置", Toast.LENGTH_SHORT).show();
        }
        try {
            //获取系统的服务，
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            //创建一个criteria对象
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            //设置不需要获取海拔方向数据
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            //设置允许产生资费
            criteria.setCostAllowed(true);
            //要求低耗电
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            String provider = locationManager.getBestProvider(criteria, true);
            Log.e("wqs", "Location Provider is " + provider);
            @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(provider);

            /**
             * 重要函数，监听数据测试
             * 位置提供器、监听位置变化的时间间隔（毫秒），监听位置变化的距离间隔（米），LocationListener监听器
             */
//           locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    lm.removeUpdates(locationListener);
//                }
//            },2000);

            //第一次获得设备的位置
            if (location != null) {
//                strLocation = df.format(location.getLatitude()) + "," + df.format(location.getLongitude());
                // 耗时操作
                strLocation += "" + convertAddress(context, location.getLatitude(), location.getLongitude());
                strLocation += "" + getLocationAddress(context, location);
            }


        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strLocation;

    }

    /**
     * @param latitude  经度
     * @param longitude 纬度
     * @return 详细位置信息 GeoCoder是基于后台backend的服务，因此这个方法不是对每台设备都适用。
     */
    public String convertAddress(Context context, double latitude, double longitude) {
        Geocoder mGeocoder = new Geocoder(context, Locale.getDefault());
        StringBuilder mStringBuilder = new StringBuilder();

        try {
            List<Address> mAddresses = mGeocoder.getFromLocation(latitude, longitude, 1);
            if (!mAddresses.isEmpty()) {
                Address address = mAddresses.get(0);
                mStringBuilder.append(address.getLocality()).append(address.getThoroughfare());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mStringBuilder.toString();
    }

    private boolean checkPermission(Context context, permission permName) {
        int perm = context.checkCallingOrSelfPermission("android.permission." + permName.toString());
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    private enum permission {
        ACCESS_COARSE_LOCATION,
        ACCESS_FINE_LOCATION
    }

    private String getLocationAddress(Context mContext, Location location) {
        String add = "";
        Geocoder geoCoder = new Geocoder(mContext, Locale.CHINESE);
        try {
            List<Address> addresses = geoCoder.getFromLocation(
                    location.getLatitude(), location.getLongitude(),
                    1);
            Address address = addresses.get(0);
            Log.e("wqs", "getLocationAddress: " + address.toString());
            // Address[addressLines=[0:"中国",1:"北京市海淀区",2:"华奥饭店公司写字间中关村创业大街"]latitude=39.980973,hasLongitude=true,longitude=116.301712]
            int maxLine = address.getMaxAddressLineIndex();
            if (maxLine >= 2) {
                add = address.getAddressLine(1) + address.getAddressLine(2);
            } else {
                add = address.getAddressLine(1);
            }
        } catch (IOException e) {
            add = "";
            e.printStackTrace();
        }
        return add;
    }
}