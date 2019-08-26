package com.android.yinwear.core.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.android.yinwear.core.db.dao.DeviceDao;
import com.android.yinwear.core.db.dao.SessionDao;
import com.android.yinwear.core.db.dao.UserDao;
import com.android.yinwear.core.db.entity.DeviceDetail;
import com.android.yinwear.core.db.entity.Session;
import com.android.yinwear.core.db.entity.UserDetail;

@Database(entities = {UserDetail.class, DeviceDetail.class, Session.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract DeviceDao deviceDao();
    public abstract SessionDao sessionDao();
}