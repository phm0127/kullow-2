package com.yakgwa.kullow.map.Point;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PointDao {
    @Query("Select * from Point ORDER BY pid ASC")
    LiveData<List<Point>> getAllPoint();

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Point point);

    @Query("DELETE FROM Point")
    void deleteAll();

    @Query("SELECT * from Point where name<> \"branch\"")
    LiveData<List<Point>> getAllNode();

    @Query("Select latitude from point where (pid=:pointid)")
    double getLatitudeByPointId(long pointid);

    @Query("Select longitude from point where (pid=:pointid)")
    double getLongitudeByPointId(long pointid);

    @Query("Select * from Point where pid=(:pid)")
    Point getPointById(long pid);
}
