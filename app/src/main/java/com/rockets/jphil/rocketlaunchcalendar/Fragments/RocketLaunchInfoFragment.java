package com.rockets.jphil.rocketlaunchcalendar.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rockets.jphil.rocketlaunchcalendar.Data.RocketLaunch;
import com.rockets.jphil.rocketlaunchcalendar.MainActivity;
import com.rockets.jphil.rocketlaunchcalendar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RocketLaunchInfoFragment extends Fragment {


    public RocketLaunchInfoFragment() {
        // Required empty public constructor
    }

    RocketLaunch launch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rocket_launch_info, container, false);

        Bundle args = getArguments();
        if(args != null){
            launch = ((MainActivity)getActivity()).launches.getLaunches().get(args.getInt("launch"));
        }

        if(launch != null){
            TextView rocketLaunch = view.findViewById(R.id.rocket_launch);
            rocketLaunch.setText(launch.getName());

            TextView rocket = view.findViewById(R.id.rocket);
            rocket.setText(launch.getRocket().getFamilyname());

            TextView payload = view.findViewById(R.id.payload);
            TextView mission = view.findViewById(R.id.mission);
            if(launch.getMissions() != null) {
                mission.setText(launch.getMissions()[0].getDescription());
                if(launch.getMissions()[0].getPayloads() != null && launch.getMissions()[0].getPayloads()[0] != null){
                    payload.setText(launch.getMissions()[0].getPayloads()[0].getName());
                }
                else{
                    payload.setText("No payloads found");
                }

            }
            else{
                mission.setText("No mission found");
                payload.setText("No mission found");
            }

            TextView pad = view.findViewById(R.id.pad);
            if(launch.getLocation().getPads() != null && launch.getLocation().getPads()[0] != null) {
                pad.setText(launch.getLocation().getPads()[0].getName());
            }
            else{
                pad.setText("No pad found");
            }


            TextView lsp = view.findViewById(R.id.lsp);
            if(launch.getLsp() != null) {
                lsp.setText(launch.getLsp().getName());
            }
            else{
                lsp.setText("No LSP fond");
            }

            TextView location = view.findViewById(R.id.location);
            location.setText(launch.getLocation().getName());

            TextView agency = view.findViewById(R.id.agency);
            if(launch.getRocket().getAgencies() != null && launch.getRocket().getAgencies()[0] != null) {
                agency.setText(launch.getRocket().getAgencies()[0].getName());
            }
            else{
                agency.setText("No agency found");
            }

        }

        return view;
    }

}
