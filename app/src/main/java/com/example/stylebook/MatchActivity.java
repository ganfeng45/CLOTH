package com.example.stylebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;


public class MatchActivity extends AppCompatActivity {
    private String TAG="ScheduleFragment";
    public static final String MATCH_NAME = "match_name";
    public static final String MATCH_IMAGE_ID = "match_image_id";

    @Override
    protected void onCreate(Bundle savedInstance){
        Log.i(TAG, "推荐衣服active");
        super.onCreate(savedInstance);
        //绑定视图
        setContentView(R.layout.activity_schedule);
        Intent intent = getIntent();
        String clothName = intent.getStringExtra("MATCH_NAME");
        int clothImageId=intent.getIntExtra("MATCH_IMAGE_ID",0);
        ImageView main = (ImageView) findViewById(R.id.Match_MainImage);
        main.setImageResource(clothImageId);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
//        ImageView clothImageView = (ImageView) findViewById(R.id.clothimage_view);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if(actionBar!=null){
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//        collapsingToolbarLayout.setTitle(clothName);
//        Glide.with(this).load(clothImageId).into(clothImageView);
//        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.fab_add);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MatchActivity.this, ClosetAddActivity.class);
//                startActivity(intent);
//            }
//        });
//        initCloth();
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview_cloth);
//        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new QuickAdapter(this,R.id.recycleview_cloth,clothList);
//        recyclerView.setAdapter(adapter);
    }
//    private void initCloth(){
//        clothList.clear();
//        for (int i=0;i<clothes.length;i++){
//            clothList.add(clothes[i]);
//        }
//    }
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
