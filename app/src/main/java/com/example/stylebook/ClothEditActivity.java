package com.example.stylebook;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.stylebook.db.Cloth;
import com.jrummyapps.android.colorpicker.ColorPanelView;

import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class ClothEditActivity extends ClosetAddActivity {
    static String clothName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.closet_item_edit);
        Intent intent = getIntent();
        clothName = intent.getStringExtra("CLOTH_NAME");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preview = (ImageView) findViewById(R.id.imageview_preview);
        ImageButton takePhoto = (ImageButton) findViewById(R.id.button_open_camera);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建File对象，用于存储拍照后的图片
                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if (outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                imageUri = FileProvider.getUriForFile(ClothEditActivity.this,"com.example.stylebook.fileprovider",outputImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });
        ImageButton chooseFromGallery = (ImageButton)findViewById(R.id.button_open_gallery);
        chooseFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(ClothEditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ClothEditActivity.this,new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },1);
                }else{
                    openAlbum();
                }
            }
        });

        editName = (EditText) findViewById(R.id.edit_name);
        editMaterial = (EditText) findViewById(R.id.edi_material);
        season = (Spinner) findViewById(R.id.spinner_season);
        temprature = (Spinner) findViewById(R.id.spinner_temprature);
        types = (Spinner) findViewById(R.id.spinner_type);
        buyTime = (Button) findViewById(R.id.text_date_picker);
        buyTime.setText(pickDate.get(Calendar.YEAR)+"-"+(pickDate.get(Calendar.MONTH)+1)+"-"+pickDate.get(Calendar.DAY_OF_MONTH));
        buyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickDlg();
            }
        });
        ArrayAdapter<String> seasonAdapter = new ArrayAdapter<String>(ClothEditActivity.this,R.layout.spinner_item,getSeasonData());
        season.setAdapter(seasonAdapter);
        season.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cloth.setSeason(position+1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> temAdapter = new ArrayAdapter<String>(ClothEditActivity.this,R.layout.spinner_item,getTemData());
        temprature.setAdapter(temAdapter);
        temprature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cloth.setTemprature(position+1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> typesAdapter = new ArrayAdapter<String>(ClothEditActivity.this,R.layout.spinner_item,getTypeData());
        types.setAdapter(typesAdapter);
        types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cloth.setType(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        colorPickerViewModel = (ColorPanelView) findViewById(R.id.color_panel_view);
        colorPickerViewModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opeAdvancenDialog();
            }
        });
        FloatingActionButton floartingdone=(FloatingActionButton)findViewById(R.id.floatingbar_done);
        floartingdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ClothEditActivity.this);
                dialog.setTitle("Save Confirm");
                dialog.setMessage("Are you sure to Update this cloth?");
                dialog.setCancelable(true);
                dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cloth.setColor(colorPickerViewModel.getColor());
                        cloth.setMaterial(editMaterial.getText().toString());
//                        cloth.setBuyDate(pickDate);
                        cloth.setName(editName.getText().toString()+"_"+cloth.getColor()+"_"+cloth.getMaterial());//                System.out.println(cloth.getBuyDate().get(Calendar.MONTH));
//                        System.out.println(cloth.getBuyDate().get(Calendar.DAY_OF_MONTH));
                        System.out.println(cloth.getSeason());
                        System.out.println(cloth.getColor());
                        System.out.println(cloth.getMaterial());
                        System.out.println(cloth.getName());
                        System.out.println(cloth.getTemprature());
                        System.out.println(cloth.getType());
                        //System.out.println(cloth.getBitmap());
                        cloth.updateAll("name = ?",clothName);
                        ClothEditActivity.this.finish();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }
        });
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(ClothEditActivity.this);
                dialog.setTitle("Delete Confirm");
                dialog.setMessage("Are you sure to delete this cloth?");
                dialog.setCancelable(true);
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            LitePal.deleteAll(Cloth.class, "name = ?",clothName);
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
                Intent intent = new Intent(ClothEditActivity.this, MainActivity.class);
                startActivity(intent);
                intent.putExtra("fragment",2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}