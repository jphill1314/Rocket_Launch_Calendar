package com.rockets.jphil.rocketlaunchcalendar;

import java.util.List;

public class Launches {

    private List<RocketLaunch> launches;
    private int total;
    private int offset;
    private int count;

    public Launches(List<RocketLaunch> launches, int total, int offset, int count) {
        this.launches = launches;
        this.total = total;
        this.offset = offset;
        this.count = count;
    }

    public List<RocketLaunch> getLaunches() {
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
}
