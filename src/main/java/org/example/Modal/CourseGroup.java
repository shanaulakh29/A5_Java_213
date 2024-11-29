package org.example.Modal;

import org.example.Modal.Section.CourseLab;
import org.example.Modal.Section.CourseLecture;
import org.example.Modal.Section.SectionDetail;

import java.util.ArrayList;
import java.util.List;

public class CourseGroup {
public static List<CourseInfo> courseGroups=new ArrayList<>();

 public void groupCourses(CourseDetail newCourseDetail){

     String semester=newCourseDetail.semester.getSemesterCode();
     String subjectName=newCourseDetail.subjectDetails.getSubjectName();
     String subjectCode=newCourseDetail.subjectDetails.getSubjectCode();
     String instructorName=newCourseDetail.instructor.getInstructorName();
     String campus=newCourseDetail.campusLocation.getCampusLocation();
     String componentCode=newCourseDetail.offerringDetails.getComponentCode();
     int totalEnrolledStudents=newCourseDetail.offerringDetails.getEnrollmentTotal();
     int enrollmentCapacity=newCourseDetail.offerringDetails.getEnrollmentCapacity();

CourseInfo newCourse=new CourseInfo(semester,subjectName,subjectCode,instructorName, campus,totalEnrolledStudents,enrollmentCapacity,componentCode);
 SectionDetail section;
     if(componentCode.equals("LEC")){
         section=new CourseLecture(totalEnrolledStudents,enrollmentCapacity);
     }else if(componentCode.equals("LAB")){
         section=new CourseLab(totalEnrolledStudents,enrollmentCapacity);
     }else{
         throw new IllegalArgumentException("Unknow section type");
     }

        for(CourseInfo courseInfo:courseGroups){
            if(courseInfo.isSameCourse(newCourse)){
                courseInfo.addNewSection(section);
                return;
            }
        }
     newCourse.addNewSection(section);
     courseGroups.add(newCourse);

 }

}
