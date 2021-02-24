package com.example.proiect;

import java.io.Serializable;
import java.util.List;

public class County implements Serializable {
    private String id;
    private String name;
    private String capitalCity;
    private String region;
    private int population;
    private float area;
    private List<String> cities;

    public County(String id, String name, String capitalCity, String region, int population, float area, List<String> cities) {
        this.id = id;
        this.name = name;
        this.capitalCity = capitalCity;
        this.region = region;
        this.population = population;
        this.area = area;
        this.cities = cities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapitalCity() {
        return capitalCity;
    }

    public void setCapitalCity(String capitalCity) {
        this.capitalCity = capitalCity;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public float getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return name;
    }
}

