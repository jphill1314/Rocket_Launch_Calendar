package com.rockets.jphil.rocketlaunchcalendar.Data;

public class Location {

    private Pad[] pads;
    private int id;
    private String name;
    private String infoURL;
    private String wikiURL;
    private String countryCode;

    public Location(Pad[] pads, int id, String name, String infoURL, String wikiURL, String countryCode) {
        this.pads = pads;
        this.id = id;
        this.name = name;
        this.infoURL = infoURL;
        this.wikiURL = wikiURL;
        this.countryCode = countryCode;
    }

    public Pad[] getPads() {
        return pads;
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

    public String getCountryCode() {
        return countryCode;
    }
}
