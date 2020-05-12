package com.example.stylebook.db;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.litepal.crud.LitePalSupport;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Match  {
    private List<Cloth> list;//？？
    private int num;
    private String name;
    private int imageId;
    private int season;
    private List<Calendar> dates;//？？
    private int temprature;
    private Bitmap bitmap;//？？


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getTemprature() {
        return temprature;
    }

    public void setTemprature(int temprature) {
        this.temprature = temprature;
    }

    public Match(String name, int imageId){
        this.name=name;
        this.imageId=imageId;
    }
    public Match(String name, Bitmap bitmap){
        this.name=name;
        this.bitmap=bitmap;
    }


    public void setSeason(int season) {
        this.season = season;
    }

    public int getSeason() {
        return season;
    }

    public void setDates(List<Calendar> dates) {
        this.dates = dates;
    }

    public List<Calendar> getDates() {
        return dates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public List<Cloth> getList() {
        return list;
    }

    public int getNum() {
        return num;
    }

    public void setList(List<Cloth> list) {
        this.list = list;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
