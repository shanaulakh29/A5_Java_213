package org.example.DTO;
import java.util.List;

public class ApiWatcherDTO {
    public long id;
    public ApiDepartmentDTO department;
    public ApiCourseDTO course;
    public List<String> events;
   public  ApiWatcherDTO(long id, ApiDepartmentDTO department, ApiCourseDTO course, List<String> events) {
       this.id = id;
       this.department = department;
       this.course = course;
       this.events = events;
   }

}
