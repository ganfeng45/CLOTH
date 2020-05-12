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
import com.example.stylebook.R;

import java.util.List;
import java.util.Random;

public class ClothAdapter extends RecyclerView.Adapter<ClothAdapter.ViewHolder>{

    private static final String TAG = "ClothAdapter";

    private Context mContext;

    private List<Cloth> mClothList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ClothImage;
        TextView ClothName;
        View clothView;

        public ViewHolder(View view) {
            super(view);
            clothView = view;
            ClothImage = (ImageView) view.findViewById(R.id.cloth_image);
            ClothName = (TextView) view.findViewById(R.id.cloth_name);
            Random random=new Random();
            int imageHidth = random.nextInt(300)+400;
            LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) ClothImage.getLayoutParams(); //取控件Clothimage当前的布局参数
            linearParams.height = imageHidth;// 控件的高强制设成20
            ClothImage.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        }
    }

    public ClothAdapter(List<Cloth> ClothList) {
        mClothList = ClothList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.cloth_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.clothView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Cloth cloth = mClothList.get(position);
                Intent intent = new Intent(mContext, ClothParentActivity.class);
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
        Glide.with(mContext).load(Cloth.getImageId()).into(holder.ClothImage);
//        Glide.with(mContext).load(Cloth.getBitmap()).into(holder.ClothImage);
    }

    @Override
    public int getItemCount() {
        return mClothList.size();
    }

}
