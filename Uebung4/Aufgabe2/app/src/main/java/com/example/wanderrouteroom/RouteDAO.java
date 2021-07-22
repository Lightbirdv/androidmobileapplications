package com.example.wanderrouteroom;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RouteDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Route route);

    @Insert
    void insertAll(Route... routes);

    @Update
    void update(Route route);

    @Delete
    void delete(Route route);

    @Query("DELETE FROM route_table")
    void deleteAll();

    @Query("SELECT * FROM route_table WHERE routeId=:id")
    LiveData<Route> getSingle(long id);

    @Query("SELECT * FROM route_table")
    LiveData<List<Route>> getAll();
}
