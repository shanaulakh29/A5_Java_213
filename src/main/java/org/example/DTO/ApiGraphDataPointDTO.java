package org.example.DTO;

public class ApiGraphDataPointDTO implements Comparable<ApiGraphDataPointDTO> {
    public long semesterCode;
    public long totalCoursesTaken;
    public ApiGraphDataPointDTO(long semesterCode, long totalCoursesTaken) {
        this.semesterCode = semesterCode;
        this.totalCoursesTaken = totalCoursesTaken;
    }
    @Override
    public int compareTo(ApiGraphDataPointDTO o) {
        return Long.compare(this.semesterCode, o.semesterCode);
    }
}
