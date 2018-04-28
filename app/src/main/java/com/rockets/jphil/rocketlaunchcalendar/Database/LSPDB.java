package com.rockets.jphil.rocketlaunchcalendar.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.rockets.jphil.rocketlaunchcalendar.Data.LSP;

@Entity
public class LSPDB {

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

    public LSP getLSP(){
        String[] urls = (infoURLs != null) ? infoURLs.split(" ") : null;
        return new LSP(id, name, abbrev, countryCode, type, infoURL, wikiURL, changed, urls);
    }
}
