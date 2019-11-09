package com.yakgwa.kullow.map.Point;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 *  Point
 */


@Entity
public class Point {
    @PrimaryKey
    private long pid;

    @NonNull
    @ColumnInfo(name = "latitude")
    private double latitude;

    @NonNull
    @ColumnInfo(name = "longitude")
    private double longitude;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    public Point(long id, double latitude, double longitude, String name){
        this.pid = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }
    public Point(){
        this.pid = -1;
        this.latitude = -1;
        this.longitude = -1;
        this.name = "null";
    }

    public String getPointText(){
        StringBuilder text = new StringBuilder();
        text.append("["+String.format("%3d", this.pid)+"]");
        text.append(this.name+"\t");
        text.append("("+this.latitude+","+this.longitude+")");
        return text.toString();
    }

    public long getPid() {
        return pid;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    @NonNull
    public String getName() {
        return name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }
}

