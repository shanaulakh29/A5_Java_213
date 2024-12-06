package org.example.Modal;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExtractDataFromFile {
    private static List<Course> courses = new ArrayList<>();//remove it
    private static List<Course> ListOfGroupedCourses = new ArrayList<>();
    private static List<List<Course>> ListOfGroupedCoursesBasedOnSubject = new ArrayList<>();

    public List<List<Course>> getListOfGroupedCoursesBasedOnSubject() {
        return ListOfGroupedCoursesBasedOnSubject;
    }

    public void extractDataFromFile() {
        try {
            FileReader fileReader = new FileReader("./data/course_data_2018.csv");
//            FileReader fileReader = new FileReader("./data/small_data.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                List<String> data = parseCSV(line);
                Semester semester = new Semester(data.get(0));
                String subjectName = data.get(1);
                String subjectCatalogNumber = data.get(2);
                String location = data.get(3);
                String instructorNames = data.get(6);
                List<String> instructors = new ArrayList<>(Arrays.asList(instructorNames.split(",")));
                ;
                int enrollmentTotal = Integer.parseInt(data.get(4));
                int enrollmentCapacity = Integer.parseInt(data.get(5));
                String componentCode = data.get(7);
                Section section = new Section(componentCode, enrollmentCapacity, enrollmentTotal);

                Course course = new Course(semester, subjectName, subjectCatalogNumber, location, instructors, section);

                courses.add(course);//remove it
                groupSimilarCourses(course);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        groupCoursesBasedOnSubject();
    }

    public void groupSimilarCourses(Course course) {
        if (ListOfGroupedCourses.isEmpty()) {
            ListOfGroupedCourses.add(course);
        } else {
            boolean isAdded = false;
            for (Course groupedCourse : ListOfGroupedCourses) {
                if (groupedCourse.isSameCourse(course)) {
                    isAdded = true;
                    groupedCourse.addInstructorToGroup(course);
                    groupedCourse.addNewSectionOrAddIntoAlreadyExistingSection(course.getSection());
                    Collections.sort(groupedCourse.getSectionsList());
                }
            }
            if (!isAdded) {
                ListOfGroupedCourses.add(course);
            }

        }
    }

    public void groupCoursesBasedOnSubject() {
        for (Course course : ListOfGroupedCourses) {
            if (ListOfGroupedCoursesBasedOnSubject.isEmpty()) {
                List<Course> courses = new ArrayList<>();
                courses.add(course);
                ListOfGroupedCoursesBasedOnSubject.add(courses);
            } else {
                boolean isAdded = false;
                for (List<Course> courses : ListOfGroupedCoursesBasedOnSubject) {
                    if (courses.get(0).isSameSubject(course)) {
                        isAdded = true;
                        courses.add(course);
                    }
                }
                if (!isAdded) {
                    List<Course> courses = new ArrayList<>();
                    courses.add(course);
                    ListOfGroupedCoursesBasedOnSubject.add(courses);
                }
            }
        }

    }

    public void printAllGroupedCoursesBelongingToSameSubject() {
        for (List<Course> courses : ListOfGroupedCoursesBasedOnSubject) {
            System.out.println(courses.get(0).getSubjectName() + " " + courses.get(0).getSubjectCatalogNumber());
            for (Course course : courses) {
                printCourse(course);
            }
        }
    }

    public void printCourse(Course course) {
        System.out.printf("%10s in %s by %s\n", course.getSemester().getSemesterCode(), course.getLocation(), course.getInstructorsNamesForPrinting());
        for (Section section : course.getSectionsList()) {
            System.out.printf("%10s Type=%s, Enrollment=%s/%s\n", "", section.getComponentCode(), section.getEnrolmentCapacity(), section.getEnrolmentTotal());
        }
        System.out.println();
    }


    private static List<String> parseCSV(String line) {
        List<String> data = new ArrayList<>();
        boolean quotationFound = false;
        StringBuilder currentValue = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                quotationFound = !quotationFound;
            } else if ((c == ',' && !quotationFound)) {
                data.add(currentValue.toString().trim());
                currentValue = new StringBuilder();
            } else if (i == line.length() - 1) {
                currentValue.append(c);
                data.add(currentValue.toString().trim());
                currentValue = new StringBuilder();

            } else {
                currentValue.append(c);
            }
        }
        return data;
    }

}

//    public void printGroupedCourses() {
//        for (Course course : ListOfGroupedCoursesBasedOnSimilarProfessors) {
//            System.out.println(course.getSubjectName() + course.getSubjectCatalogNumber());
//            System.out.printf("%10s in %s by %s\n", course.getSemester().getTerm(), course.getLocation(), course.getInstructorsNamesForPrinting());
//            for (Section section : course.getSectionsList()) {
//                System.out.printf("%10s Type=%s, Enrollment=%s/%s\n", "", section.getComponentCode(), section.getEnrolmentCapacity(), section.getEnrolmentTotal());
//            }
//        }
//    }
//public static void main(String[] args) {
//        ExtractDataFromFile extractDataFromFile=new ExtractDataFromFile();
//        extractDataFromFile.extractDataFromFile();
//}
//                courses.add(new CourseDetail(semester, subjectDetails, instructor, campusLocation, offerringDetails));
//                courseGroup.groupCourses(courses.get(courses.size() - 1));

//    public void setMinumumAndMaximmSemester(String semester) {
//       long semesterCode=Integer.parseInt(semester);
//        if(sem.getStartSemesterForDepartmentDataFromCSV()==0 &&
//                sem.getEndSemesterForDepartmentDataFromCSV()==0) {
//            sem.setStartSemesterForDepartmentDataFromCSV(semesterCode);
//            sem.setEndSemesterForDepartmentDataFromCSV(semesterCode);
//            originalStartSemester=semesterCode;
//        }
//        else if((sem.getStartSemesterForDepartmentDataFromCSV()> semesterCode)){
//            originalStartSemester=semesterCode;
//            sem.setStartSemesterForDepartmentDataFromCSV(semesterCode);
//        }
//        else if(sem.getEndSemesterForDepartmentDataFromCSV()< semesterCode){
//            sem.setEndSemesterForDepartmentDataFromCSV(semesterCode);
//        }
//    }
//    public long getOriginalStartSemester() {
//       return originalStartSemester;
//    }
//
//    public long getStartSemesterForDepartmentDataFromCSV(){
//       return sem.getStartSemesterForDepartmentDataFromCSV();
//    }
//    public long getEndSemesterForDepartmentDataFromCSV(){
//       return sem.getEndSemesterForDepartmentDataFromCSV();
//    }
//    public void setStartSemesterToOriginalStartSemester(){
//       sem.setStartSemesterForDepartmentDataFromCSV(originalStartSemester);
//    }
//    public void addIntoStartSemester(){
//       long amountToadd=0;
//       if((getStartSemesterForDepartmentDataFromCSV()%10==1)||(getStartSemesterForDepartmentDataFromCSV()%10==4)){
//           amountToadd=3;
//       }
//       else if(getStartSemesterForDepartmentDataFromCSV()%10==7){
//           amountToadd=4;
//       }
//       sem.setStartSemesterForDepartmentDataFromCSV(getStartSemesterForDepartmentDataFromCSV()+amountToadd);
//    }