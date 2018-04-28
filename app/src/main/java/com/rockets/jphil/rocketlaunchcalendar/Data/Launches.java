package com.rockets.jphil.rocketlaunchcalendar.Data;

import android.util.Log;

import com.rockets.jphil.rocketlaunchcalendar.Data.RocketLaunch;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Launches {

    private ArrayList<RocketLaunch> launches;
    private int total;
    private int offset;
    private int count;

    public Launches(ArrayList<RocketLaunch> launches, int total, int offset, int count) {
        this.launches = launches;
        this.total = total;
        this.offset = offset;
        this.count = count;

        sortLaunchesByDate();
    }

//    public Launches(ArrayList<RocketLaunch> launches){
//        this.launches = launches;
//        sortLaunchesByDate();
//    }

    public ArrayList<RocketLaunch> getLaunches() {
        return launches;
    }

    public int getTotal() {
        return total;
    }

    public int getOffset() {
        return offset;
    }

    public int getCount() {
        return count;
    }

    public int getLaunchesSize(){
        return launches.size();
    }

    private void sortLaunchesByDate(){
        int changes;
        int size = launches.size();
        Date dateZero = new Date(10);
        do{
            changes = 0;
            for(int x = 0; x < size - 1; x++){
                for(int y = x + 1; y < size; y++){
                    if(launches.get(x).getWindowNETDate().after(launches.get(y).getWindowNETDate())){
                        if(launches.get(x).getWindowNETDate().after(new Date(0))){
                            Collections.swap(launches, x, y);
                            changes++;
                        }
                    }
                    else if(launches.get(x).getWindowNETDate().before(dateZero)){
                        Collections.swap(launches, x, size-1);
                        changes++;
                        size--;
                    }
                }
            }
        }while (changes != 0);

        for(RocketLaunch L : launches){
            Log.d("Sort", L.getName() + " date: " + L.getWindowNETDate().toString());
        }
    }
}
