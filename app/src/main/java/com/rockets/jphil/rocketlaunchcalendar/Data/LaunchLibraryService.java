package com.rockets.jphil.rocketlaunchcalendar.Data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LaunchLibraryService {

    @GET("launch/next/{numLaunches}/mode/verbose")
    Call<Launches> listLaunches(@Path("numLaunches") int numLaunches);
}
