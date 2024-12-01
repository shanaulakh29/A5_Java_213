package org.example.Modal;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private final String catalogNumber;
    private Semester semester;
    private List<CourseOffering> courseOfferings = new ArrayList<>();

    public Course(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public CourseOffering findOrCreateCourseOffering(Semester semester, String location) {
        for (CourseOffering offering : courseOfferings) {
            if (offering.getSemester().equals(semester) && offering.getLocation().equals(location)) {
                return offering;
            }
        }
        CourseOffering newOffering = new CourseOffering(semester, location);
        courseOfferings.add(newOffering);
        return newOffering;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public String getSemesterCode() {
        return semester.getSemesterCode();
    }

    public List<CourseOffering> getOfferings() {
        return courseOfferings;
    }

}
