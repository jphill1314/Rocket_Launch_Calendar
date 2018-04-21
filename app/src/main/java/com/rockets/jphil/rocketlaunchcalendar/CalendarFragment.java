package com.rockets.jphil.rocketlaunchcalendar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.launchlibrary.net/1.4/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LaunchLibraryService launchLibraryService = retrofit.create(LaunchLibraryService.class);
        Call<Launches> call = launchLibraryService.listLaunches(15);
        recyclerView = view.findViewById(R.id.calendar_list_view);


        call.enqueue(new Callback<Launches>() {
            @Override
            public void onResponse(Call<Launches> call, Response<Launches> response) {
                launches = new ArrayList<>(response.body().getLaunches());
                populateRecyclerView();
            }

            @Override
            public void onFailure(Call<Launches> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });


        return view;
    }

    private void populateRecyclerView(){
        if(isAdded()) {
            for(RocketLaunch l : launches){
                l.setUpStrings();
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new CalendarAdapter(launches));
        }
    }
}
