package com.example.stylebook;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.stylebook.db.Cloth;
import com.example.stylebook.db.ClothAdapter;
import com.example.stylebook.db.HoodieAdapter;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClothParentActivity extends AppCompatActivity {
    private List<Cloth> clothList = new ArrayList<>();
    private HoodieAdapter adapter;
    private  String TAG="ClothParentActivity";
    String clothName;
    @Override
    protected void onCreate(Bundle savedInstance){
        Log.i(TAG, "衣服");
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_cloth_parent);
        Intent intent = getIntent();
        clothName = intent.getStringExtra("CLOTH_NAME");
        int clothImageId=intent.getIntExtra("CLOTH_IMAGE_ID",0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        ImageView clothImageView = (ImageView) findViewById(R.id.clothimage_view);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(clothName);
        Glide.with(this).load(clothImageId).into(clothImageView);
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.fab_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClothParentActivity.this, ClosetAddActivity.class);
                startActivity(intent);
            }
        });
        initCloth();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview_cloth);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HoodieAdapter(clothList);
        recyclerView.setAdapter(adapter);
    }
    private void initCloth(){
        try{
            clothList = LitePal.where("type =?",clothName).find(Cloth.class);
            //clothList = LitePal.findAll(Cloth.class);
            for (Cloth s:clothList){
                Log.i(TAG, "类型:"+s.getType()+";-----name:"+s.getName());
            }
            Log.i(TAG, "查询类型"+clothName);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
