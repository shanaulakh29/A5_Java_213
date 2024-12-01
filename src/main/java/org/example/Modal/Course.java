package org.example.Modal;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private final Subject subject;
    private List<CourseOffering> offerings = new ArrayList<>();

    public Course(Subject subject) {
        this.subject = subject;
    }

    public CourseOffering getOffering(Semester semester, String location) {
        for (CourseOffering offering : offerings) {
            if (offering.getSemester().equals(semester) && offering.getLocation().equals(location)) {
                return offering;
            }
        }
        return null;
    }


}
