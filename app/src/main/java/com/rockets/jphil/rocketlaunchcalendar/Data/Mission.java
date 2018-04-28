package com.rockets.jphil.rocketlaunchcalendar.Data;

public class Mission {

    private int id;
    private String name;
    private String description;
    private int type;
    private String wikiURL;
    private String typeName;
    private Agency[] agencies;
    private Payload[] payloads;

    public Mission(int id, String name, String description, int type, String wikiURL,
                   String typeName, Agency[] agencies, Payload[] payloads) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.wikiURL = wikiURL;
        this.typeName = typeName;
        this.agencies = agencies;
        this.payloads = payloads;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }

    public String getWikiURL() {
        return wikiURL;
    }

    public String getTypeName() {
        return typeName;
    }

    public Agency[] getAgencies() {
        return agencies;
    }

    public Payload[] getPayloads() {
        return payloads;
    }
}
