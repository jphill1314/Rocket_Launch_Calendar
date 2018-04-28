package com.rockets.jphil.rocketlaunchcalendar.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

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
    public String wikiURLs;
    public String imageURL;
    public String imageSizes;

}
