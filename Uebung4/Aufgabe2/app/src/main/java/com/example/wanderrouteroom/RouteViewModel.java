package com.example.wanderrouteroom;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RouteViewModel extends AndroidViewModel {

    private WanderRouteRepository repository;
    private final LiveData<List<Route>> AllRoutes;
    public LiveData<Route> SingleRoute;

    public RouteViewModel (Application application) {
        super(application);
        repository = new WanderRouteRepository(application);
        AllRoutes = repository.getAllRoutes();
    }

    LiveData<List<Route>> getAllRoutes() {
        return AllRoutes;
    }

    LiveData<Route> getSingle(long id) {
        SingleRoute = repository.getSingle(id);
        return repository.getSingle(id);
    }

    void update(Route route){
       repository.update(route);
    }

    void delete(Route route) {
        repository.delete(route);
    }

    void deleteAll() {
       repository.deleteAll();
    }

    public void insertAll(Route...routes) {
        repository.insertAll(routes);
    }

    public void insert(Route route) {
        repository.insert(route);
    }
}
