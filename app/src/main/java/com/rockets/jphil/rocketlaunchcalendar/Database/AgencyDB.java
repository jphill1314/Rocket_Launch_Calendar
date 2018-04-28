package com.rockets.jphil.rocketlaunchcalendar.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.rockets.jphil.rocketlaunchcalendar.Data.Agency;

@Entity
public class AgencyDB {

    @PrimaryKey
    public int id;
    public String name;
    public String abbrev;
    public String countryCode;
    public int type;
    public String infoURL;
    public String wikiURL;
    public String changed;
    public String infoURLs;

    public Agency getAgency(){
        String[] info = (infoURLs != null) ? infoURLs.split(" ") : null;
        return new Agency(id, name, abbrev, countryCode, type, infoURL, wikiURL, changed, info);
    }
}
