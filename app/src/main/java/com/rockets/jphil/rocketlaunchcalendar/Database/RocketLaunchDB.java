package com.rockets.jphil.rocketlaunchcalendar.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class RocketLaunchDB {

    @PrimaryKey
    public int id;

    public String name;
    public String windowStart;
    public String windowEnd;
    public String net;
    public int windowStartStamp;
    public int windowEndStamp;
    public int netStamp;
    public int status;
    public int inHold;
    public int tbdTime;
}
