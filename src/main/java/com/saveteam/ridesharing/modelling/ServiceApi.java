package com.saveteam.ridesharing.modelling;

import com.saveteam.ridesharing.modelling.firebase.FirebaseService;
import com.saveteam.ridesharing.modelling.firebase.FirebaseServiceApi;
import com.saveteam.ridesharing.modelling.firebase.model.Matching;
import com.saveteam.ridesharing.modelling.firebase.model.Query;
import com.saveteam.ridesharing.modelling.firebase.model.Trip;
import com.saveteam.ridesharing.modelling.firebase.model.TripFB;
import com.saveteam.ridesharing.modelling.here.HereService;
import com.saveteam.ridesharing.modelling.here.model.Place;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

@Service
public class ServiceApi {

    private HereService hereService;
    private FirebaseService firebaseService;

    private String app_id="xxx";
    private String app_code="xxx";
    private String begin="<b>";
    private String end="</b>";

    private Place place;

    private static ServiceApi instance;

    public static ServiceApi getInstance() {
        if (instance == null) {
            synchronized (ServiceApi.class) {
                if (instance == null) {
                    instance = new ServiceApi();
                }
            }
        }

        return instance;
    }

    public ServiceApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://autocomplete.geocoder.api.here.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        hereService = retrofit.create(HereService.class);

        firebaseService = new FirebaseServiceApi();
    }

    public ServiceApi(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        hereService = retrofit.create(HereService.class);
    }

    @Async
    public List<TripFB> getTrips() {
        return firebaseService.getTrips();
    }

    @Async
    public List<TripFB> getTripWithDatabaseAndTime(String db, long time) {
        return  firebaseService.getTripWithDatabaseAndTime(db, time);
    }

    @Async
    public List<Query> getQueries() {
        return firebaseService.getQueries();
    }

    @Async
    public void searchPlace(String namePlace) {
        Call<Place> res = hereService.getSuggestions(app_id, app_code,namePlace,begin,end);
        res.enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                System.out.println("size " + response.body().getSuggestions().size());
                place = response.body();
            }

            @Override
            public void onFailure(Call<Place> call, Throwable throwable) {

            }
        });
    }

    @Async
    public boolean insertMatching(Matching matching) {
        return firebaseService.insertMatching(matching);
    }

    public Place getPlace() {
        return place;
    }

    public static final void main(String[] args) {
        ServiceApi service = new ServiceApi();
        List<TripFB> trips = service.getTrips();
        System.out.println("..............................size "+trips.size());
    }
}
