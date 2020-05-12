package com.example.stylebook.db;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.stylebook.R;

import java.util.List;

public class ClothAdapterRound extends RecyclerView.Adapter<ClothAdapterRound.ViewHolder>{

    private static final String TAG = "ClothAdapter";

    private Context mContext;

    private List<Cloth> mClothList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ClothImage;
        TextView ClothName;

        public ViewHolder(View view) {
            super(view);
            ClothImage = (ImageView) view.findViewById(R.id.cloth_image);
            ClothName = (TextView) view.findViewById(R.id.cloth_name);
        }
    }

    public ClothAdapterRound(List<Cloth> ClothList) {
        mClothList = ClothList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.cloth_item_horizon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cloth Cloth = mClothList.get(position);
        holder.ClothName.setText(Cloth.getName());
        Glide.with(mContext).load(Cloth.getImageId()).into(holder.ClothImage);
    }

    @Override
    public int getItemCount() {
        return mClothList.size();
    }

}
