package org.example.Modal;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private final String catalogNumber;
    private List<CourseOffering> courseOfferings = new ArrayList<>();

    public Course(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public CourseOffering getOffering(Semester semester, String location) {
        for (CourseOffering offering : courseOfferings) {
            if (offering.getSemester().equals(semester) && offering.getLocation().equals(location)) {
                return offering;
            }
        }
        return null;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public List<CourseOffering> getOfferings() {
        return courseOfferings;
    }


}
