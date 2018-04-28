package com.rockets.jphil.rocketlaunchcalendar.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.rockets.jphil.rocketlaunchcalendar.Data.Agency;
import com.rockets.jphil.rocketlaunchcalendar.Data.Mission;
import com.rockets.jphil.rocketlaunchcalendar.Data.Payload;

@Entity
public class MissionDB {

    @PrimaryKey
    public int id;
    public String name;
    public String description;
    public int type;
    public String wikiURL;
    public String typeName;

    public String payloads;
    public String agencies;

    public Mission getMission(Payload[] payloads, Agency[] agencies){
        return new Mission(id, name, description, type, wikiURL, typeName, agencies, payloads);
    }
}
