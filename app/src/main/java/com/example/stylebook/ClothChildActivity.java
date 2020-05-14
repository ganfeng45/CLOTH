package com.example.stylebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.stylebook.db.Cloth;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

/*
衣服详情页面
 */
public class ClothChildActivity extends AppCompatActivity {
    private static final String TAG = "ClothChildActivity";
    public static String CLOTH_NAME = "cloth_name";
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_cloth_child);
        Intent intent = getIntent();
        //获取衣服参数
        final String clothName = intent.getStringExtra("CLOTH_NAME");
        //int clothImageId=intent.getIntExtra("CLOTH_IMAGE_ID",0);
        CLOTH_NAME=clothName;
        Log.i(TAG, "衣服名字: "+clothName);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        //衣服图片
        ImageView clothImageView = (ImageView) findViewById(R.id.imageView);
        setSupportActionBar(toolbar);
        TextView textname = (TextView) findViewById(R.id.edit_name);
        textname.setText(clothName);
        try{
            List<Cloth> cloths = LitePal.where("name = ?",clothName).find(Cloth.class);
            Log.i(TAG, "结果总数"+cloths.size());
            //Log.d(TAG,cloths.get(0).getType()+"-"+cloths.get(0).getBuyDate());
            Log.d(TAG,cloths.get(0).getMaterial());
            Log.d(TAG,cloths.get(0).getName());
            Log.d(TAG,"byte"+cloths.get(0).getBitmapimg().length);
            TextView textColor = (TextView) findViewById(R.id.color_panel_view);
            textColor.setBackgroundColor(cloths.get(0).getColor());
            TextView textMaterial = (TextView) findViewById(R.id.edi_material);
            textMaterial.setText(cloths.get(0).getMaterial());
            TextView textSeason = (TextView) findViewById(R.id.spinner_season);
            textSeason.setText(showSeason(cloths.get(0).getSeason()));
            TextView buyTime = (TextView) findViewById(R.id.text_date_picker);
            String calendar = cloths.get(0).getBuyDate();
            String tempwen= String.valueOf(cloths.get(0).getTemprature());
            Log.i(TAG, "购买时间"+calendar);
            Log.i(TAG, "onCreate:测试5");
            TextView textType = (TextView) findViewById(R.id.spinner_type);
            textType.setText(cloths.get(0).getType());
            TextView textTem = (TextView) findViewById(R.id.spinner_temprature);
            Log.i(TAG, "onCreate:"+cloths.get(0).getTemprature());
            buyTime.setText(calendar);
            textTem.setText(tempwen);
            Bitmap bitmap= BitmapFactory.decodeByteArray(cloths.get(0).getBitmapimg(),0,cloths.get(0).getBitmapimg().length);
            Log.i(TAG, "onCreate:"+bitmap);
            clothImageView.setImageBitmap(bitmap);
        }catch (Exception e){
            e.printStackTrace();
        }
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(clothName);
        //Glide.with(this).load(clothImageId).into(clothImageView);

        FloatingActionButton editButton = (FloatingActionButton) findViewById(R.id.fab);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClothChildActivity.this, ClothEditActivity.class);
                startActivity(intent);
                intent.putExtra("CLOTH_NAME",clothName);
            }
        });
    }
    public String showSeason(int i){
        String out = "Spring";
        switch (i){
            case 1:
                out = "Spring";
                break;
            case 2:
                out = "Summer";
                break;
            case 3:
                out = "Fall";
                break;
            case 4:
                out = "Winter";
                break;
        }
        return out;
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.toolbar_delete:
                AlertDialog.Builder dialog = new AlertDialog.Builder(ClothChildActivity.this);
                dialog.setTitle("Delete Confirm");
                dialog.setMessage("Are you sure to delete this cloth?");
                dialog.setCancelable(true);
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            LitePal.deleteAll(Cloth.class, "name = ?",CLOTH_NAME);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        finish();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
                return true;
            case R.id.toolbar_home:
                Intent intent = new Intent(ClothChildActivity.this, MainActivity.class);
                startActivity(intent);
                intent.putExtra("fragment",2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
