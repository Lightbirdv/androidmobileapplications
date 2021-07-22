package com.example.wanderrouteroom;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Route.class, Poi.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class WanderRouteDatabase extends RoomDatabase {

    public abstract RouteDAO routeDao();
    public abstract PoiDAO poiDao();

    private static volatile WanderRouteDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static WanderRouteDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WanderRouteDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WanderRouteDatabase.class, "wander_route_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                RouteDAO rDao = INSTANCE.routeDao();
                PoiDAO pDao = INSTANCE.poiDao();
                rDao.deleteAll();

                rDao.insert(new Route("first route for test purposes","beginning", "ending" , "so much gpxdata" , "1.2"));
                rDao.insert(new Route("second route for test purposes", "beginning", "ending", "again so much gpxdata" , "1.6"));

                pDao.insert(new Poi("Cool place 1", (long) 1,"13", "really cool place for testing part 1", "fakeimg.png"));
                pDao.insert(new Poi("Cool place 2 make this different", (long) 1,"23", "really cool", "fakeimg2.png"));
                pDao.insert(new Poi("Cool place for second entry", (long) 2,"23", "really cool", "fakeimg3.png"));
            });
        }
    };
}
