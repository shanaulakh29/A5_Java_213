package org.example.Modal;

import java.util.List;

public class Facade {
    private ExtractDataFromFile extractDataFromFile = new ExtractDataFromFile();
    public void extractDataFromCSVFile(){
        extractDataFromFile.extractDataFromFile();
    }
    public void printAllGroupedCoursesBelongingToSameSubject(){
        extractDataFromFile.printAllGroupedCoursesBelongingToSameSubject();
    }
    public List<List<Course>> getListOfGroupedCoursesBasedOnSubject(){
        return extractDataFromFile.getListOfGroupedCoursesBasedOnSubject();
    }

}
