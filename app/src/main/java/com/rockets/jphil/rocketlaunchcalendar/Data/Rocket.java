package com.rockets.jphil.rocketlaunchcalendar.Data;

import com.rockets.jphil.rocketlaunchcalendar.Database.RocketDB;

public class Rocket {

    private int id;
    private String name;
    private String configuration;
    private String familyname;

    private Agency[] agencies;
    private String wikiURL;
    private String[] infoURLs;
    private String imageURL;
    private int[] imageSizes;

    public Rocket(int id, String name, String configuration, String familyname, Agency[] agencies,
                  String wikiURLs, String[] infoURLs, String imageURL, int[] imageSizes) {
        this.id = id;
        this.name = name;
        this.configuration = configuration;
        this.familyname = familyname;
        this.agencies = agencies;
        this.wikiURL = wikiURLs;
        this.infoURLs = infoURLs;
        this.imageURL = imageURL;
        this.imageSizes = imageSizes;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getConfiguration() {
        return configuration;
    }

    public String getFamilyname() {
        return familyname;
    }

    public Agency[] getAgencies() {
        return agencies;
    }

    public String getWikiURL() {
        return wikiURL;
    }

    public String[] getInfoURLs() {
        return infoURLs;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int[] getImageSizes() {
        return imageSizes;
    }

    public RocketDB getEntity(){
        RocketDB db = new RocketDB();

        db.id = id;
        db.name = name;
        db.configuration = configuration;
        db.familyname = familyname;
        db.agencies = "";
        if(agencies != null) {
            for (Agency a : agencies) {
                db.agencies += a.getId() + ",";
            }
        }

        db.wikiURL = wikiURL;
        db.infoURLs = "";
        if(infoURLs != null) {
            for (String s : infoURLs) {
                db.infoURLs += s + " ";
            }
        }
        db.imageURL = imageURL;

        db.imageSizes = "";
        if(imageSizes != null) {
            for (int i : imageSizes) {
                db.imageSizes += i + ",";
            }
        }

        return db;
    }
}
