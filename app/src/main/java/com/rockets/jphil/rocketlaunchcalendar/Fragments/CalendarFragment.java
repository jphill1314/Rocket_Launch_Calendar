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

import com.rockets.jphil.rocketlaunchcalendar.Adapters.CalendarAdapter;
import com.rockets.jphil.rocketlaunchcalendar.Data.LaunchLibraryService;
import com.rockets.jphil.rocketlaunchcalendar.Data.Launches;
import com.rockets.jphil.rocketlaunchcalendar.Database.AppDatabase;
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

    ArrayList<RocketLaunch> launches;
    RecyclerView recyclerView;

    /*
    * TODO:
    *   Sort list of launches by NET date
    *   Make list items look nice
    *   Show countdown for the next launch
    *   Clicking on an item should show more info
    *   Store all info that the API gives when doing a verbose call
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
                if (launches == null || launches.size() == 0) {
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

                        if (launches == null || launches.size() == 0) {
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
                recyclerView.setAdapter(new CalendarAdapter(launches));
                recyclerView.getAdapter().notifyDataSetChanged();
                Log.d("Async", "Loaded in adapter");
            }
        }

        private void loadLaunchesFromRemote(){
            Log.d("Load", "Load from remote");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.launchlibrary.net/1.4/")
                    .addConverterFactory(GsonConverterFactory.create())
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

            for(RocketLaunch launch : launches){
                RocketLaunchDB rocketLaunchDB = new RocketLaunchDB();
                rocketLaunchDB.id = launch.getId();
                rocketLaunchDB.name = launch.getName();
                rocketLaunchDB.windowStart = launch.getWindowstart();
                rocketLaunchDB.windowEnd = launch.getWindowend();
                rocketLaunchDB.net = launch.getNet();
                launchDBS.add(rocketLaunchDB);
            }

            database.appDAO().insertLaunches(launchDBS);
            Log.d("Save", "Saved");
        }

        private void loadLaunches(){
            Log.d("Load", "Load from db");
            ArrayList<RocketLaunchDB> launchDBS = new ArrayList<>(database.appDAO().getAllLaunches());
            if(launches == null){
                launches = new ArrayList<>();
            }

            for(RocketLaunchDB db: launchDBS){
                launches.add(new RocketLaunch(db.id, db.name, db.windowStart, db.windowEnd, db.net));
            }
        }
    }
}
