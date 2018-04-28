package com.rockets.jphil.rocketlaunchcalendar.Data;

import com.rockets.jphil.rocketlaunchcalendar.Database.PayloadDB;

public class Payload {

    private int id;
    private String name;

    public Payload(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PayloadDB getEntity(){
        PayloadDB db = new PayloadDB();

        db.id = id;
        db.name = name;

        return db;
    }
}
