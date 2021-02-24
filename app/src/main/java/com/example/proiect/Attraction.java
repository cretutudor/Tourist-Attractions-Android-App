package com.example.proiect;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Attractions")
public class Attraction implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int id;

    private String firebaseId;
    private String name;
    private String county;
    private String city;
    private float latitude;
    private float longitude;
    private String type;
    private String description;

    @Ignore
    public Attraction(String name, String county, String city, float latitude, float longitude, String type, String description) {
        this.name = name;
        this.county = county;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.description = description;
    }

    public Attraction(int id, String name, String county, String city, float latitude, float longitude, String type, String description) {
        this.id = id;
        this.name = name;
        this.county = county;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.description = description;
    }

    @Ignore
    public Attraction(String id, String name, String county, String city, float latitude, float longitude, String type, String description) {
        this.firebaseId = id;
        this.name = name;
        this.county = county;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.description = description;
    }

    @Ignore
    public Attraction() {}

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
