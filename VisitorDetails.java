package com.example.victo.logbook;

import android.net.Uri;

import javax.annotation.PropertyKey;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class VisitorDetails {
    @Id private long id;

    //should be auto-fill. eg: mVisitorId: TBV15 for 15th visitor of the day.
    private long visitorId;


//    private int visitorId;

    private String firstName;

    private String companyName;

    private String entryTimeString;

    private String exitTimeString;

    private long entryTime;

    private long exitTime;

    private String visitorImageUri;

    private boolean hasVisitorLeft = false;

    public VisitorDetails(){

    }

    public VisitorDetails(long visitorId, String firstName, String companyName, long entryTime, long exitTime){
        this.visitorId = visitorId;
        this.firstName = firstName;
        this.companyName = companyName;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
    }

    public VisitorDetails(long visitorId, String firstName, String companyName, long entryTime){
        this.visitorId = visitorId;
        this.firstName = firstName;
        this.companyName = companyName;
        this.entryTime = entryTime;
//        this.exitTime = exitTime;
    }

    public VisitorDetails(long visitorId, String firstName, String companyName){
        this.visitorId = visitorId;
        this.firstName = firstName;
        this.companyName = companyName;
//        this.entryTime = entryTime;
//        this.exitTime = exitTime;
    }

    public VisitorDetails(String firstName, String companyName){
        this.firstName = firstName;
        this.companyName = companyName;
//        this.entryTime = entryTime;
//        this.exitTime = exitTime;
    }

    //getter and setter methods

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setVisitorId(long visitorId){
        this.visitorId = visitorId;
    }

    public long getVisitorId() {
        return visitorId;
    }

    public void setFirstName(String mFirstName) {
        this.firstName = mFirstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setCompanyName(String mCompanyName) {
        this.companyName = mCompanyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setEntryTimeString(String mEntryTimeString) {
        this.entryTimeString = mEntryTimeString;
    }

    public String getEntryTimeString() {
        return entryTimeString;
    }

    public void setEntryTime(long mEntryTime) {
        this.entryTime = mEntryTime;
    }

    public long getEntryTime() {
        return entryTime;
    }

    public void setExitTimeString(String mExitTimeString) {
        this.exitTimeString = mExitTimeString;
    }

    public String getExitTimeString() {
        return exitTimeString;
    }

    public void setExitTime(long mExitTime) {
        this.exitTime = mExitTime;
    }

    public long getExitTime() {
        return exitTime;
    }

    public void setHasVisitorLeft(boolean hasVisitorLeft) {
        this.hasVisitorLeft = hasVisitorLeft;
    }

    public boolean isHasVisitorLeft() {
        return hasVisitorLeft;
    }

    public void setVisitorImageUri(String uri){
        this.visitorImageUri = uri;
    }

    public String getVisitorImageUri() {
        return visitorImageUri;
    }
}
