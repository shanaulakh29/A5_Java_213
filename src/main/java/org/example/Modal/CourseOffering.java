package org.example.Modal;

import java.util.*;

public class CourseOffering {
    private Semester semester;
    private String location;
    private List<String> instructors = new ArrayList<>();
    private List<Section> sections = new ArrayList<>();

    public CourseOffering(Semester semester, String location) {
        this.semester = semester;
        this.location = location;
    }

    public void addInstructor(String instructor) {
        instructor = instructor.trim();
        if (!instructors.contains(instructor)) {
            this.instructors.add(instructor);
        }
    }

    public void addSection(String componentCode, int enrolmentCapacity, int enrolmentTotal) {
        for (Section section : sections) {
            if (section.getComponentCode().equals(componentCode)) {
                section.addEnrolment(enrolmentCapacity, enrolmentTotal);
                return;
            }
        }
        sections.add(new Section(componentCode, enrolmentCapacity, enrolmentTotal));
    }

    public Semester getSemester() {
        return semester;
    }

    public String getLocation() {
        return location;
    }

}
