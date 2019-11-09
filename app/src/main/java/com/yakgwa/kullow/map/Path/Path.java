package com.yakgwa.kullow.map.Path;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.yakgwa.kullow.map.Point.Point;


@Entity
public class Path {
    @PrimaryKey
    private long pthid;

    @NonNull
    @ColumnInfo(name = "p1_id")
    @ForeignKey(entity = Point.class, parentColumns = "pid", childColumns = "p1_id")
    private long p1Id;

    @NonNull
    @ColumnInfo(name = "p2_id")
    @ForeignKey(entity = Point.class, parentColumns = "pid", childColumns = "p2_id")
    private long p2Id;

    public Path(long pthid, long p1Id, long p2Id) {
        this.pthid = pthid;
        this.p1Id = p1Id;
        this.p2Id = p2Id;
    }
    public String getPathText(){
        StringBuilder text = new StringBuilder();
        text.append("["+String.format("%2d", this.pthid)+"]");
        text.append("("+this.p1Id+" - "+this.p2Id+")");
        return text.toString();
    }

    public long getPthid(){
        return pthid;
    }
    public long getP1Id(){
        return p1Id;
    }
    public long getP2Id(){
        return p2Id;
    }

    public void setP1Id(long p1Id) {
        this.p1Id = p1Id;
    }

    public void setP2Id(long p2Id) {
        this.p2Id = p2Id;
    }

    public void setPthid(long pthid) {
        this.pthid = pthid;
    }
}
