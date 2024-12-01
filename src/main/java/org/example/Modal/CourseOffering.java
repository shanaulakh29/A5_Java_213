package org.example.Modal;

import java.util.*;

public class CourseOffering {
    private final Semester semester;
    private final String location;
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

    public void addNewSection(String componentCode, int enrolmentCapacity, int enrolmentTotal) {
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

    public String getInstructor() {
        String instructorToReturn = instructors.get(0);
        for (String instructor : instructors.subList(1, instructors.size())) {
            instructorToReturn += ", " + instructor ;
        }
        return instructorToReturn;
    }

    public List<Section> getSections() {
        return sections;
    }



}
