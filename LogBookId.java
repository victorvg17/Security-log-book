package com.example.victo.logbook;

import android.support.annotation.IdRes;
import android.support.annotation.IntDef;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class LogBookId {

    private String companyName;
//    @io.objectbox.annotation.Id long Id;
    @Id private long Id;

    private long startingTime;

    public LogBookId(){

    }

    public LogBookId(String mCompanyName, long mId, long mStartingTime){
        this.companyName = mCompanyName;
        this.Id = mId;
        this.startingTime = mStartingTime;
    }

    public LogBookId(String mCompanyName, long mStartingTime){
        this.companyName = mCompanyName;
        this.startingTime = mStartingTime;
    }

    public void setCompanyName(String mCompanyName) {
        this.companyName = mCompanyName;
    }

    public String getCompanyName(){
        return companyName;
    }

    public void setId(long id) {
        this.Id = id;
    }

    public long getId(){
        return Id;
    }

    public void setStartingTime(long startingTime) {
        this.startingTime = startingTime;
    }

    public long getStartingTime() {
        return startingTime;
    }
}
