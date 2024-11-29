package org.example.Modal;

import org.example.Modal.Section.*;

import java.util.ArrayList;
import java.util.List;

public class CourseGroup {
    public static List<CourseInfo> courseGroups = new ArrayList<>();

    public void groupCourses(CourseDetail newCourseDetail) {

        String semester = newCourseDetail.semester.getSemesterCode();
        String subjectName = newCourseDetail.subjectDetails.getSubjectName();
        String subjectCode = newCourseDetail.subjectDetails.getSubjectCode();
        String instructorName = newCourseDetail.instructor.getInstructorName();
        String campus = newCourseDetail.campusLocation.getCampusLocation();
        String componentCode = newCourseDetail.offerringDetails.getComponentCode();
        int totalEnrolledStudents = newCourseDetail.offerringDetails.getEnrollmentTotal();
        int enrollmentCapacity = newCourseDetail.offerringDetails.getEnrollmentCapacity();

        CourseInfo newCourse = new CourseInfo(semester, subjectName, subjectCode, instructorName, campus, totalEnrolledStudents, enrollmentCapacity, componentCode);
        SectionDetail section = switch (componentCode) {
            case "LEC" -> new CourseLecture(totalEnrolledStudents, enrollmentCapacity);
            case "LAB" -> new CourseLab(totalEnrolledStudents, enrollmentCapacity);
            case "TUT" -> new CourseTutorial(totalEnrolledStudents, enrollmentCapacity);
            case "SEM" -> new CourseSeminar(totalEnrolledStudents, enrollmentCapacity);
            case "PRA" -> new CoursePractical(totalEnrolledStudents, enrollmentCapacity);
            case "OPL" -> new CourseOptional(totalEnrolledStudents, enrollmentCapacity);
            case "SEC" -> new CourseSEC(totalEnrolledStudents, enrollmentCapacity);
            case "INS" -> new CourseINS(totalEnrolledStudents, enrollmentCapacity);
            default -> throw new IllegalArgumentException("Unknown section type");
        };

        for (CourseInfo courseInfo : courseGroups) {
            if (courseInfo.isSameCourse(newCourse)) {
                courseInfo.addNewSection(section);
                return;
            }
        }
        newCourse.addNewSection(section);
        courseGroups.add(newCourse);

    }

}
