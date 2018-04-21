package com.rockets.jphil.rocketlaunchcalendar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LaunchLibraryService {

    @GET("launch")
    Call<Launches> listLaunches(@Query("next") int numLaunches);
}
