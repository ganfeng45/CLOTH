package com.example.stylebook;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stylebook.db.Cloth;
import com.example.stylebook.db.ClothAdapter;
import com.example.stylebook.db.ClothAdapterRound;

import java.util.ArrayList;
import java.util.List;

public class ClosetFragment extends Fragment {
    private Cloth[] clothes = {
            new Cloth("Coat", R.drawable.coat),
            new Cloth("Sweater", R.drawable.sweater),
            new Cloth("Hoodie", R.drawable.hoodie),
            new Cloth("Shirt", R.drawable.shirt),
            new Cloth("Tshirt", R.drawable.tshirt),
            new Cloth("Dress", R.drawable.dress),
            new Cloth("Jeans", R.drawable.jeans),
            new Cloth("Trouser", R.drawable.trouser),
            new Cloth("Shorts", R.drawable.shorts),
            new Cloth("Skirt", R.drawable.skirt)};
    private List<Cloth> clothList = new ArrayList<>();
    private ClothAdapter adapter;
    private ClothAdapterRound clothAdapterRound;
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.closet_fragment,container,false);
        FloatingActionButton add = (FloatingActionButton)view.findViewById(R.id.fab_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClosetAddActivity.class);
                startActivity(intent);
            }
        });
        initCloth();

        final RecyclerView staggeredRecyclerView = (RecyclerView)view.findViewById(R.id.recycleview_cloth);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        staggeredRecyclerView.setLayoutManager(layoutManager);
        adapter = new ClothAdapter(clothList);
        staggeredRecyclerView.setAdapter(adapter);

        final RecyclerView horizontalRecyclerView=(RecyclerView)view.findViewById(R.id.horizon_recycleview_cloth);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);
        horizontalRecyclerView.setLayoutManager(linearLayoutManager);
        clothAdapterRound = new ClothAdapterRound(clothList);
        horizontalRecyclerView.setAdapter(clothAdapterRound);

        Resources resources = this.getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        final double density =Math.ceil( dm.heightPixels/dm.widthPixels);
        staggeredRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (RecyclerView.SCROLL_STATE_IDLE != recyclerView.getScrollState()) {
                    horizontalRecyclerView.scrollBy(dy/(int)density, dx/(int)density);
                }
            }
        });
        horizontalRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (RecyclerView.SCROLL_STATE_IDLE != recyclerView.getScrollState()) {
                    staggeredRecyclerView.scrollBy(dy*(int)density, dx*(int)density);
                }
            }
        });
        return view;
    }
    private void initCloth(){
        clothList.clear();
        for (int i=0;i<10;i++){
            clothList.add(clothes[i]);
        }
    }
}
