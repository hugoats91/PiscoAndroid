package com.pisco.app.LocalService;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.pisco.app.LocalService.Entity.EntityStateOnboarding;
import com.pisco.app.LocalService.Entity.EntityUser;
import com.pisco.app.LocalService.Interface.InterfaceUserDao;
import com.pisco.app.Utils.AppConstantList;

@Database(entities = {EntityUser.class, EntityStateOnboarding.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static AppDatabase INSTANCE;

    @SuppressWarnings("WeakerAccess")
    public abstract InterfaceUserDao userDao();

    public static void getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppConstantList.BD_NAME)
                            .allowMainThreadQueries()
                            .build();
        }
    }

}