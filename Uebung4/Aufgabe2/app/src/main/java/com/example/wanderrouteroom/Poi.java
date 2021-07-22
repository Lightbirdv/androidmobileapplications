package com.example.wanderrouteroom;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "poi_table")
public class Poi {

    @PrimaryKey(autoGenerate = true)
    public long poiId;

    @NonNull
    @ColumnInfo(name = "place")
    private String place;

    @NonNull
    private Long poiRouteId;

    @NonNull
    private String coordinates;

    @NonNull
    private String description;

    private String img;

    public Poi(@NonNull String place,@NonNull Long poiRouteId, @NonNull String coordinates, @NonNull String description, String img) {
        this.place = place;
        this.poiRouteId = poiRouteId;
        this.coordinates = coordinates;
        this.description = description;
        this.img = img;
    }

    public long getPoiId() {
        return poiId;
    }

    public Long getPoiRouteId() {
        return this.poiRouteId;
    }

    public String getCoordinates() {
        return this.coordinates;
    }

    public String getDescription() {
        return this.description;
    }

    public String getImg() {
        return this.img;
    }

    public String getPlace() {
        return this.place;
    }

}
