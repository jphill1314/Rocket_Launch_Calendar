package com.rockets.jphil.rocketlaunchcalendar.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.rockets.jphil.rocketlaunchcalendar.Data.Agency;

@Entity
public class AgencyDB {

    @PrimaryKey
    public int id;
    public String name;
    public String abbrev;
    public String countryCode;
    public int type;
    public String infoURL;
    public String wikiURL;
    public String changed;
    public String infoURLs;
    public String wikiURLs;
    public String imageURL;
    public String imageSizes;

    public Agency getAgency(){
        String[] info = (infoURLs != null) ? infoURLs.split(" ") : null;
        String[] wiki = (wikiURLs != null) ? wikiURLs.split(" ") : null;
        String[] sizes = (imageSizes != null && imageSizes.length() > 0) ? imageSizes.split(",") : null;
        int[] images = (sizes != null) ? new int[sizes.length] : null;
        if(images != null){
            for(int x = 0; x < images.length; x++){
                images[x] = Integer.parseInt(sizes[x]);
            }
        }

        return new Agency(id, name, abbrev, countryCode, type, infoURL, wikiURL, changed, info, wiki,
                imageURL, images);
    }
}
