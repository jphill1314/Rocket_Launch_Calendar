package com.rockets.jphil.rocketlaunchcalendar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView missionName, missionDate, missionTime, rocketName;

        ViewHolder(View view){
            super(view);

            missionName = view.findViewById(R.id.mission_name);
            missionDate = view.findViewById(R.id.mission_date);
            missionTime = view.findViewById(R.id.mission_time);
            rocketName = view.findViewById(R.id.rocket_name);
        }
    }

    ArrayList<RocketLaunch> launches;
    public CalendarAdapter(ArrayList<RocketLaunch> launches){
        this.launches = launches;
    }

    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_calendar, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.missionName.setText(launches.get(position).getMissionName());
        holder.missionDate.setText(launches.get(position).getMissionDate());
        holder.missionTime.setText(launches.get(position).getMissionTime());
        holder.rocketName.setText(launches.get(position).getRocketName());
    }

    @Override
    public int getItemCount(){
        return (launches != null) ? launches.size() : 0;
    }
}
