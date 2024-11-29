package org.example.Modal.Section;

public class CourseLecture implements SectionDetail {
    private int totalStudentsEnrolled;
    private int enrollmentCapacity;

    public CourseLecture(int totalStudentsEnrolled, int enrollmentCapacity) {
        this.totalStudentsEnrolled = totalStudentsEnrolled;
        this.enrollmentCapacity = enrollmentCapacity;
    }

    @Override
    public String getType() {
        return "LEC";
    }

    @Override
    public int getTotalStudentsEnrolled() {
        return totalStudentsEnrolled;
    }

    @Override
    public int getEnrollmentCapacity() {
        return enrollmentCapacity;
    }

    @Override
    public void addEnrollment(int enrolledStudents, int enrollmentCapacity) {
  this.totalStudentsEnrolled += enrolledStudents;
  this.enrollmentCapacity += enrollmentCapacity;
    }
}
