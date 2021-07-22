package com.example.wanderrouteroom;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.io.File;
import java.util.List;

@Entity(tableName = "route_table")
public class Route {

    @PrimaryKey(autoGenerate = true)
    private Integer routeId;
    @NonNull
    private String description;
    @NonNull
    private String beginning;
    @NonNull
    private String ending;
    @NonNull
    private String gpxdata;
    @NonNull
    private String duration;
    //@Relation(
    //        parentColumn = "routeId",
    //        entityColumn = "poiRouteId"
    //)
    private String pois;

    public Route(@NonNull String description, @NonNull String beginning, @NonNull String ending, @NonNull String gpxdata, @NonNull String duration) {
        this.description = description;
        this.beginning = beginning;
        this.ending = ending;
        this.gpxdata = gpxdata;
        this.duration = duration;

    }
    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }
    public Integer getRouteId() {
        return this.routeId;
    }
    public String getDescription() {
        return this.description;
    }
    public String getBeginning() {
        return this.beginning;
    }
    public String getEnding() {
        return this.ending;
    }
    public String getGpxdata() {
        return this.gpxdata;
    }
    public String getDuration() {
        return this.duration;
    }
    public /*List<Poi>*/ String getPois() {
        return this.pois;
    }

    public void setPois(String pois) {
        this.pois = pois;
    }
}
