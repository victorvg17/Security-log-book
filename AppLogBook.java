package com.example.victo.logbook;

import android.app.Application;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.objectbox.BoxStore;

public class AppLogBook extends Application{

    private static final String AppName = "Security Log Book";
    private static BoxStore mBoxStore;
//    Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
//        ObjectBox.init(this);

        mBoxStore = MyObjectBox.builder().androidContext(this.getApplicationContext()).build();
    }

//    public static BoxStore getBoxStore() {
//        return mBoxStore;
//    }
    public BoxStore getBoxStore() {
        return mBoxStore;
    }

    public String getTimefromDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss a");
        String dataToString = format.format(date);
        return dataToString;
    }

}
