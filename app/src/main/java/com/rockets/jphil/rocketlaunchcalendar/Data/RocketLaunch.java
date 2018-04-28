package com.rockets.jphil.rocketlaunchcalendar.Data;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private int wssstamp;
    private int wesstamp;
    private int netstamp;
    private String isostart;
    private String isoend;
    private String isonet;
    private int status;
    private int tbdtime; // 1 = launch time is TBA
    private String[] vidURLs;
    private String vidURL;
    private String[] infoURLs;
    private String infoURL;
    private int holdreason;
    private int failreason;
    private int tbddate; // 1 = launch date is TBA
    private int probability;
    private String hashtag;
    private String changed;
    private Location location;
    private Rocket rocket;

    public RocketLaunch(int id, String name, String windowstart, String windowend, String net,
                        int wssstamp, int wesstamp, int netstamp, String isostart, String isoend,
                        String isonet, int status, int tbdtime, int tbddate, int probability,
                        String changed) {
        this.id = id;
        this.name = name;
        this.windowstart = windowstart;
        this.windowend = windowend;
        this.net = net;
        this.wssstamp = wssstamp;
        this.wesstamp = wesstamp;
        this.netstamp = netstamp;
        this.isostart = isostart;
        this.isoend = isoend;
        this.isonet = isonet;
        this.status = status;
        this.tbdtime = tbdtime;
        this.tbddate = tbddate;
        this.probability = probability;
        this.changed = changed;

        setUpStrings();
    }

    public RocketLaunch(String missionName, String missionDate, String missionTime, String rocketName,
                        int id, String name, String windowstart, String windowend, String net,
                        int wssstamp, int wesstamp, int netstamp, String isostart, String isoend,
                        String isonet, int status, int tbdtime, String[] vidURLs, String vidURL,
                        String[] infoURLs, String infoURL, int holdreason, int failreason, int tbddate,
                        int probability, String hashtag, String changed, Location location, Rocket rocket) {
        this.missionName = missionName;
        this.missionDate = missionDate;
        this.missionTime = missionTime;
        this.rocketName = rocketName;
        this.id = id;
        this.name = name;
        this.windowstart = windowstart;
        this.windowend = windowend;
        this.net = net;
        this.wssstamp = wssstamp;
        this.wesstamp = wesstamp;
        this.netstamp = netstamp;
        this.isostart = isostart;
        this.isoend = isoend;
        this.isonet = isonet;
        this.status = status;
        this.tbdtime = tbdtime;
        this.vidURLs = vidURLs;
        this.vidURL = vidURL;
        this.infoURLs = infoURLs;
        this.infoURL = infoURL;
        this.holdreason = holdreason;
        this.failreason = failreason;
        this.tbddate = tbddate;
        this.probability = probability;
        this.hashtag = hashtag;
        this.changed = changed;
        this.location = location;
        this.rocket = rocket;

        setUpStrings();
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

    public int getWssstamp() {
        return wssstamp;
    }

    public int getWesstamp() {
        return wesstamp;
    }

    public int getNetstamp() {
        return netstamp;
    }

    public String getIsostart() {
        return isostart;
    }

    public String getIsoend() {
        return isoend;
    }

    public String getIsonet() {
        return isonet;
    }

    public String[] getVidURLs() {
        return vidURLs;
    }

    public String getVidURL() {
        return vidURL;
    }

    public String[] getInfoURLs() {
        return infoURLs;
    }

    public String getInfoURL() {
        return infoURL;
    }

    public int getHoldreason() {
        return holdreason;
    }

    public int getFailreason() {
        return failreason;
    }

    public String getHashtag() {
        return hashtag;
    }

    public Location getLocation() {
        return location;
    }

    public Rocket getRocket() {
        return rocket;
    }

    public Date getWindowNETDate(){

        DateFormat df = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        try {
            Date date = df.parse(isonet);
            if(tbddate == 1){
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.MONTH, 1);
                cal.set(Calendar.DATE, 1);
                cal.add(Calendar.DATE, -1);
                date = new Date(cal.getTimeInMillis());
            }
            return date;
        }catch (ParseException e){
            return new Date(0);
        }
    }

    private void setUpStrings(){
        rocketName = name.substring(0, name.indexOf("|")).trim();
        missionName = name.substring(name.indexOf("|") + 2).trim();

        if(tbddate == 1) {
            missionDate = net.substring(0, net.indexOf(",") - 2).trim() + " TBA";
        }
        else {
            missionDate = net.substring(0, net.indexOf(",")).trim();
        }

        if(tbdtime == 1) {
            missionTime = "TBA";
        }
        else {
            missionTime = net.substring(net.indexOf(",") + 6).trim();
        }
    }
}
