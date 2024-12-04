package org.example.Modal;

import org.example.DTO.ApiDepartmentDTO;
import org.example.DTO.ApiOfferingDataDTO;
import org.example.DTO.ApiWatcherDTO;

import java.util.ArrayList;
import java.util.List;

public class Facade {
    private ExtractDataFromFile extractDataFromFile = new ExtractDataFromFile();
    private List<Watcher> watchers = new ArrayList<>();

    public void extractDataFromCSVFile() {
        extractDataFromFile.extractDataFromFile();
    }

    public void printAllGroupedCoursesBelongingToSameSubject() {
        extractDataFromFile.printAllGroupedCoursesBelongingToSameSubject();
    }

    public List<List<Course>> getListOfGroupedCoursesBasedOnSubject() {
        return extractDataFromFile.getListOfGroupedCoursesBasedOnSubject();
    }

    public void addCourseToListOfGroupedCoursesBelongingToSameSubject(Course course) {
        List<List<Course>> listOfGroupedCoursesBasedOnSubject = getListOfGroupedCoursesBasedOnSubject();

        boolean isSubjectFound = false;
        for (List<Course> courses : listOfGroupedCoursesBasedOnSubject) {
            if (courses.get(0).isSameSubject(course)) {
                courses.add(course);
                isSubjectFound = true;
                break;
            }
        }

        if (!isSubjectFound) {
            List<Course> newListOfGroupedCoursesBasedOnSubject = new ArrayList<>();
            newListOfGroupedCoursesBasedOnSubject.add(course);
            listOfGroupedCoursesBasedOnSubject.add(newListOfGroupedCoursesBasedOnSubject);

        }
    }

    public void addNewOffering(ApiOfferingDataDTO dto) {
        List<String> instructors = dto.instructor != null
                ? List.of(dto.instructor.split(","))
                : new ArrayList<>();

        Semester semester = new Semester(dto.semester);
        Section section = new Section(dto.component, dto.enrollmentCap, dto.enrollmentTotal);
        Course course = new Course(semester, dto.subjectName, dto.catalogNumber, dto.location, instructors, section);

        addCourseToListOfGroupedCoursesBelongingToSameSubject(course);
    }

    public List<Watcher> getWatchers() {
        return watchers;
    }

    public void addWatcher(Watcher watcher) {
        watchers.add(watcher);
    }

    public List<ApiWatcherDTO> getAllApiWatchers() {
    }

}
