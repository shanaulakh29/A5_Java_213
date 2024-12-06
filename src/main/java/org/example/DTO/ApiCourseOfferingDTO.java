package org.example.DTO;

public class ApiCourseOfferingDTO implements Comparable<ApiCourseOfferingDTO> {
    public long courseOfferingId;
    public String location;
    public String instructors;
    public String term;
    public long semesterCode;
    public int year;
    public ApiCourseOfferingDTO(long courseOfferingId, String location, String instructors, String term, long semesterCode, int year) {
        this.courseOfferingId = courseOfferingId;
        this.location = location;
        this.instructors = instructors;
        this.term = term;
        this.semesterCode = semesterCode;
        this.year = year;
    }

    @Override
    public int compareTo(ApiCourseOfferingDTO o) {
        return  Long.compare(this.courseOfferingId, o.courseOfferingId);
    }
}
