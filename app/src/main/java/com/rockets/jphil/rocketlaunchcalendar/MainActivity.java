package com.rockets.jphil.rocketlaunchcalendar;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rockets.jphil.rocketlaunchcalendar.Data.Agency;
import com.rockets.jphil.rocketlaunchcalendar.Data.IntTypeAdapter;
import com.rockets.jphil.rocketlaunchcalendar.Data.LSP;
import com.rockets.jphil.rocketlaunchcalendar.Data.LaunchLibraryService;
import com.rockets.jphil.rocketlaunchcalendar.Data.Launches;
import com.rockets.jphil.rocketlaunchcalendar.Data.Location;
import com.rockets.jphil.rocketlaunchcalendar.Data.Mission;
import com.rockets.jphil.rocketlaunchcalendar.Data.Pad;
import com.rockets.jphil.rocketlaunchcalendar.Data.Payload;
import com.rockets.jphil.rocketlaunchcalendar.Data.Rocket;
import com.rockets.jphil.rocketlaunchcalendar.Data.RocketLaunch;
import com.rockets.jphil.rocketlaunchcalendar.Database.AgencyDB;
import com.rockets.jphil.rocketlaunchcalendar.Database.AppDatabase;
import com.rockets.jphil.rocketlaunchcalendar.Database.LSPDB;
import com.rockets.jphil.rocketlaunchcalendar.Database.LocationDB;
import com.rockets.jphil.rocketlaunchcalendar.Database.MissionDB;
import com.rockets.jphil.rocketlaunchcalendar.Database.PadDB;
import com.rockets.jphil.rocketlaunchcalendar.Database.PayloadDB;
import com.rockets.jphil.rocketlaunchcalendar.Database.RocketDB;
import com.rockets.jphil.rocketlaunchcalendar.Database.RocketLaunchDB;
import com.rockets.jphil.rocketlaunchcalendar.Fragments.CalendarFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public Launches launches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null || launches == null) {
            new DataAsync().execute("load");
        }
    }

    private void changeFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new CalendarFragment()).commit();
    }


    // TODO: it appears that missions and LSPs aren't being saved at all -> ie they don't exist after the network call
    private class DataAsync extends AsyncTask<String, String, String> {

        private AppDatabase database;
        private SharedPreferences preferences;

        @Override
        protected void onPreExecute() {
            if(database == null || !database.isOpen()){
                Log.d("Database", "Opening database");
                database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "rocket-launch").build();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            if(strings[0].equals("load")) {
                if (launches == null || launches.getLaunchesSize() == 0) {
                    preferences = getSharedPreferences(
                            getString(R.string.preferences_key), Context.MODE_PRIVATE);

                    Long lastTimeLoadedFromRemote = Long.parseLong(preferences.getString(
                            getString(R.string.preference_last_load_from_remote), "0"));
                    Log.d("Prefs", "Last Time Loaded = " + lastTimeLoadedFromRemote);
                    Long nowInMillis = Calendar.getInstance().getTimeInMillis();
                    Long hourInMillis = 3600000L;

                    if (lastTimeLoadedFromRemote + hourInMillis < nowInMillis) {
                        loadLaunchesFromRemote();
                    } else {
                        loadLaunches();

                        if (launches == null || launches.getLaunchesSize() == 0) {
                            loadLaunchesFromRemote();
                        }
                    }
                }
                return "loaded";
            }

            return "none";
        }

        @Override
        protected void onPostExecute(String result){
            if(database != null && database.isOpen()){
                Log.d("Database", "Closed the database");
                database.close();
            }

            changeFragment();
        }

        private void loadLaunchesFromRemote(){
            Log.d("Load", "Load from remote");

            Gson gson = new GsonBuilder().registerTypeAdapter(int.class, new IntTypeAdapter())
                    .registerTypeAdapter(Integer.class, new IntTypeAdapter()).create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.launchlibrary.net/1.4/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            LaunchLibraryService launchLibraryService = retrofit.create(LaunchLibraryService.class);
            Call<Launches> call = launchLibraryService.listLaunches(25);
            try {
                saveLaunches(new ArrayList<>(call.execute().body().getLaunches()));
                loadLaunches();

                preferences.edit().putString(getString(R.string.preference_last_load_from_remote),
                        Calendar.getInstance().getTimeInMillis() + "").apply();

            }catch (IOException e){
                Log.e("Error", "IOException", e);
            }
        }

        private void saveLaunches(ArrayList<RocketLaunch> launches){
            Log.d("save", "Saving");
            ArrayList<RocketLaunchDB> launchDBS = new ArrayList<>();
            ArrayList<RocketDB> rockets = new ArrayList<>();
            ArrayList<PayloadDB> payloads = new ArrayList<>();
            ArrayList<PadDB> pads = new ArrayList<>();
            ArrayList<MissionDB> missions = new ArrayList<>();
            ArrayList<LSPDB> lsps = new ArrayList<>();
            ArrayList<LocationDB> locations = new ArrayList<>();
            ArrayList<AgencyDB> agencies = new ArrayList<>();

            for(RocketLaunch launch : launches){
                launchDBS.add(launch.getEntity());

                if(launch.getRocket() != null){
                    Rocket r = launch.getRocket();
                    rockets.add(r.getEntity());

                    if(r.getAgencies() != null){
                        for(Agency a : r.getAgencies()){
                            agencies.add(a.getEntity());
                        }
                    }
                }

                if(launch.getLocation() != null){
                    Location l = launch.getLocation();
                    locations.add(l.getEntity());

                    if(l.getPads() != null){
                        for(Pad p : l.getPads()){
                            pads.add(p.getEntity());

                            if(p.getAgencies() != null){
                                for(Agency a : p.getAgencies()){
                                    agencies.add(a.getEntity());
                                }
                            }
                        }
                    }
                }

                if(launch.getLsp() != null){
                    lsps.add(launch.getLsp().getEntity());
                }

                if(launch.getMissions() != null){
                    for(Mission m : launch.getMissions()){
                        missions.add(m.getEntity());

                        if(m.getPayloads() != null){
                            for(Payload p : m.getPayloads()){
                                payloads.add(p.getEntity());
                            }
                        }

                        if(m.getAgencies() != null){
                            for(Agency a : m.getAgencies()){
                                agencies.add(a.getEntity());
                            }
                        }
                    }
                }
            }

            database.appDAO().insertLaunches(launchDBS);
            database.appDAO().insertAgencies(agencies);
            database.appDAO().insertLocations(locations);
            database.appDAO().insertLSPs(lsps);
            database.appDAO().insertMissions(missions);
            database.appDAO().insertPads(pads);
            database.appDAO().insertPayloads(payloads);
            database.appDAO().insertRockets(rockets);
            Log.d("Save", "Saved");
        }

        private void loadLaunches(){
            Log.d("Load", "Load from db");
            ArrayList<RocketLaunchDB> launchDBS = new ArrayList<>(database.appDAO().getAllLaunches());
            ArrayList<RocketLaunch> rocketLaunches = new ArrayList<>();

            ArrayList<LSP> lsps = new ArrayList<>();
            for(LSPDB db : database.appDAO().getAllLSPs()){
                lsps.add(db.getLSP());
            }

            ArrayList<Payload> payloads = new ArrayList<>();
            for(PayloadDB db : database.appDAO().getAllPayloads()){
                payloads.add(db.getPayload());
            }

            ArrayList<Agency> agencies = new ArrayList<>();
            for(AgencyDB db : database.appDAO().getAllAgencies()){
                agencies.add(db.getAgency());
            }

            for(RocketLaunchDB db: launchDBS){
                String[] vidURLS = (db.vidURLs != null) ? db.vidURLs.split(" ") : null;
                String[] infoURLS = (db.infoURLs != null) ? db.infoURLs.split(" ") : null;

                LocationDB lDB = database.appDAO().getLocationWithID(db.locationID);
                Pad[] pads = null;
                if(lDB != null && lDB.pads != null){
                    String[] ids = lDB.pads.split(",");
                    pads = new Pad[ids.length];
                    int index = 0;
                    for(String s : ids){
                        int id = Integer.parseInt(s);
                        PadDB padDB = database.appDAO().getPadWithID(id);
                        if(padDB != null) {
                            String[] aIDs = (padDB.agencies != null && padDB.agencies.length() > 0) ? padDB.agencies.split(",") : null;
                            Agency[] padAgencies = (aIDs != null) ? new Agency[aIDs.length] : null;
                            if (aIDs != null) {
                                int aIndex = 0;
                                for (String i : aIDs) {
                                    int aId = Integer.parseInt(i);
                                    for (Agency a : agencies) {
                                        if (a.getId() == aId && aIndex < padAgencies.length) {
                                            padAgencies[aIndex] = a;
                                            aIndex++;
                                        }
                                    }
                                }
                            }


                            if (index < pads.length) {
                                pads[index] = padDB.getPad(padAgencies);
                                index++;
                            }
                        }
                    }
                }
                Location location = lDB.getLocation(pads);


                RocketDB rocketDB = database.appDAO().getRocketWithID(db.rocket);
                Agency[] rAgencies = null;
                Mission[] missions = null;

                if(rocketDB != null){

                    if(rocketDB.agencies != null){
                        String[] ids = (rocketDB.agencies.length() > 0) ? rocketDB.agencies.split(",") : null;
                        rAgencies = (ids != null) ? new Agency[ids.length] : null;

                        if(rAgencies != null) {
                            int index = 0;
                            for (String s : ids) {
                                int id = Integer.parseInt(s);
                                for (Agency a : agencies) {
                                    if (a.getId() == id && index < rAgencies.length) {
                                        rAgencies[index] = a;
                                        index++;
                                    }
                                }
                            }
                        }
                    }

                }


                Rocket rocket = rocketDB.getRocket(rAgencies);
                LSP lsp = null;

                for(LSP l : lsps){
                    if(db.lsp == l.getId()){
                        lsp = l;
                    }
                }

                if(db.missions != null && db.missions.length() > 0){
                    String[] ids = db.missions.split(",");
                    missions = new Mission[ids.length];
                    int index = 0;
                    for(String s : ids){
                        MissionDB missionDB = database.appDAO().getMissionWithID(Integer.parseInt(s));
                        Payload[] mPayloads = null;
                        Agency[] mAgencies = null;

                        if(missionDB.agencies != null && missionDB.agencies.length() > 0){
                            String[] aIds = missionDB.agencies.split(",");
                            mAgencies = new Agency[aIds.length];
                            int aIndex = 0;

                            for(String string : aIds){
                                int aID = Integer.parseInt(string);
                                for(Agency a : agencies){
                                    if(a.getId() == aID && aIndex < mAgencies.length){
                                        mAgencies[aIndex] = a;
                                        aIndex++;
                                    }
                                }
                            }
                        }

                        if(missionDB.payloads != null && missionDB.payloads.length() > 0){
                            String[] pIds = missionDB.payloads.split(",");
                            mPayloads = new Payload[pIds.length];
                            int pIndex = 0;

                            for(String string : pIds){
                                int pId = Integer.parseInt(string);
                                for(Payload p : payloads){
                                    if(p.getId() == pId && pIndex < mPayloads.length){
                                        mPayloads[pIndex] = p;
                                        pIndex++;
                                    }
                                }
                            }
                        }

                        missions[index] = missionDB.getMission(mPayloads, mAgencies);
                        index++;
                    }
                }

                rocketLaunches.add(new RocketLaunch(db.id, db.name, db.windowstart, db.windowend, db.net,
                        db.wssstamp, db.wesstamp, db.netstamp, db.isostart, db.isoend, db.isonet,
                        db.status, db.tbdtime, vidURLS, db.vidURL, infoURLS, db.infoURL,
                        db.holdreason, db.failreason, db.tbddate, db.probability, db.hashtag,
                        db.changed, location, rocket, missions, lsp));
            }

            Log.d("Load", "init Launches: " + rocketLaunches.size());
            launches = new Launches(rocketLaunches, 0, 0, 0);
            Log.d("Load", "Finished loading");
        }
    }
}
