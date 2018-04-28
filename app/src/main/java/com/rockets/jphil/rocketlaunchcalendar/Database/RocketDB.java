package com.rockets.jphil.rocketlaunchcalendar.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class RocketDB {

    @PrimaryKey
    public int id;
    public String name;
    public String configuration;
    public String familyname;

    public String agencies;
    public String missions;
    public int lsp;
}
