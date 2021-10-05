package com.sithagi.universalpickerdialog.slice.data;

/**
 * Developer data model.
 */
public class Developer {

    private final Level level;
    private final Specialization specialization;
    private final City location;

    public Developer(Level level, Specialization specialization, City location) {
        this.level = level;
        this.specialization = specialization;
        this.location = location;
    }

    public Level getLevel() {
        return level;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public City getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return String.format(
                "%s %s developer in %s (%s)",
                level,
                specialization,
                location.getName(),
                location.getCountry());
    }

    /**
     * level model.
     */
    public static class Level {

        private final long id;
        private final String name;

        public Level(long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public long getId() {
            return id;
        }
    }

    public static class Specialization {

        private final long id;
        private final String name;

        public Specialization(long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public long getId() {
            return id;
        }
    }

}
