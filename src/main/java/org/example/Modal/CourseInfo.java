//package org.example.Modal;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CourseInfo{
//
//    private String semester;
//   private String subjectName;
//   private String subjectCode;
//    private String instructorName;
//    private String campusLocation;
//    private int totalEnrolledStudents;
//    private int enrollmentCapacity;
//    private String componentCode;
//    private List<SectionDetail> sections = new ArrayList<>();
//    CourseInfo(String semester,String subjectName,String subjectCode, String instructorName,  String campusLocation, int totalEnrolledStudents, int enrollmentCapacity, String componentCode) {
//        this.subjectName = subjectName;
//        this.subjectCode = subjectCode;
//        this.instructorName = instructorName;
//        this.semester = semester;
//        this.campusLocation = campusLocation;
//        this.totalEnrolledStudents = totalEnrolledStudents;
//        this.enrollmentCapacity = enrollmentCapacity;
//        this.componentCode = componentCode;
//    }
//
//    public boolean isSameCourse(CourseInfo course) {
//        if(this.subjectName.equals(course.subjectName)&& this.subjectCode.equals(course.subjectCode)&&
//        this.campusLocation.equals(course.campusLocation)&&this.instructorName.equals(course.instructorName)&&
//        this.semester.equals(course.semester)) {
//            return true;
//        }
//        return false;
//    }
//    public void addNewSection(SectionDetail newSection) {
//        for(SectionDetail section : sections) {
//            if(section.getType().equals(newSection.getType())) {
//                section.addEnrollment(newSection.getTotalStudentsEnrolled(),newSection.getEnrollmentCapacity());
//                return;
//            }
//        }
//        sections.add(newSection);
//
//    }
//
//    public String getSemester() {
//        return semester;
//    }
//    public String getSubjectName() {
//        return subjectName;
//    }
//    public String getSubjectCode() {
//        return subjectCode;
//    }
//    public String getInstructorName() {
//        return instructorName;
//    }
//    public String getCampusLocation() {
//        return campusLocation;
//    }
//    public int getTotalEnrolledStudents() {
//        return totalEnrolledStudents;
//    }
//    public int getEnrollmentCapacity() {
//        return enrollmentCapacity;
//    }
//    public String getComponentCode() {
//        return componentCode;
//    }
//
//    public static void printCourseListingForDumpToConsole(){
//        for(CourseInfo course:CourseGroup.courseGroups){
//            System.out.println(course.getSubjectName()+course.getSubjectCode());
//            System.out.printf("%10s in %s by %s\n",course.getSemester(),course.getCampusLocation(),course.getInstructorName());
//            for(SectionDetail section:course.sections){
//                System.out.printf("%10s Type=%s, Enrollment=%s/%s\n","",section.getType(),section.getTotalStudentsEnrolled(),section.getEnrollmentCapacity());
//            }
//
//        }
//
//    }
//
//
//}
