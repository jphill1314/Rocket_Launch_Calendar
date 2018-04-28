package com.rockets.jphil.rocketlaunchcalendar.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class RocketLaunchDB {

    @PrimaryKey
    public int id;

    public String name;
    public String windowstart;
    public String windowend;
    public String net;
    public int wssstamp;
    public int wesstamp;
    public int netstamp;
    public String isostart;
    public String isoend;
    public String isonet;
    public int status;
    public int tbdtime; // 1 = launch time is TBA
    public String vidURLs;
    public String vidURL;
    public String infoURLs;
    public String infoURL;
    public int holdreason;
    public int failreason;
    public int tbddate; // 1 = launch date is TBA
    public int probability;
    public String hashtag;
    public String changed;
    public int locationID;
    public int rocket;
    public String missions;
    public int lsp;
}
