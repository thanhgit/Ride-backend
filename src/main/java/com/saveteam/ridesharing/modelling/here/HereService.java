package com.saveteam.ridesharing.modelling.here;


import com.saveteam.ridesharing.modelling.here.model.Place;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HereService {
    @GET("/6.2/suggest.json")
    Call<Place> getSuggestions(@Query("app_id")String app_id,
                               @Query("app_code")String app_code,
                               @Query("query")String search,
                               @Query("beginHighlight")String begin,
                               @Query("endHighlight")String end);

}
