package org.example.Modal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Course {
    private Semester semester;
    private String subjectName;
    private final String subjectCatalogNumber;
    private String location;
    private List<String> instructors = new ArrayList<>();
    private List<Section> sections = new ArrayList<>();
    private Section section;

    public Course(Semester semester, String subjectName, String subjectCatalogNumber,
                  String location, List<String> instructors, Section section) {
        this.semester = semester;
        this.subjectName = subjectName;
        this.subjectCatalogNumber = subjectCatalogNumber;
        this.location = location;
        this.instructors = instructors;
        this.section = section;
        this.addNewSectionOrAddIntoAlreadyExistingSection(this.section);
    }


    public Semester getSemester() {
        return semester;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getSubjectCatalogNumber() {
        return subjectCatalogNumber;
    }

    public String getLocation() {
        return location;
    }

    public String getInstructorsNamesForPrinting() {
        StringBuilder names = new StringBuilder();
        for (String instructor : instructors) {
            names.append(instructor).append(", ");
        }
        return names.substring(0, names.length() - 2);

    }

    public List<String> getInstructors() {
        return instructors;
    }

    public Section getSection() {
        return section;
    }

    public List<Section> getSectionsList() {
        return sections;
    }

    public void addNewSectionOrAddIntoAlreadyExistingSection(Section section) {
        for (Section currentSection : this.sections) {
            if (currentSection.getComponentCode().equals(section.getComponentCode())) {
                currentSection.addEnrolment(section.getEnrolmentCapacity(), section.getEnrolmentTotal());
                return;
            }
        }
        this.sections.add(section);
    }

    public void addInstructorToGroup(Course course) {

        for (String instructor : course.getInstructors()) {
            if (!(this.getInstructors().contains(instructor))) {
                this.getInstructors().add(instructor);
            }
        }
    }

    public boolean isSameSubject(Course course) {
        return (this.subjectName.equals(course.getSubjectName()) &&
                this.subjectCatalogNumber.equals(course.getSubjectCatalogNumber()));
    }

    public boolean isSameCourse(Course course) {
        if (this.subjectName.equals(course.subjectName) && this.subjectCatalogNumber.equals(course.subjectCatalogNumber)
                && this.getLocation().equals(course.getLocation()) &&
                this.semester.equals(course.semester)) {
            return true;
        }
        return false;
    }

}

