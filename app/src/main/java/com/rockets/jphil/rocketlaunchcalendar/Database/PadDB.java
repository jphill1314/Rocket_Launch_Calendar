package com.rockets.jphil.rocketlaunchcalendar.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.rockets.jphil.rocketlaunchcalendar.Data.Agency;
import com.rockets.jphil.rocketlaunchcalendar.Data.Pad;

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

    public Pad getPad(Agency[] agencies){
        return new Pad(id, name, infoURL, wikiURL, mapURL, latitude, longitude, agencies);
    }
}
