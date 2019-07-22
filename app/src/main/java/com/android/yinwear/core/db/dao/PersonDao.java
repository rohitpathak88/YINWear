package com.android.yinwear.core.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.yinwear.core.db.entity.PersonDetail;

import java.util.List;

@Dao
public interface PersonDao {

    @Query("SELECT * FROM persondetail")
    List<PersonDetail> getAll();

    @Query("SELECT * FROM persondetail WHERE person_id IN (:personIds)")
    List<PersonDetail> loadPersonByIds(String[] personIds);

    @Insert
    void insert(PersonDetail personDetail);

    @Insert
    void insertAll(PersonDetail[] personDetail);

    @Delete
    void delete(PersonDetail personDetail);

    @Query("DELETE FROM persondetail")
    public void deleteAll();

    @Update
    void update(PersonDetail personDetail);
}
