package com.android.yinwear.core.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.android.yinwear.core.db.dao.DeviceDao;
import com.android.yinwear.core.db.dao.PersonDao;
import com.android.yinwear.core.db.dao.SessionDao;
import com.android.yinwear.core.db.entity.DeviceDetail;
import com.android.yinwear.core.db.entity.PersonDetail;
import com.android.yinwear.core.db.entity.Session;

@Database(entities = {PersonDetail.class, DeviceDetail.class, Session.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PersonDao personDao();
    public abstract DeviceDao deviceDao();
    public abstract SessionDao sessionDao();
}