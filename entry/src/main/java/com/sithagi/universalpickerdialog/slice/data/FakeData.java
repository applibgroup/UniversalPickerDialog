package com.sithagi.universalpickerdialog.slice.data;

import java.util.ArrayList;

/**
 * FakeData data model.
 */
public final class FakeData {
    private FakeData() {
        throw new AssertionError();
    }

    /**
     * gets all city list.
     *
     * @return city list
     */
    public static ArrayList<City> getCitiesList() {
        ArrayList<City> cities = new ArrayList<>();

        cities.add(new City("London", "Great Britain"));
        cities.add(new City("New York", "United States of America"));
        cities.add(new City("Paris", "France"));
        cities.add(new City("Rome", "Italy"));
        cities.add(new City("Berlin", "Germany"));
        cities.add(new City("Warsaw", "Poland"));

        return cities;
    }

    /**
     * get developer list by levels.
     *
     * @return developer list by levels
     */
    public static Developer.Level[] getDeveloperLevels() {
        return new Developer.Level[] {
            new Developer.Level(0, "Trainee"),
            new Developer.Level(1, "Junior"),
            new Developer.Level(2, "Middle"),
            new Developer.Level(3, "Senior"),
            new Developer.Level(4, "Team leader")
        };
    }

    /**
     * get developer list by Specialization.
     *
     * @return developer list by Specialization
     */
    public static Developer.Specialization[] getDeveloperSpecializations() {
        return new Developer.Specialization[] {
            new Developer.Specialization(0, "Android"),
            new Developer.Specialization(1, "iOS"),
            new Developer.Specialization(2, "WP"),
            new Developer.Specialization(3, "Front-end"),
            new Developer.Specialization(4, "Back-end")
        };
    }
}
