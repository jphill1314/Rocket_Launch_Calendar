package com.rockets.jphil.rocketlaunchcalendar.Data;

import com.rockets.jphil.rocketlaunchcalendar.Database.AgencyDB;

public class Agency {

    private int id;
    private String name;
    private String abbrev;
    private String countryCode;
    private int type;
    private String infoURL;
    private String wikiURL;
    private String changed;
    private String infoURLs[];

    public Agency(int id, String name, String abbrev, String countryCode, int type, String infoURL, String wikiURL, String changed, String[] infoURLs) {
        this.id = id;
        this.name = name;
        this.abbrev = abbrev;
        this.countryCode = countryCode;
        this.type = type;
        this.infoURL = infoURL;
        this.wikiURL = wikiURL;
        this.changed = changed;
        this.infoURLs = infoURLs;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public int getType() {
        return type;
    }

    public String getInfoURL() {
        return infoURL;
    }

    public String getWikiURL() {
        return wikiURL;
    }

    public String getChanged() {
        return changed;
    }

    public String[] getInfoURLs() {
        return infoURLs;
    }

    public AgencyDB getEntity(){
        AgencyDB db = new AgencyDB();

        db.id = id;
        db.name = name;
        db.abbrev = abbrev;
        db.countryCode = countryCode;
        db.type = type;
        db.infoURL = infoURL;
        db.wikiURL = wikiURL;
        db.changed = changed;
        db.infoURLs = "";

        if(infoURLs != null) {
            for (String s : infoURLs) {
                db.infoURLs += s + " ";
            }
        }

        return db;
    }
}
