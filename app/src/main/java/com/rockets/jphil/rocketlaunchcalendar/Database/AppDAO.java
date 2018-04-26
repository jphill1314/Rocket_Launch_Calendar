package com.rockets.jphil.rocketlaunchcalendar.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface AppDAO {

    @Query("SELECT * FROM RocketLaunchDB")
    List<RocketLaunchDB> getAllLaunches();

    @Query("SELECT * FROM RocketLaunchDB WHERE id = :id")
    RocketLaunchDB getLaunchWithID(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLaunches(List<RocketLaunchDB> launchDBS);

    @Delete
    void deleteLaunches(List<RocketLaunchDB> launchDBS);
}
