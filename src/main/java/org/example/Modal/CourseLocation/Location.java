package org.example.Modal.CourseLocation;

public class Location implements CampusLocation {
    private String location;
    public Location(String location) {
        this.location = location;
    }
    @Override
    public String getCampusLocation() {
        return location;
    }
}
