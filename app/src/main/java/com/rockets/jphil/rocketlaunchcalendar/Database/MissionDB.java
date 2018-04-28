package com.rockets.jphil.rocketlaunchcalendar.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

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
}
