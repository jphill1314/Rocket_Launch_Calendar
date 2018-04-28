package com.rockets.jphil.rocketlaunchcalendar;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rockets.jphil.rocketlaunchcalendar.Adapters.CalendarAdapter;
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
                RocketLaunchDB rocketLaunchDB = new RocketLaunchDB();
                rocketLaunchDB.id = launch.getId();
                rocketLaunchDB.name = launch.getName();
                rocketLaunchDB.windowstart = launch.getWindowstart();
                rocketLaunchDB.windowend = launch.getWindowend();
                rocketLaunchDB.net = launch.getNet();
                rocketLaunchDB.wssstamp = launch.getWssstamp();
                rocketLaunchDB.wesstamp = launch.getWesstamp();
                rocketLaunchDB.isostart = launch.getIsostart();
                rocketLaunchDB.isoend = launch.getIsoend();
                rocketLaunchDB.isonet = launch.getIsonet();
                rocketLaunchDB.status = launch.getStatus();
                rocketLaunchDB.tbdtime = launch.getTbdtime();
                rocketLaunchDB.tbddate = launch.getTbddate();
                rocketLaunchDB.probability = launch.getProbability();

                rocketLaunchDB.vidURL = launch.getVidURL();
                if(launch.getVidURLs() != null){
                    String urls = "";
                    for(String url : launch.getVidURLs()){
                        urls += url + " ";
                    }
                    urls = urls.trim();
                    rocketLaunchDB.vidURLs = urls;
                }

                rocketLaunchDB.infoURL = launch.getInfoURL();
                if(launch.getInfoURLs() != null){
                    String urls = "";
                    for(String url : launch.getInfoURLs()){
                        urls += url + " ";
                    }
                    urls = urls.trim();
                    rocketLaunchDB.infoURLs = urls;
                }

                rocketLaunchDB.holdreason = launch.getHoldreason();
                rocketLaunchDB.failreason = launch.getFailreason();
                rocketLaunchDB.hashtag = launch.getHashtag();

                if(launch.getLocation() != null){
                    rocketLaunchDB.locationID = launch.getLocation().getId();

                    Location l = launch.getLocation();
                    LocationDB locationDB = new LocationDB();
                    locationDB.id = l.getId();
                    locationDB.name = l.getName();
                    locationDB.infoURL = l.getInfoURL();
                    locationDB.wikiURL = l.getWikiURL();
                    locationDB.countryCode = l.getCountryCode();

                    if(l.getPads() != null){
                        String padIds = "";
                        for(Pad p : l.getPads()){
                            padIds += p.getId() + ",";

                            PadDB padDB = new PadDB();
                            padDB.id = p.getId();
                            padDB.name = p.getName();
                            padDB.infoURL = p.getInfoURL();
                            padDB.wikiURL = p.getWikiURL();
                            padDB.mapURL = p.getMapURL();
                            padDB.latitude = p.getLatitude();
                            padDB.longitude = p.getLongitude();

                            if(p.getAgencies() != null){
                                String agIDs = "";
                                for(Agency a : p.getAgencies()){
                                    agIDs += a.getId() + ",";

                                    AgencyDB agencyDB = new AgencyDB();
                                    agencyDB.id = a.getId();
                                    agencyDB.abbrev = a.getAbbrev();
                                    agencyDB.countryCode = a.getCountryCode();
                                    agencyDB.type = a.getType();
                                    agencyDB.infoURL = a.getInfoURL();
                                    agencyDB.wikiURL = a.getWikiURL();
                                    agencyDB.changed = a.getChanged();
                                    agencyDB.infoURLs = "";
                                    if(a.getInfoURLs() != null) {
                                        for (String s : a.getInfoURLs()) {
                                            agencyDB.infoURLs += s + " ";
                                        }
                                    }
                                    agencyDB.wikiURLs = "";
                                    if(a.getWikiURLs() != null) {
                                        for (String s : a.getWikiURLs()) {
                                            agencyDB.wikiURLs += s + " ";
                                        }
                                    }

                                    agencyDB.imageURL = a.getImageURL();
                                    agencyDB.imageSizes = "";
                                    if(a.getImageSizes() != null) {
                                        for (int i : a.getImageSizes()) {
                                            agencyDB.imageSizes += i + ",";
                                        }
                                    }

                                    agencies.add(agencyDB);
                                }
                                padDB.agencies = agIDs;
                            }
                            pads.add(padDB);
                        }

                        locationDB.pads = padIds.substring(0, padIds.lastIndexOf(',')-1);
                    }
                    locations.add(locationDB);
                }

                if(launch.getRocket() != null){
                    rocketLaunchDB.rocket = launch.getRocket().getId();

                    Rocket r = launch.getRocket();
                    RocketDB rocket = new RocketDB();
                    rocket.id = r.getId();
                    rocket.name = r.getName();
                    rocket.configuration = r.getConfiguration();
                    rocket.familyname = r.getFamilyname();

                    if(r.getAgencies() != null){
                        String agIDs = "";
                        for(Agency a : r.getAgencies()){
                            agIDs += a.getId() + ",";

                            AgencyDB agencyDB = new AgencyDB();
                            agencyDB.id = a.getId();
                            agencyDB.abbrev = a.getAbbrev();
                            agencyDB.countryCode = a.getCountryCode();
                            agencyDB.type = a.getType();
                            agencyDB.infoURL = a.getInfoURL();
                            agencyDB.wikiURL = a.getWikiURL();
                            agencyDB.changed = a.getChanged();
                            agencyDB.infoURLs = "";
                            if(a.getInfoURLs() != null) {
                                for (String s : a.getInfoURLs()) {
                                    agencyDB.infoURLs += s + " ";
                                }
                            }
                            agencyDB.wikiURLs = "";
                            if(a.getWikiURLs() != null) {
                                for (String s : a.getWikiURLs()) {
                                    agencyDB.wikiURLs += s + " ";
                                }
                            }

                            agencyDB.imageURL = a.getImageURL();
                            agencyDB.imageSizes = "";
                            if(a.getImageSizes() != null) {
                                for (int i : a.getImageSizes()) {
                                    agencyDB.imageSizes += i + ",";
                                }
                            }

                            agencies.add(agencyDB);
                        }
                        rocket.agencies = (agIDs.contains(",")) ? agIDs.substring(0, agIDs.lastIndexOf(",")-1) : rocket.agencies;
                    }

                    if(r.getMissions() != null){
                        String missionIds = "";
                        for(Mission m : r.getMissions()){
                            missionIds += m.getId() + ",";

                            MissionDB mission = new MissionDB();
                            mission.id = m.getId();
                            mission.name = m.getName();
                            mission.description = m.getDescription();
                            mission.type = m.getType();
                            mission.wikiURL = m.getWikiURL();
                            mission.typeName = m.getTypeName();

                            if(m.getPayloads() != null){
                                String payLoadIds = "";
                                for(Payload p: m.getPayloads()){
                                    payLoadIds += p.getId() + ",";

                                    PayloadDB payload = new PayloadDB();
                                    payload.id = p.getId();
                                    payload.name = p.getName();

                                    payloads.add(payload);
                                }
                                mission.payloads = payLoadIds.substring(0, payLoadIds.lastIndexOf(',')-1);
                            }

                            if(m.getAgencies() != null){
                                String agIDs = "";
                                for(Agency a : m.getAgencies()){
                                    agIDs += a.getId() + ",";

                                    AgencyDB agencyDB = new AgencyDB();
                                    agencyDB.id = a.getId();
                                    agencyDB.abbrev = a.getAbbrev();
                                    agencyDB.countryCode = a.getCountryCode();
                                    agencyDB.type = a.getType();
                                    agencyDB.infoURL = a.getInfoURL();
                                    agencyDB.wikiURL = a.getWikiURL();
                                    agencyDB.changed = a.getChanged();
                                    agencyDB.infoURLs = "";
                                    for(String s: a.getInfoURLs()){
                                        agencyDB.infoURLs += s + " ";
                                    }
                                    agencyDB.infoURLs = agencyDB.infoURLs.trim();

                                    agencyDB.wikiURLs = "";
                                    for(String s : a.getWikiURLs()){
                                        agencyDB.wikiURLs += s + " ";
                                    }
                                    agencyDB.wikiURLs = agencyDB.wikiURLs.trim();

                                    agencyDB.imageURL = a.getImageURL();
                                    agencyDB.imageSizes = "";
                                    for(int i: a.getImageSizes()){
                                        agencyDB.imageSizes += i + ",";
                                    }
                                    agencyDB.imageSizes = agencyDB.imageSizes.trim();

                                    agencies.add(agencyDB);
                                }
                                mission.agencies = agIDs.substring(0, agIDs.lastIndexOf(',')-1);
                            }
                            missions.add(mission);
                        }
                        rocket.missions = missionIds;
                    }

                    if(r.getLsp() != null) {
                        rocket.lsp = r.getLsp().getId();

                        LSP lsp = r.getLsp();
                        LSPDB lspdb = new LSPDB();
                        lspdb.id = lsp.getId();
                        lspdb.name = lsp.getName();
                        lspdb.abbrev = lsp.getAbbrev();
                        lspdb.countryCode = lsp.getCountryCode();
                        lspdb.type = lsp.getType();
                        lspdb.infoURL = lsp.getInfoURL();
                        lspdb.wikiURL = lsp.getWikiURL();
                        lspdb.changed = lsp.getChanged();

                        if (lsp.getInfoURLs() != null) {
                            lspdb.infoURLs = "";
                            for (String url : lsp.getInfoURLs()) {
                                lspdb.infoURLs += url + " ";
                            }
                            lspdb.infoURLs = lspdb.infoURLs.trim();
                        }

                        lsps.add(lspdb);
                    }
                    rockets.add(rocket);
                }


                launchDBS.add(rocketLaunchDB);
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

            // TODO: load all data from database

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
                            String[] aIDs = (padDB.agencies != null) ? padDB.agencies.split(",") : null;
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
                LSP lsp = null;

                if(rocketDB != null){
                    for(LSP l: lsps){
                        if(l.getId() == rocketDB.lsp){
                            lsp = l;
                        }
                    }

                    if(rocketDB.agencies != null){
                        String[] ids = rocketDB.agencies.split(",");
                        rAgencies = new Agency[ids.length];
                        int index = 0;
                        for(String s : ids){
                            int id = Integer.parseInt(s);
                            for(Agency a : agencies){
                                if(a.getId() == id && index < rAgencies.length){
                                    rAgencies[index] = a;
                                    index++;
                                }
                            }
                        }
                    }

                    if(rocketDB.missions != null){
                        String[] ids = rocketDB.missions.split(",");
                        missions = new Mission[ids.length];
                        int index = 0;
                        for(String s : ids){
                            MissionDB missionDB = database.appDAO().getMissionWithID(Integer.parseInt(s));
                            Payload[] mPayloads = null;
                            Agency[] mAgencies = null;

                            if(missionDB.agencies != null){
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

                            if(missionDB.payloads != null){
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
                }


                Rocket rocket = rocketDB.getRocket(rAgencies, missions, lsp);

                rocketLaunches.add(new RocketLaunch(db.id, db.name, db.windowstart, db.windowend, db.net,
                        db.wssstamp, db.wesstamp, db.netstamp, db.isostart, db.isoend, db.isonet,
                        db.status, db.tbdtime, vidURLS, db.vidURL, infoURLS, db.infoURL,
                        db.holdreason, db.failreason, db.tbddate, db.probability, db.hashtag,
                        db.changed, location, rocket));
            }

            Log.d("Load", "init Launches: " + rocketLaunches.size());
            launches = new Launches(rocketLaunches, 0, 0, 0);
            Log.d("Load", "Finished loading");
        }
    }
}
