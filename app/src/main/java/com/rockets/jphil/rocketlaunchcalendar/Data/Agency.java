package com.rockets.jphil.rocketlaunchcalendar.Data;

import com.google.gson.annotations.SerializedName;

public class Agency {

    private int id;
    private String name;
    private String abbrev;
    private String countryCode;
    private int type;
    private String infoURL;
    private String wikiURL;
    private String changed;
    private String[] infoURLs;
    private String[] wikiURLs;
    private String imageURL;
    private int[] imageSizes;

    public Agency(int id, String name, String abbrev, String countryCode, int type, String infoURL,
                  String wikiURL, String changed, String[] infoURLs, String[] wikiURLs,
                  String imageURL, int[] imageSizes) {
        this.id = id;
        this.name = name;
        this.abbrev = abbrev;
        this.countryCode = countryCode;
        this.type = type;
        this.infoURL = infoURL;
        this.wikiURL = wikiURL;
        this.changed = changed;
        this.infoURLs = infoURLs;
        this.wikiURLs = wikiURLs;
        this.imageURL = imageURL;
        this.imageSizes = imageSizes;
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

    public String[] getWikiURLs() {
        return wikiURLs;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int[] getImageSizes() {
        return imageSizes;
    }
}
