package com.android.yinwear.core.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.yinwear.core.db.entity.UserDetail;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM UserDetail")
    List<UserDetail> getAll();

    @Query("SELECT * FROM UserDetail WHERE user_id IN (:userIds)")
    List<UserDetail> loadUserByIds(String[] userIds);

    @Insert
    void insert(UserDetail userDetail);

    @Insert
    void insertAll(UserDetail[] userDetail);

    @Delete
    void delete(UserDetail userDetail);

    @Query("DELETE FROM UserDetail")
    public void deleteAll();

    @Update
    void update(UserDetail userDetail);
}
