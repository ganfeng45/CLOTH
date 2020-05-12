package com.example.stylebook.db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.Log;

import org.litepal.crud.LitePalSupport;

import java.util.Calendar;
import java.util.Date;

public class Cloth extends LitePalSupport {
    private  String name;
    private int imageId;
    private int color;
    private String material;
    private int season;
    private String buyDate;
    private String type;
    private int temprature;
    private Bitmap bitmap;
    private byte[] bitmapimg;
    public Cloth(){};
    public Cloth(String name, int imageId){
        this.name=name;
        this.imageId=imageId;
    }
    public Cloth(String name, int imageId, int color,String material,int season,String buyDate,String type,int temprature){
        this.name=name;
        this.imageId=imageId;
        this.color=color;
        this.material=material;
        this.season=season;
        this.buyDate=buyDate;
        this.type=type;
        this.temprature=temprature;
    }
    public byte[] getBitmapimg() {
        return bitmapimg;
    }

    public void setBitmapimg(byte[] bitmapimg) {
        this.bitmapimg = bitmapimg;
    }

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
    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
