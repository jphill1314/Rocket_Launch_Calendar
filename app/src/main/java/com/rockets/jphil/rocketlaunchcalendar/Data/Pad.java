package com.rockets.jphil.rocketlaunchcalendar.Data;

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
}
