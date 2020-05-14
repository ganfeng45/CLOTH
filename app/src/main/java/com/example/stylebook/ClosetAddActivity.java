package com.example.stylebook;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.stylebook.db.Cloth;
import com.jrummyapps.android.colorpicker.ColorPanelView;
import com.jrummyapps.android.colorpicker.ColorPickerDialog;
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener;
import com.jrummyapps.android.colorpicker.ColorPickerView;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.zelory.compressor.Compressor;

import static java.lang.reflect.Array.set;

public class ClosetAddActivity extends AppCompatActivity {
    protected static final String TAG = "ClosetAddActivity";
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int CROP_PHOTO = 3;
    protected ImageView preview;
    protected ImageView titlepreview;

    protected Uri imageUri;
    protected ColorPanelView colorPickerViewModel;
    protected EditText editName,editMaterial;
    protected Spinner season,temprature,types;
    protected Button buyTime;
    protected Calendar pickDate=Calendar.getInstance();
    Cloth cloth=new Cloth();
    protected List<String>getSeasonData(){
        List<String> dataList = new ArrayList<String>();
        dataList.add("Spring");
        dataList.add("Summer");
        dataList.add("Fall");
        dataList.add("Winter");
        return dataList;
    }
    protected List<String>getTemData(){
        List<String> dataList = new ArrayList<String>();
        for (int i=1;i<=8;i++){
            dataList.add(i+"");
        }
        return dataList;
    }
    //类型添加
    protected List<String>getTypeData(){
        List<String> dataList = new ArrayList<String>();
        dataList.add("Coat");dataList.add("Sweater");dataList.add("Shirt");dataList.add("T-shirt");dataList.add("Hoodie");
        dataList.add("Dress");dataList.add("Skirt");dataList.add("Jeans");dataList.add("Trouser");dataList.add("Shorts");
        return dataList;
    }

