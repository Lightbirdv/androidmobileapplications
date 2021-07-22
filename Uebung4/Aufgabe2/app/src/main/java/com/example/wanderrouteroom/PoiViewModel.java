package com.example.wanderrouteroom;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PoiViewModel extends AndroidViewModel {

    private WanderRouteRepository repository;


    public PoiViewModel (Application application) {
        super(application);
        repository = new WanderRouteRepository(application);
    }



    List<Poi> getAll(long route_id) {
        return repository.getAllPois(route_id);
    }

    public void insert(Poi poi) {
        repository.insert(poi);
    }
}
