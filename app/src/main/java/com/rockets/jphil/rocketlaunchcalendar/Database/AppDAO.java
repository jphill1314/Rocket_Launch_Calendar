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

    @Query("SELECT * FROM AgencyDB")
    List<AgencyDB> getAllAgencies();

    @Query("SELECT * FROM AgencyDB WHERE id = :id")
    AgencyDB getAgencyWithID(int id);

    @Query("SELECT * FROM LocationDB")
    List<LocationDB> getAllLocations();

    @Query("SELECT * FROM LocationDB WHERE id = :id")
    LocationDB getLocationWithID(int id);

    @Query("SELECT * FROM LSPDB")
    List<LSPDB> getAllLSPs();

    @Query("SELECT * FROM LSPDB WHERE id = :id")
    LSPDB getLSPWithID(int id);

    @Query("SELECT * FROM MissionDB")
    List<MissionDB> getAllMissions();

    @Query("SELECT * FROM MissionDB WHERE id = :id")
    MissionDB getMissionWithID(int id);

    @Query("SELECT * FROM PadDB")
    List<PadDB> getAllPads();

    @Query("SELECT * FROM PadDB WHERE id = :id")
    PadDB getPadWithID(int id);

    @Query("SELECT * FROM PayloadDB")
    List<PayloadDB> getAllPayloads();

    @Query("SELECT * FROM PayloadDB WHERE id = :id")
    PayloadDB getPayloadWithID(int id);

    @Query("SELECT * FROM RocketDB")
    List<RocketDB> getAllRockets();

    @Query("SELECT * FROM RocketDB WHERE id = :id")
    RocketDB getRocketWithID(int id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLaunches(List<RocketLaunchDB> launchDBS);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAgencies(List<AgencyDB> agencies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocations(List<LocationDB> locations);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLSPs(List<LSPDB> lsps);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMissions(List<MissionDB> missions);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPads(List<PadDB> pads);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPayloads(List<PayloadDB> payloads);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRockets(List<RocketDB> rockets);


    @Delete
    void deleteLaunches(List<RocketLaunchDB> launchDBS);

    @Delete
    void deleteAgencies(List<AgencyDB> agencies);

    @Delete
    void deleteLocations(List<LocationDB> locations);

    @Delete
    void deleteLSPs(List<LSPDB> lsps);

    @Delete
    void deleteMissions(List<MissionDB> missions);

    @Delete
    void deletePads(List<PadDB> pads);

    @Delete
    void deletePayloads(List<PayloadDB> payloads);

    @Delete
    void deleteRockets(List<RocketDB> rockets);
}
