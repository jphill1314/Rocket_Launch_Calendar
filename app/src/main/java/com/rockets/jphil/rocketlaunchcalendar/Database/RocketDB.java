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
    public String wikiURL;
    public String infoURLs;
    public String imageURL;
    public String imageSizes;

    public Rocket getRocket(Agency[] agencies){
        String[] info = (infoURLs != null) ? infoURLs.split(" ") : null;
        String[] sizes = (imageSizes != null && imageSizes.length() > 0) ? imageSizes.split(",") : null;
        int[] images = (sizes != null) ? new int[sizes.length] : null;
        if(images != null){
            for(int x = 0; x < images.length; x++){
                images[x] = Integer.parseInt(sizes[x]);
            }
        }

        return new Rocket(id, name, configuration, familyname, agencies, wikiURL, info, imageURL, images);
    }
}
