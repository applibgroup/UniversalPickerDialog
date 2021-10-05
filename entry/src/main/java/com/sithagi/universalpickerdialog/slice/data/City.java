package com.sithagi.universalpickerdialog.slice.data;

/**
 * city data model.
 */
public class City {

    private final String name;
    private final String country;

    public City(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return name;
    }

}
