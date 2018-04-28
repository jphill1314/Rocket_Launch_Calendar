package com.rockets.jphil.rocketlaunchcalendar.Data;

import com.rockets.jphil.rocketlaunchcalendar.Database.PadDB;

public class Pad {

    private int id;
    private String name;
    private String infoURL;
    private String wikiURL;
    private String mapURL;
    private double latitude;
    private double longitude;
    private Agency[] agencies;

    public Pad(int id, String name, String infoURL, String wikiURL, String mapURL, double latitude, double longitude, Agency[] agencies) {
        this.id = id;
        this.name = name;
        this.infoURL = infoURL;
        this.wikiURL = wikiURL;
        this.mapURL = mapURL;
        this.latitude = latitude;
        this.longitude = longitude;
        this.agencies = agencies;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInfoURL() {
        return infoURL;
    }

    public String getWikiURL() {
        return wikiURL;
    }

    public String getMapURL() {
        return mapURL;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Agency[] getAgencies() {
        return agencies;
    }

    public PadDB getEntity(){
        PadDB db = new PadDB();

        db.id = id;
        db.name = name;
        db.infoURL = infoURL;
        db.wikiURL = wikiURL;
        db.mapURL = mapURL;
        db.latitude = latitude;
        db.longitude = longitude;
        db.agencies = "";
        if(agencies != null) {
            for (Agency a : agencies) {
                db.agencies += a.getId() + ",";
            }
        }

        return db;
    }
}
