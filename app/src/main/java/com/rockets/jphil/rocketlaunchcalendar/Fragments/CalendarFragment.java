package com.rockets.jphil.rocketlaunchcalendar.Fragments;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.rockets.jphil.rocketlaunchcalendar.Database.AgencyDB;
import com.rockets.jphil.rocketlaunchcalendar.Database.AppDatabase;
import com.rockets.jphil.rocketlaunchcalendar.Database.LSPDB;
import com.rockets.jphil.rocketlaunchcalendar.Database.LocationDB;
import com.rockets.jphil.rocketlaunchcalendar.Database.MissionDB;
import com.rockets.jphil.rocketlaunchcalendar.Database.PadDB;
import com.rockets.jphil.rocketlaunchcalendar.Database.PayloadDB;
import com.rockets.jphil.rocketlaunchcalendar.Database.RocketDB;
import com.rockets.jphil.rocketlaunchcalendar.Database.RocketLaunchDB;
import com.rockets.jphil.rocketlaunchcalendar.R;
import com.rockets.jphil.rocketlaunchcalendar.Data.RocketLaunch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

    public CalendarFragment() {
        // Required empty public constructor
    }

    //ArrayList<RocketLaunch> launches;
    Launches launches;
    RecyclerView recyclerView;

    /*
    * TODO:
    *   ~~Sort list of launches by NET date
    *   Make list items look nice
    *   Show countdown for the next launch
    *   Clicking on an item should show more info
    *   ~~Store all info that the API gives when doing a verbose call
    * */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        recyclerView = view.findViewById(R.id.calendar_list_view);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new CalendarAdapter(null));
        DividerItemDecoration decoration = (new DividerItemDecoration(getContext(), manager.getOrientation()));
        decoration.setDrawable(getContext().getDrawable(R.drawable.linedivider));
        recyclerView.addItemDecoration(decoration);

        new DataAsync().execute("load");

        return view;
    }

    private class DataAsync extends AsyncTask<String, String, String>{

        private AppDatabase database;
        private SharedPreferences preferences;

        @Override
        protected void onPreExecute() {
            if(database == null || !database.isOpen()){
                Log.d("Database", "Opening database");
                database = Room.databaseBuilder(getContext().getApplicationContext(), AppDatabase.class, "rocket-launch").build();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            if(strings[0].equals("load")) {
                if (launches == null || launches.getLaunchesSize() == 0) {
                    preferences = getContext().getSharedPreferences(
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

            if(result.equals("loaded")){
                recyclerView.setAdapter(new CalendarAdapter(launches.getLaunches()));
                recyclerView.getAdapter().notifyDataSetChanged();
                Log.d("Async", "Loaded in adapter");
            }
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
                    rocketLaunchDB.vidURLs = urls;
                }

                rocketLaunchDB.infoURL = launch.getInfoURL();
                if(launch.getInfoURLs() != null){
                    String urls = "";
                    for(String url : launch.getInfoURLs()){
                        urls += url + " ";
                    }
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
                                    for(String s: a.getInfoURLs()){
                                        agencyDB.infoURLs += s + " ";
                                    }
                                    agencyDB.wikiURLs = "";
                                    for(String s : a.getWikiURLs()){
                                        agencyDB.wikiURLs += s + " ";
                                    }

                                    agencyDB.imageURL = a.getImageURL();
                                    agencyDB.imageSizes = "";
                                    for(int i: a.getImageSizes()){
                                        agencyDB.imageSizes += i + ",";
                                    }

                                    agencies.add(agencyDB);
                                }
                                padDB.agencies = agIDs;
                            }
                            pads.add(padDB);
                        }

                        locationDB.pads = padIds;
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
                            for(String s: a.getInfoURLs()){
                                agencyDB.infoURLs += s + " ";
                            }
                            agencyDB.wikiURLs = "";
                            for(String s : a.getWikiURLs()){
                                agencyDB.wikiURLs += s + " ";
                            }

                            agencyDB.imageURL = a.getImageURL();
                            agencyDB.imageSizes = "";
                            for(int i: a.getImageSizes()){
                                agencyDB.imageSizes += i + ",";
                            }

                            agencies.add(agencyDB);
                        }
                        rocket.agencies = agIDs;
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
                                mission.payloads = payLoadIds;
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
                                    agencyDB.wikiURLs = "";
                                    for(String s : a.getWikiURLs()){
                                        agencyDB.wikiURLs += s + " ";
                                    }

                                    agencyDB.imageURL = a.getImageURL();
                                    agencyDB.imageSizes = "";
                                    for(int i: a.getImageSizes()){
                                        agencyDB.imageSizes += i + ",";
                                    }

                                    agencies.add(agencyDB);
                                }
                                mission.agencies = agIDs;
                            }
                            missions.add(mission);
                        }
                        rocket.missions = missionIds;
                    }

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

                    if(lsp.getInfoURLs() != null){
                        lspdb.infoURLs = "";
                        for(String url: lsp.getInfoURLs()){
                            lspdb.infoURLs += url + " ";
                        }
                    }

                    lsps.add(lspdb);
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

            // TODO: load all data from database

            for(RocketLaunchDB db: launchDBS){
                rocketLaunches.add(new RocketLaunch(db.id, db.name, db.windowstart, db.windowend, db.net,
                        db.wssstamp, db.wesstamp, db.netstamp, db.isostart, db.isoend, db.isonet,
                        db.status, db.tbdtime, db.tbddate, db.probability, db.changed));
            }

            Log.d("Load", "init Launches: " + rocketLaunches.size());
            launches = new Launches(rocketLaunches, 0, 0, 0);
            Log.d("Load", "Finished loading");
        }
    }
}
