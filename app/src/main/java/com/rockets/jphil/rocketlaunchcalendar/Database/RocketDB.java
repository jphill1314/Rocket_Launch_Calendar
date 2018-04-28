package com.rockets.jphil.rocketlaunchcalendar.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.rockets.jphil.rocketlaunchcalendar.Data.Agency;
import com.rockets.jphil.rocketlaunchcalendar.Data.LSP;
import com.rockets.jphil.rocketlaunchcalendar.Data.Mission;
import com.rockets.jphil.rocketlaunchcalendar.Data.Rocket;

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

    public Rocket getRocket(Agency[] agencies, Mission[] missions, LSP lsp){
        return new Rocket(id, name, configuration, familyname, agencies, missions, lsp);
    }
}
