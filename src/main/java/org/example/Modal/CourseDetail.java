package org.example.Modal;

import org.example.Modal.CourseLocation.CampusLocation;
import org.example.Modal.CourseOfferingsDetails.OfferringDetails;
import org.example.Modal.InstructorDetails.Instructor;
import org.example.Modal.Semester.Semester;
import org.example.Modal.Subject.SubjectDetails;

public class CourseDetail {
    Semester semester;
    SubjectDetails subjectDetails;
    Instructor instructor;
    CampusLocation campusLocation;
    OfferringDetails offerringDetails;
    CourseDetail(Semester semester, SubjectDetails subjectDetails, Instructor instructor, CampusLocation campusLocation, OfferringDetails offerringDetails) {
        this.semester = semester;
        this.subjectDetails = subjectDetails;
        this.instructor = instructor;
        this.campusLocation = campusLocation;
        this.offerringDetails = offerringDetails;
    }



}
//    public  boolean isSameCourseLab(CourseDetail courseDetail) {
//        if(isSameCourseListing(courseDetail)){
//            if(this.offerringDetails.getComponentCode().equals(courseDetail.offerringDetails.getComponentCode())){
//                if(this.offerringDetails.getComponentCode().equals("LAB")){
//                    return true;
//                }
//                return false;
//            }
//            return false;
//        }
//        return false;
//    }
//    public boolean isSameCourseLec(CourseDetail courseDetail){
//        if(isSameCourseListing(courseDetail)){
//            if(this.offerringDetails.getComponentCode().equals(courseDetail.offerringDetails.getComponentCode())){
//                if(this.offerringDetails.getComponentCode().equals("LEC")){
//                    return true;
//                }
//                return false;
//            }
//            return false;
//        }
//        return false;
//    }
//    public boolean isSameCourseListing(CourseDetail courseDetail) {
//        if((this.semester.getSemesterName().equals(courseDetail.semester.getSemesterName())&&
//                this.subjectDetails.equals(courseDetail.subjectDetails)
//        && this.instructor.getInstructorName().equals(courseDetail.instructor.getInstructorName())&&
//                this.campusLocation.getCampusLocation().equals(courseDetail.campusLocation.getCampusLocation())
//       )){
//            return true;
//        }return false;
//    }
//    public void printCourseListingForDumpToConsole(){
//        System.out.println(subjectDetails.getSubjectName()+subjectDetails.getSubjectCode());
//        System.out.printf("%10s in %s by %s\n ",semester.getSemesterCode(),campusLocation.getCampusLocation(),instructor.getInstructorName());
//        System.out.printf("%10s Type=%s, Enrollment=%s/%s\n","",offerringDetails.getComponentCode(),offerringDetails.getEnrollmentTotal(),offerringDetails.getEnrollmentCapacity());
//    }
//    @Override
//    public String toString(){
//        return semester.getSemesterName()+subjectDetails.getSubjectName()+subjectDetails.getSubjectCode()+
//                campusLocation.getCampusLocation()+instructor.getInstructorName();
//    }