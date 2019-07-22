package com.android.yinwear.core.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.yinwear.core.db.entity.Session;

import java.util.List;

@Dao
public interface SessionDao {

    @Query("SELECT * FROM devicedetail")
    List<Session> getAll();

    @Query("SELECT device_id FROM session WHERE person_id IN (:personIds)")
    List<String> loadDevicesByPersonIds(String[] personIds);

    @Insert
    void insert(Session session);

    @Insert
    void insertAll(Session[] session);

    @Delete
    void delete(Session session);

    @Query("DELETE FROM session")
    public void deleteAll();

    @Update
    void update(Session session);
}
