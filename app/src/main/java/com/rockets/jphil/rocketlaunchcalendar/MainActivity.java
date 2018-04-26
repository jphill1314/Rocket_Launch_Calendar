package com.rockets.jphil.rocketlaunchcalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.rockets.jphil.rocketlaunchcalendar.Fragments.CalendarFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout, new CalendarFragment()).commit();
        }
    }
}
