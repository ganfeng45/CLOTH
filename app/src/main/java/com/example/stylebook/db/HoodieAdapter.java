package com.example.stylebook.db;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.stylebook.ClothChildActivity;
import com.example.stylebook.R;

import java.util.List;

public class HoodieAdapter extends RecyclerView.Adapter<HoodieAdapter.ViewHolder>{

    private static final String TAG = "HoodieAdapter";
    private Context mContext;
    private List<Cloth> mClothList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ClothImage;
        TextView ClothName;
        CardView Cardview;
        public ViewHolder(View view) {
            super(view);
            Cardview = (CardView)view;
            ClothImage = (ImageView) view.findViewById(R.id.cloth_image);
            ClothName = (TextView) view.findViewById(R.id.cloth_name);
            int imageHidth = 300;
            LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) ClothImage.getLayoutParams(); //取控件Clothimage当前的布局参数
            linearParams.height = imageHidth;// 控件的高强制设成20
            ClothImage.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        }
    }

    public HoodieAdapter(List<Cloth> ClothList) {
        mClothList = ClothList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.cloth_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        //设置监听事件
        holder.Cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Cloth cloth = mClothList.get(position);
                Intent intent = new Intent(mContext, ClothChildActivity.class);
                intent.putExtra("CLOTH_NAME",cloth.getName());
                intent.putExtra("CLOTH_IMAGE_ID",cloth.getImageId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cloth Cloth = mClothList.get(position);
        holder.ClothName.setText(Cloth.getName());
        try{
            Bitmap bitmap= BitmapFactory.decodeByteArray(Cloth.getBitmapimg(),0,Cloth.getBitmapimg().length);
            Log.i(TAG, "最后的长度"+Cloth.getBitmapimg().length);
            holder.ClothImage.setImageBitmap(bitmap);
            //Glide.with(mContext).load(Cloth.getImageId()).into(holder.ClothImage);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mClothList.size();
    }

}
