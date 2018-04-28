package com.rockets.jphil.rocketlaunchcalendar.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.rockets.jphil.rocketlaunchcalendar.Data.Location;
import com.rockets.jphil.rocketlaunchcalendar.Data.Pad;

@Entity
public class LocationDB {

    @PrimaryKey
    public int id;
    public String name;
    public String infoURL;
    public String wikiURL;
    public String countryCode;

    public String pads;

    public Location getLocation(Pad[] pads){
        return new Location(pads, id, name, infoURL, wikiURL, countryCode);
    }
}
