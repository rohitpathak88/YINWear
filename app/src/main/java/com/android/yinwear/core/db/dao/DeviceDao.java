package com.android.yinwear.core.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.yinwear.core.db.entity.DeviceDetail;

import java.util.List;

@Dao
public interface DeviceDao {

    @Query("SELECT * FROM devicedetail")
    List<DeviceDetail> getAll();

    @Query("SELECT * FROM devicedetail WHERE device_id IN (:deviceIds)")
    List<DeviceDetail> loadDeviceByIds(String[] deviceIds);

    @Insert
    void insert(DeviceDetail devicedetail);

    @Insert
    void insertAll(DeviceDetail[] devicedetail);

    @Delete
    void delete(DeviceDetail devicedetail);

    @Query("DELETE FROM devicedetail")
    public void deleteAll();

    @Update
    void update(DeviceDetail devicedetail);
}
