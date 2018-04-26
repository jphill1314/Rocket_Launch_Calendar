package com.rockets.jphil.rocketlaunchcalendar.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {RocketLaunchDB.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract AppDAO appDAO();
}
