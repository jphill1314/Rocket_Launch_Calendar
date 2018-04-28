package com.rockets.jphil.rocketlaunchcalendar.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class PadDB {

    @PrimaryKey
    public int id;
    public String name;
    public String infoURL;
    public String wikiURL;
    public String mapURL;
    public double latitude;
    public double longitude;

    public String agencies;
}
