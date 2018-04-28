package com.rockets.jphil.rocketlaunchcalendar.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.rockets.jphil.rocketlaunchcalendar.Data.Payload;

@Entity
public class PayloadDB {

    @PrimaryKey
    public int id;
    public String name;

    public Payload getPayload(){
        return new Payload(id, name);
    }
}