    //将图片转化为字节
    private byte[]img(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Cloth> clothList = new ArrayList<>();
        clothList = LitePal.findAll(Cloth.class);
        Log.i(TAG, "总数"+clothList.size());
        for (Cloth s:clothList){
            Log.i(TAG, "类型:"+s.getType()+";-----name:"+s.getName()+";-----:"+s.getBitmapimg());
        }
        //测试log
//        Cloth querycloth= LitePal.findLast(Cloth.class);
//        Log.i(TAG, "last类型:"+querycloth.getType()+";-----name:"+querycloth.getName()+";-----:"+querycloth.getBitmapimg());


        //Bitmap logbitmaptest=BitmapFactory.decodeByteArray(logimages,0,logimages.length);
//        Log.i(TAG, "最后添加的衣服名字："+querycloth.getName());
//        Log.i(TAG, "最后添加的衣服bitmap："+querycloth.getBitmap());

        //绑定布局文件
        setContentView(R.layout.closet_item_add);
        preview = (ImageView) findViewById(R.id.imageview_preview);
        titlepreview = (ImageView) findViewById(R.id.cloth_imageview);
        //preview.setImageBitmap(logbitmaptest);


//        preview.setImageBitmap(bitmaptest);
        ImageButton takePhoto = (ImageButton) findViewById(R.id.button_open_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //拍照按钮监听事件
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick:点击了拍照按钮");
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
                //全局imguri
                imageUri = FileProvider.getUriForFile(ClosetAddActivity.this,"com.example.stylebook.fileprovider",outputImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });
        //相册选取照片按钮
        ImageButton chooseFromGallery = (ImageButton)findViewById(R.id.button_open_gallery);
        chooseFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick:相册选择");
                if(ContextCompat.checkSelfPermission(ClosetAddActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ClosetAddActivity.this,new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },1);
                }else{
                    //打开相册
                    openAlbum();
                }
            }
        });
        setSupportActionBar(toolbar);
        editName = (EditText) findViewById(R.id.edit_name);
        editMaterial = (EditText) findViewById(R.id.edi_material);
        season = (Spinner) findViewById(R.id.spinner_season);
        temprature = (Spinner) findViewById(R.id.spinner_temprature);
        types = (Spinner) findViewById(R.id.spinner_type);
        buyTime = (Button) findViewById(R.id.text_date_picker);
        buyTime.setText(pickDate.get(Calendar.YEAR)+"-"+(pickDate.get(Calendar.MONTH)+1)+"-"+pickDate.get(Calendar.DAY_OF_MONTH));
        buyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            //购买时间
            public void onClick(View v) {
                showDatePickDlg();
            }
        });
        ArrayAdapter<String> seasonAdapter = new ArrayAdapter<String>(ClosetAddActivity.this,R.layout.spinner_item,getSeasonData());
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
        ArrayAdapter<String> temAdapter = new ArrayAdapter<String>(ClosetAddActivity.this,R.layout.spinner_item,getTemData());
        temprature.setAdapter(temAdapter);
        temprature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cloth.setTemprature(position+1);
                Log.i(TAG, "温度"+position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> typesAdapter = new ArrayAdapter<String>(ClosetAddActivity.this,R.layout.spinner_item,getTypeData());
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(ClosetAddActivity.this);
                dialog.setTitle("Save Confirm");
                dialog.setMessage("Are you sure to Save this cloth?");
                dialog.setCancelable(true);
                dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cloth.setColor(colorPickerViewModel.getColor());
                        cloth.setMaterial(editMaterial.getText().toString());
                        //cloth.setBuyDate(pickDate);
                        cloth.setName(editName.getText().toString()+"_"+cloth.getColor()+"_"+cloth.getMaterial());//                System.out.println(cloth.getBuyDate().get(Calendar.MONTH));
                        Log.i(TAG, "save前bitmapimg的大小:"+cloth.getBitmapimg().length);
                        Log.i(TAG, "save前确认"+"-DATA:"+cloth.getBuyDate()+"-CLORE:"+cloth.getColor()+"-TEM:"+cloth.getTemprature());
                        if(cloth.save()){
                            Log.i(TAG, "------保存成功-----");
                            List<Cloth> clothList = new ArrayList<>();
                            clothList = LitePal.findAll(Cloth.class);
                            Log.i(TAG, "总数"+clothList.size());
                            for (Cloth s:clothList){
                                Log.i(TAG, "类型:"+s.getType()+";-----name:"+s.getName()+";-----:"+s.getBitmapimg());
                            }
                        }else{
                            Log.i(TAG, "------保存失败-----");
                        }
                        ClosetAddActivity.this.finish();
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
    //压缩图片函数(仅限file，无关内存)
    //返回byte[]
    protected  byte[] saveBitmap(Bitmap bitmap){
        int size=bitmap.getWidth()*bitmap.getHeight()*4;
        ByteArrayOutputStream baos=new ByteArrayOutputStream(size);
        int option=100;
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        while (baos.toByteArray().length/1024>500){
            baos.reset();
            option-=10;
            bitmap.compress(Bitmap.CompressFormat.JPEG,option,baos);
        }
        byte[] imagedata=baos.toByteArray();
        Log.i(TAG, "saveBitmap压缩后的大小:"+imagedata.length);
        return imagedata;
    }
    //拍照/相册的结果回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "压缩图片");
                    //压缩展示图片
                    BitmapFactory.Options options=new BitmapFactory.Options();
                    options.inJustDecodeBounds=true;
                    try{
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri),null,options);
                        //Bitmap bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(uri),null,options);
                        int height=options.outHeight;
                        int size=height/800;
                        if(size<=0){
                            size=2;
                        }
                        options.inSampleSize=size;
                        options.inJustDecodeBounds = false;
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri),null,options);
                        //存储照片到数据库
                        cloth.setBitmapimg(saveBitmap(bitmap));
                        cloth.setBitmap(bitmap);
                        //跟新视图
                        preview.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "选择照片的回调函数 CHOOSE_PHOTO");
                    handleImageOnKitKat(data);
                }
                break;
            default:
                break;
        }
    }
    public boolean onCreateOptionsMenu(Menu menu){
        Log.i(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.toolbar_home:
                Intent intent = new Intent(ClosetAddActivity.this, MainActivity.class);
                startActivity(intent);
                intent.putExtra("fragment",2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void openAlbum(){
        Log.i(TAG, "openAlbum打开相册");
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        //结果回调
        startActivityForResult(intent,CHOOSE_PHOTO);
    }
    //权限检查
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @TargetApi(19)
    protected void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.i(TAG, "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            Log.i(TAG, "document类型的Uri");
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            Log.i(TAG, "content类型的Uri");
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            Log.i(TAG, "FILE类型的Uri");
            imagePath = uri.getPath();
        }
        displayImage(imagePath,uri); // 根据图片路径显示图片
    }
    protected String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    protected void displayImage(String imagePath ,Uri uri) {
        if (imagePath != null) {
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            try {
                Bitmap bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(uri),null,options);
                int height=options.outHeight;
                int size=height/800;
                if(size<=0){
                    size=2;
                }
                options.inSampleSize=size;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(getContentResolver()
                        .openInputStream(uri), null, options);
                cloth.setBitmapimg(saveBitmap(bitmap));
                cloth.setBitmap(bitmap);
                //更新视图
                preview.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
           /* Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            byte[] dsimgbyte=img(bitmap);
            cloth.setBitmapimg(dsimgbyte);
            Log.i(TAG, "选取的长度:"+dsimgbyte.length);
            preview.setImageBitmap(bitmap);
            //存储图片
            Log.i(TAG, "displayImage的bitmap:"+cloth.getBitmap());
            Log.i(TAG,"displayed");
            try{
                //压缩照片2
                File file = new File(imagePath);
                Bitmap compressedImageBitmap = new Compressor(this).compressToBitmap(file);
                cloth.setBitmap(compressedImageBitmap);
                Log.i(TAG,"压缩compressed---1");
                Log.i(TAG, "displayImage:"+cloth.getBitmap());
            }catch (Exception e){
                e.printStackTrace();
            }*/
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
    //日历
    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear=monthOfYear+1;
                buyTime.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                Log.i(TAG, "日期是："+year + "-" + monthOfYear + "-" + dayOfMonth);
                pickDate.set(year,monthOfYear,dayOfMonth);
                cloth.setBuyDate(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public static final int DIALGE_ID = 0;
    protected void opeAdvancenDialog() {
        int color = colorPickerViewModel.getColor();
//传入的默认color
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.newBuilder().setColor(color)
                .setDialogTitle(R.string.color_picker)
//设置dialog标题
//                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
//设置为自定义模式
                .setShowAlphaSlider(true)
//设置有透明度模式，默认没有透明度
                .setDialogId(DIALGE_ID)
//设置Id,回调时传回用于判断
                .setAllowPresets(false)
//不显示预知模式
                .create();
//Buider创建
        colorPickerDialog.setColorPickerDialogListener(pickerDialogListener);
//设置回调，用于获取选择的颜色
        colorPickerDialog.show(getFragmentManager(), "color-picker-dialog");
    }

    protected ColorPickerDialogListener pickerDialogListener = new ColorPickerDialogListener() {
        @Override
        public void onColorSelected(int dialogId, @ColorInt int color) {
            if (dialogId == DIALGE_ID) {
                colorPickerViewModel.setColor(color);
            }
        }
        @Override
        public void onDialogDismissed(int dialogId) {

        }
    };
}

