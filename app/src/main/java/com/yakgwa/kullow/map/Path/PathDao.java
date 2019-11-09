package com.yakgwa.kullow.map.Path;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PathDao {
    @Query("Select * from Path ORDER BY pthid ASC")
    LiveData<List<Path>> getAllPath();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Path path);

    @Query("DELETE FROM Path")
    void deleteAll();

    @Query("Select p2_id from Path where p1_id=(:p1Id)")
    LiveData<List<Long>> getPointIdBySrcPointId(long p1Id);

    @Query("SELECT p1_id from Path where p2_id=(:p2Id)")
    LiveData<List<Long>> getPointIdByDestPointId(long p2Id);
}
