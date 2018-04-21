package com.rockets.jphil.rocketlaunchcalendar;

import android.util.Log;

public class RocketLaunch {

    private String missionName;
    private String missionDate;
    private String missionTime;
    private String rocketName;

    private int id;
    private String name;
    private String windowstart;
    private String windowend;
    private String net;
    private int status;
    private int tbdtime;
    private int tbddate;
    private int probability;
    private String changed;
    private String lsp;

    public RocketLaunch(int id, String name, String windowstart, String windowend, String net, int status, int tbdtime, int tbddate, int probability, String changed, String lsp) {
        this.id = id;
        this.name = name;
        this.windowstart = windowstart;
        this.windowend = windowend;
        this.net = net;
        this.status = status;
        this.tbdtime = tbdtime;
        this.tbddate = tbddate;
        this.probability = probability;
        this.changed = changed;
        this.lsp = lsp;
    }

    public String getMissionName() {
        return missionName;
    }

    public String getMissionDate() {
        return missionDate;
    }

    public String getMissionTime() {
        return missionTime;
    }

    public String getRocketName() {
        return rocketName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWindowstart() {
        return windowstart;
    }

    public String getWindowend() {
        return windowend;
    }

    public String getNet() {
        return net;
    }

    public int getStatus() {
        return status;
    }

    public int getTbdtime() {
        return tbdtime;
    }

    public int getTbddate() {
        return tbddate;
    }

    public int getProbability() {
        return probability;
    }

    public String getChanged() {
        return changed;
    }

    public String getLsp() {
        return lsp;
    }

    public void setUpStrings(){
        rocketName = name.substring(0, name.indexOf("|")).trim();
        missionName = name.substring(name.indexOf("|") + 2).trim();
        missionDate = net.substring(0, net.indexOf(",") + 6).trim();
        missionTime = net.substring(net.indexOf(",") + 6).trim();
    }
}
