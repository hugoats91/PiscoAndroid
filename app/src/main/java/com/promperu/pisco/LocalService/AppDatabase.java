package com.promperu.pisco.LocalService;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.promperu.pisco.LocalService.Entity.EntityStateOnboarding;
import com.promperu.pisco.LocalService.Entity.EntityUser;
import com.promperu.pisco.LocalService.Interface.InterfaceUserDao;
import com.promperu.pisco.Utils.AppConstantList;

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