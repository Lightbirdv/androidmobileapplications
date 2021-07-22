package com.example.wanderrouteroom;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WanderRouteRepository {

    private RouteDAO RouteDao;
    private LiveData<List<Route>> AllRoutes;
    public LiveData<Route> SingleRoute;

    private PoiDAO PoiDao;
    private LiveData<List<Poi>> AllPois;

    WanderRouteRepository(Application application) {
        WanderRouteDatabase db = WanderRouteDatabase.getDatabase(application);
        RouteDao = db.routeDao();
        PoiDao = db.poiDao();
        AllRoutes = RouteDao.getAll();
    }

    /*Routes*/

    LiveData<List<Route>> getAllRoutes() {
        return AllRoutes;
    }

    LiveData<Route> getSingle(long id) {
        SingleRoute = RouteDao.getSingle(id);
       return SingleRoute;
    }

    void insert(Route route) {
        WanderRouteDatabase.databaseWriteExecutor.execute(() -> {
            RouteDao.insert(route);
        });
    }

    void insertAll(Route...routes){
        WanderRouteDatabase.databaseWriteExecutor.execute(() -> {
            RouteDao.insertAll(routes);
        });
    }

    void update(Route route){
        WanderRouteDatabase.databaseWriteExecutor.execute(() -> {
            RouteDao.update(route);
        });
    }

    void delete(Route route) {
        WanderRouteDatabase.databaseWriteExecutor.execute(() -> {
            RouteDao.delete(route);
        });
    }

    void deleteAll() {
        WanderRouteDatabase.databaseWriteExecutor.execute(() -> {
            RouteDao.deleteAll();
        });
    }

    /* POIS */

    List<Poi> getAllPois(long route_id) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<Poi>> future = executor.submit(new Callable<List<Poi>>() {
            @Override
            public List<Poi> call() {
                List<Poi> list;
                list = PoiDao.getAll(route_id);
                return list;
            }
        });
        List<Poi>list = null;
        try {
            list = future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    void insert(Poi poi) {
        WanderRouteDatabase.databaseWriteExecutor.execute(() -> {
            PoiDao.insert(poi);
        });
    }
}
