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
import com.rockets.jphil.rocketlaunchcalendar.MainActivity;
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

        if(((MainActivity)getActivity()).launches != null) {
            recyclerView = view.findViewById(R.id.calendar_list_view);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(new CalendarAdapter(((MainActivity) getActivity()).launches.getLaunches()));
            DividerItemDecoration decoration = (new DividerItemDecoration(getContext(), manager.getOrientation()));
            decoration.setDrawable(getContext().getDrawable(R.drawable.linedivider));
            recyclerView.addItemDecoration(decoration);
        }

        return view;
    }


}
