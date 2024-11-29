package org.example.Modal.Section;

public interface SectionDetail {
    String getType();
    int getTotalStudentsEnrolled();
    int getEnrollmentCapacity();
    void addEnrollment(int enrolledStudents, int enrollmentCapacity);
}
