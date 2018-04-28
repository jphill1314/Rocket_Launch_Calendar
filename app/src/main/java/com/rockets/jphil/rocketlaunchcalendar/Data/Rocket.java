package com.rockets.jphil.rocketlaunchcalendar.Data;

public class Rocket {

    private int id;
    private String name;
    private String configuration;
    private String familyname;

    private Agency[] agencies;
    private Mission[] missions;
    private LSP lsp;

    public Rocket(int id, String name, String configuration, String familyname, Agency[] agencies,
                  Mission[] missions, LSP lsps) {
        this.id = id;
        this.name = name;
        this.configuration = configuration;
        this.familyname = familyname;
        this.agencies = agencies;
        this.missions = missions;
        this.lsp = lsps;
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

    public Mission[] getMissions() {
        return missions;
    }

    public LSP getLsp() {
        return lsp;
    }
}
