package org.example.Modal;

import java.util.ArrayList;
import java.util.List;

public class Facade {
    private ExtractDataFromFile extractDataFromFile = new ExtractDataFromFile();

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

        boolean isSubjectFound = false;)
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

}
