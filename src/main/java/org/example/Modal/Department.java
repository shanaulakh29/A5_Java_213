package org.example.Modal;

import java.util.*;

public class Department {
    private final String subject;
    private final List<Course> courses = new ArrayList<>();

    public Department(String subject) {
        this.subject = subject;
    }

    public void addCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
        }
    }

    public String getSubject() {
        return subject;
    }

    public List<Course> getCourses() {
        return courses;
    }


}
