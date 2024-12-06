package org.example.Modal;

import org.example.DTO.*;

import java.util.ArrayList;
import java.util.List;

public class Facade {
    private ExtractDataFromFile extractDataFromFile = new ExtractDataFromFile();
    private List<CourseSectionChangeObserver> observers = new ArrayList<>();
    private List<ApiCourseDTO> courseDTOs = new ArrayList<>();
    private List<ApiDepartmentDTO> departmentDTOs = new ArrayList<>();
    private List<ApiCourseOfferingDTO> courseOfferingDTOs = new ArrayList<>();
    private long courseId;
    private long departmentId;
    private long courseOfferingId;


    //Observer Code


    public void addObserver(CourseSectionChangeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(long id) {
        CourseSectionChangeObserver observerToRemove = null;
        for (CourseSectionChangeObserver observer : observers) {
            if (observer.getId() == id) {
                observerToRemove = observer;
            }
        }
        if (observerToRemove != null) {
            observers.remove(observerToRemove);
        }
    }


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

        boolean isSubjectFound = false;
        for (List<Course> courses : getListOfGroupedCoursesBasedOnSubject()) {
            if (courses.get(0).isSameSubject(course)) {
                courses.add(course);
                isSubjectFound = true;
                break;
            }
        }

        if (!isSubjectFound) {
            List<Course> courses = new ArrayList<>();
            courses.add(course);
            getListOfGroupedCoursesBasedOnSubject().add(courses);

        }
    }

    private void notifyAllOberserversOfTheNewSectionBeingAdded(Course course) {
        for (CourseSectionChangeObserver observer : observers) {
            observer.newSectionBeingAdded(course);
        }
    }

    public void addNewOffering(ApiOfferingDataDTO dto) {
        List<String> instructors = dto.instructor != null
                ? List.of(dto.instructor.split(","))
                : new ArrayList<>();

        Semester semester = new Semester(dto.semester);
        Section section = new Section(dto.component, dto.enrollmentTotal, dto.enrollmentCap);
        Course course = new Course(semester, dto.subjectName, dto.catalogNumber, dto.location, instructors, section);

        addCourseToListOfGroupedCoursesBelongingToSameSubject(course);
        notifyAllOberserversOfTheNewSectionBeingAdded(course);
    }

}

//public long getStartSemesterForDepartmentDataFromCSV(){
//    return extractDataFromFile.getStartSemesterForDepartmentDataFromCSV();
//}
//public long getEndSemesterForDepartmentDataFromCSV(){
//    return extractDataFromFile.getEndSemesterForDepartmentDataFromCSV();
//}
//public void addIntoStartSemester(){
//    extractDataFromFile.addIntoStartSemester();
//}
//public long getOriginalStartSemester(){
//    return extractDataFromFile.getOriginalStartSemester();
//}
//public void setStartSemesterToOriginalStartSemester(){
//    extractDataFromFile.setStartSemesterToOriginalStartSemester();
//}