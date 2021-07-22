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
public interface PoiDAO {

    @Query("SELECT * FROM [poi_table] WHERE poiRouteId=:route_id AND poiId=:poi_id")
    Poi loadSingle(long route_id, long poi_id);

    @Query("SELECT * FROM [poi_table] WHERE poiRouteId=:route_id")
    List<Poi> getAll(long route_id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Poi poi);

    @Insert
    void insertAll(Poi...poi);

    @Delete
    void delete(Poi poi);

    @Query("DELETE FROM poi_table")
    void deleteAll();

    @Update
    public abstract int update(Poi poi);

}
