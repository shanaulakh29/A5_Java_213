package org.example.DTO;

public class ApiCourseDTO implements Comparable<ApiCourseDTO> {
    public long courseId;
    public String catalogNumber;
    public ApiCourseDTO(long courseId, String catalogNumber) {
        this.courseId = courseId;
        this.catalogNumber = catalogNumber;
    }

    @Override
    public int compareTo(ApiCourseDTO other) {
        return catalogNumber.compareTo(other.catalogNumber);
    }
}
