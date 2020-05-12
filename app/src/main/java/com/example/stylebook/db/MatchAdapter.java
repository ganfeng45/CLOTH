package com.example.stylebook.db;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.stylebook.ClothParentActivity;
import com.example.stylebook.MatchActivity;
import com.example.stylebook.R;

import java.util.List;
import java.util.Random;
//推荐系统recycleview适配器
public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder>{

    private static final String TAG = "MatchAdapter";

    private Context mContext;

    private List<Match> mMatchList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView matchImage;
        TextView matchName;
        View matchView;

        public ViewHolder(View view) {
            super(view);
            matchView = view;
            //绑定控件
            matchImage = (ImageView) view.findViewById(R.id.cloth_image);
            matchName = (TextView) view.findViewById(R.id.cloth_name);
            int imageHidth = 300;
            LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) matchImage.getLayoutParams(); //取控件Clothimage当前的布局参数
            linearParams.height = imageHidth;// 控件的高强制设成20
            matchImage.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        }
    }

    public MatchAdapter(List<Match> MatchList) {
        mMatchList = MatchList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.cloth_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        //点击事件
        holder.matchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Match match = mMatchList.get(position);
                Intent intent = new Intent(mContext, MatchActivity.class);
                intent.putExtra("MATCH_NAME",match.getName());
                intent.putExtra("MATCH_IMAGE_ID",match.getImageId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }
    //绑定数据
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Match match = mMatchList.get(position);
        holder.matchName.setText(match.getName());
        Glide.with(mContext).load(match.getImageId()).into(holder.matchImage);
    }
    @Override
    public int getItemCount() {
        return mMatchList.size();
    }

}
