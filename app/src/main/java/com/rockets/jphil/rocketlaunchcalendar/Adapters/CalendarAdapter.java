package com.rockets.jphil.rocketlaunchcalendar.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rockets.jphil.rocketlaunchcalendar.Data.RocketLaunch;
import com.rockets.jphil.rocketlaunchcalendar.Fragments.RocketLaunchInfoFragment;
import com.rockets.jphil.rocketlaunchcalendar.MainActivity;
import com.rockets.jphil.rocketlaunchcalendar.R;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView missionName, missionDate, missionTime, rocketName;
        View view;

        ViewHolder(View view){
            super(view);

            missionName = view.findViewById(R.id.mission_name);
            missionDate = view.findViewById(R.id.mission_date);
            missionTime = view.findViewById(R.id.mission_time);
            rocketName = view.findViewById(R.id.rocket_name);

            this.view = view;
        }
    }

    ArrayList<RocketLaunch> launches;
    Context context;
    public CalendarAdapter(ArrayList<RocketLaunch> launches){
        this.launches = launches;
    }

    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_calendar, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.missionName.setText(launches.get(position).getMissionName());
        holder.missionDate.setText(launches.get(position).getMissionDate());
        holder.missionTime.setText(launches.get(position).getMissionTime());
        holder.rocketName.setText(launches.get(position).getRocketName());


        final int pos = position;
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RocketLaunchInfoFragment fragment = new RocketLaunchInfoFragment();
                Bundle args = new Bundle();
                args.putInt("launch", pos);
                fragment.setArguments(args);

                ((MainActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, fragment).addToBackStack("launches")
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount(){
        return (launches != null) ? launches.size() : 0;
    }
}
