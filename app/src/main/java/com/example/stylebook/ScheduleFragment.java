package com.example.stylebook;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stylebook.db.Cloth;
import com.example.stylebook.db.Match;
import com.example.stylebook.db.MatchAdapter;
import com.example.stylebook.gson.Weather;
import com.example.stylebook.service.AutoUpdateService;
import com.example.stylebook.util.AlwaysMarqueeTextView;
import com.example.stylebook.util.HttpUtil;
import com.example.stylebook.util.Utility;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ScheduleFragment extends Fragment {
    private float mDensity;
    private int mHiddenViewMeasuredHeight;
    private ImageView miv;
    private LinearLayout onvisibleclick;
    private MatchAdapter todayAdapter,moreAdapter;
    private String TAG="ScheduleFragment";
    private int code=-1;
    private List<Cloth> clothList_TSHIRT = new ArrayList<>();
    private List<Cloth> clothList_JEANS = new ArrayList<>();
    AlwaysMarqueeTextView tips=null;
    public String drg_txt;
    //private List<Cloth> clothList_coat = new ArrayList<>();


    //today为今日首页推荐搭配
    private Match[] today = {
            new Match("上衣", R.drawable.match_default1),
            new Match("下衣", R.drawable.match_default2),
    };
    //more按钮展开后内容
    private Match[] more = {
            new Match("match1", R.drawable.match1),
            new Match("match2", R.drawable.match2),
            new Match("match3", R.drawable.match3),
            new Match("match4", R.drawable.match4),
            new Match("match5", R.drawable.match5),
            new Match("match6", R.drawable.match6),};
    private List<Match> todaylist = new ArrayList<>();
    private List<Match> morelist = new ArrayList<>();
    private void initCloth(Match[] today,List<Match> todaylist){
        todaylist.clear();
        for (int i=0;i<today.length;i++){
            todaylist.add(today[i]);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "推荐衣服fragment");
        View view=inflater.inflate(R.layout.schedule_fragment,container,false);
        tips=view.findViewById(R.id.tips);
        //tips.setText("ceshi");
        sendRequestWithOkHttp();
        Log.i(TAG, "onCreateView:"+drg_txt);
        tips.setText(drg_txt);
        initCloth(today,todaylist);
        //todaylist为全部推荐
        RecyclerView listView = view.findViewById(R.id.display_list);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(),2);
        listView.setLayoutManager(layoutManager1);
        todayAdapter= new MatchAdapter(todaylist);
        listView.setAdapter(todayAdapter);
        //更多推荐
        initCloth(more,morelist);
        final RecyclerView mHiddenLayout = (RecyclerView) view.findViewById(R.id.match_hidden);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        mHiddenLayout.setLayoutManager(layoutManager);
        moreAdapter = new MatchAdapter(morelist);
        mHiddenLayout.setAdapter(moreAdapter);
        mHiddenLayout.setVisibility(mHiddenLayout.GONE);
        miv = (ImageView) view.findViewById(R.id.expand_activities_button);
        mDensity = getResources().getDisplayMetrics().density;
        mHiddenViewMeasuredHeight = (int) (mDensity*240+0.5);
        onvisibleclick = (LinearLayout)view.findViewById(R.id.onclickvisible);
        onvisibleclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHiddenLayout.getVisibility() == View.GONE) {
                    animateOpen(mHiddenLayout);
                    animationIvOpen();
                } else {
                    animateClose(mHiddenLayout);
                    animationIvClose();
                }
            }
        });
        return view;
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    tips.setText(drg_txt);
            }
        }
    };

    //动画不用看
    private void animateOpen(View v) {
        v.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(v, 0,
                mHiddenViewMeasuredHeight);
        animator.start();

    }

    private void animationIvOpen() {
        RotateAnimation animation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setFillAfter(true);
        animation.setDuration(100);
        miv.startAnimation(animation);
    }

    private void animationIvClose() {
        RotateAnimation animation = new RotateAnimation(180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setFillAfter(true);
        animation.setDuration(100);
        miv.startAnimation(animation);
    }

    private void animateClose(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

        });
        animator.start();
    }

    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }


    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                try {
                    // 创建一个OkHttpClient的实例
                    OkHttpClient client = new OkHttpClient();
                    // 如果要发送一条HTTP请求，就需要创建一个Request对象
                    // 可在最终的build()方法之前连缀很多其他方法来丰富这个Request对象
                    Request request = new Request.Builder()
                            .url("https://free-api.heweather.net/s6/weather/lifestyle?location=auto_ip&key=2cc82c03e99d4cae8d7001d0ec84a758")
                            .build();
                    // 调用OkHttpClient的newCall()方法来创建一个Call对象，并调用execute()方法来发送请求并获取服务器的返回数据
                    Response response = client.newCall(request).execute();
                    // 其中Response对象就是服务器返回的数据，将数据转换成字符串
                    String responseData = response.body().string();
                    // 将获取到的字符串传入showResponse()方法中进行UI显示
                    JSONObject myjson= new JSONObject(responseData);
                    code=myjson.optInt("code");
                    Log.i(TAG, "结果-数组"+myjson.optJSONArray("HeWeather6"));
                    JSONArray jsonArray=myjson.optJSONArray("HeWeather6");
                    JSONObject injson=jsonArray.optJSONObject(0);
                    JSONArray basic_json=injson.optJSONArray("lifestyle");
                    Log.i(TAG, "basic_json_run:"+basic_json.optJSONObject(1));
                    JSONObject drsg=basic_json.optJSONObject(1);
                    String drg_brf=drsg.optString("brf");
                    drg_txt=drsg.optString("txt");
                    Log.i(TAG, "drgs_brf:"+drg_brf);
                    Log.i(TAG, "drgs_txt:"+drg_txt);
                    //tips.setText("drg_txt");
                    for(int i=0;i<jsonArray.length();i++){
                        Log.i(TAG, "run:"+jsonArray.optJSONObject(i));
                        // Log.i(TAG, "run: "+);
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        }).start();
    }
}
