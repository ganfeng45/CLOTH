package com.example.stylebook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
import com.example.stylebook.db.Cloth;
import com.example.stylebook.db.Match;
import com.google.gson.Gson;
import com.example.stylebook.MainActivity;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import id.zelory.compressor.Compressor;
import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import io.reactivex.functions.BiFunction;


public class HomepageFragment extends Fragment {
    private static final String TAG = "HomepageFragment";
    private TextView titleCity;
    private TextView degreeText;
    private LinearLayout forecastLayout;
    private String mWeatherId;
    private ScrollView weatherLayout;
    private Context mcontext;
    private ImageView weatherIcon;
    private Button refresh;
    private CardView schedule;
    private ImageView todaymatchleft,todaymatchright;
    private MainActivity mainActivity = (MainActivity) getActivity();
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.homepage_fragment,container,false);
        this.mcontext=getActivity();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        weatherLayout = (ScrollView) view.findViewById(R.id.weather_layout);
        titleCity = (TextView) view.findViewById(R.id.title_city);
        degreeText = (TextView) view.findViewById(R.id.degree_text);
        forecastLayout = (LinearLayout) view.findViewById(R.id.forecast_layout);
        weatherIcon = (ImageView) view.findViewById(R.id.weather_icon);
        todaymatchleft = (ImageView) view.findViewById(R.id.match_image_left);
        todaymatchright = (ImageView) view.findViewById(R.id.match_image_right);
        todaymatchleft.setImageResource(R.drawable.match_default1);
        todaymatchright.setImageResource(R.drawable.match_default2);
        schedule = (CardView) view.findViewById(R.id.schedule_card);
        refresh = (Button) view.findViewById(R.id.refresh_button);
        refreshWeather();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshWeather();
                Toast.makeText(mcontext, "更新天气信息成功", Toast.LENGTH_LONG).show();
            }
        });
        final ImageView randommatch = (ImageView) view.findViewById(R.id.random_match);
        shuffleMatch(randommatch);
        randommatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffleMatch(randommatch);
            }
        });
        ImageButton addButton=(ImageButton)view.findViewById(R.id.Add_Button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClosetAddActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    private Match[] defaultmatch = {
            new Match("defaultmatch1", R.drawable.match_default1),
            new Match("defaultmatch2", R.drawable.match_default2),
            new Match("match1", R.drawable.match1),
            new Match("match2", R.drawable.match2),
            new Match("match3", R.drawable.match3),
            new Match("match4", R.drawable.match4),
            new Match("match5", R.drawable.match5),
            new Match("match6", R.drawable.match6),
    };
    private void shuffleMatch(ImageView randommatch){
        Random random = new Random();
        int id=random.nextInt(8);
        randommatch.setImageResource(defaultmatch[id].getImageId());
    }
    private  void refreshWeather(){
        HeWeather.getWeather(mcontext, "CN101010100", new HeWeather.OnResultWeatherDataListBeansListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG,"Weather onError: ",throwable);
            }
            @Override
            public void onSuccess(Weather weather) {
                Log.i(TAG, " Weather onSuccess: " + new Gson().toJson(weather));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if ( Code.OK.getCode().equalsIgnoreCase(weather.getStatus()) ){
                    //此时返回数据
                    titleCity.setText(weather.getBasic().getLocation());
                    degreeText.setText(weather.getNow().getTmp()+"°C");
                    mWeatherId=weather.getNow().getCond_txt();
                    chooseIcon(mWeatherId,weatherIcon);
                } else {
                    //在此查看返回数据失败的原因
                    String status = weather.getStatus();
                    Code code = Code.toEnum(status);
                    Log.i(TAG, "failed code: " + code);
                }
            }
        });
        HeWeather.getWeatherForecast(mcontext,"CN101010100", new HeWeather.OnResultWeatherForecastBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG,"Forecast Now onError: ",throwable);
            }
            @Override
            public void onSuccess(Forecast forecast) {
                Log.i(TAG, " Forecast onSuccess: " + new Gson().toJson(forecast));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if ( Code.OK.getCode().equalsIgnoreCase(forecast.getStatus()) ){
                    //此时返回数据
                    for (int i = 0;i<forecast.getDaily_forecast().size();i++) {
                        View view = LayoutInflater.from(mcontext).inflate(R.layout.forecast_item, forecastLayout, false);
                        TextView dateText = (TextView) view.findViewById(R.id.date_text);
                        ImageView infoImage = (ImageView) view.findViewById(R.id.info_Image);
                        TextView maxText = (TextView) view.findViewById(R.id.max_text);
                        TextView minText = (TextView) view.findViewById(R.id.min_text);
                        dateText.setText(forecast.getDaily_forecast().get(i).getDate().substring(5));
                        chooseIcon(forecast.getDaily_forecast().get(i).getCond_txt_d(),infoImage);
                        maxText.setText(forecast.getDaily_forecast().get(i).getTmp_max());
                        minText.setText(forecast.getDaily_forecast().get(i).getTmp_min());
                        forecastLayout.addView(view);
                    }
                } else {
                    //在此查看返回数据失败的原因
                    String status = forecast.getStatus();
                    Code code = Code.toEnum(status);
                    Log.i(TAG, "failed code: " + code);
                }
            }
        });

    }
    private void chooseIcon(String mWeatherId,ImageView weatherIcon){
        switch (mWeatherId){
            case "晴":
                weatherIcon.setImageResource(R.drawable.weather_sunny);
                break;
            case "多云": case "阴":
                weatherIcon.setImageResource(R.drawable.weather_cloudy);
                break;
            case "小雨":
                weatherIcon.setImageResource(R.drawable.weather_rainny);
                break;
            case "雪":
                weatherIcon.setImageResource(R.drawable.weather_snow);

        }
    }

}
