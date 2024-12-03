package org.example.Controllers;

import org.example.DTO.*;
//import org.example.Modal.CourseDetail;
//import org.example.Modal.CourseGroup;
//import org.example.Modal.CourseInfo;
import org.example.Modal.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class Controller {

    Facade facade = new Facade();
    List<List<Course>> AllGroupedCoursesBelongingToSameSubject = facade.getListOfGroupedCoursesBasedOnSubject();
    List<ApiDepartmentDTO> departmentsDTO = new ArrayList<>();
    List<ApiCourseDTO> coursesDTO = new ArrayList<>();
    List<ApiCourseOfferingDTO> courseOfferingsDTO = new ArrayList<>();
    List<ApiOfferingSectionDTO> offeringSectionsDTO = new ArrayList<>();

    @GetMapping("/api/about")
    public ResponseEntity<ApiAboutDTO> getHelloMessage() {
        ApiAboutDTO aboutDTO = new ApiAboutDTO("Gurshan Aulakh and Prottoy Zaman Awesome App", "Gurshan Aulakh and Prottoy Zaman");
        return new ResponseEntity<>(aboutDTO, HttpStatus.OK);
    }

    @GetMapping("/api/dump-model")
    public void dumpModel() {
        facade.extractDataFromCSVFile();
        facade.printAllGroupedCoursesBelongingToSameSubject();
    }

    public boolean containsDepartment(List<ApiDepartmentDTO> departments, String departmentName) {
        for (ApiDepartmentDTO department : departments) {
            if (department.getName().equals(departmentName)) {
                return true;
            }
        }
        return false;
    }

    @GetMapping("/api/departments")
    public ResponseEntity<List<ApiDepartmentDTO>> getDepartments() {
        System.out.println("Entered getDepartments");
        int deptIdCounter = 1;

        for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
            String departmentName = courses.get(0).getSubjectName().toUpperCase();
            if (departmentsDTO.isEmpty()) {
                ApiDepartmentDTO apiDepartmentDTO = new ApiDepartmentDTO(deptIdCounter, departmentName);
                departmentsDTO.add(apiDepartmentDTO);
                deptIdCounter++;
            } else {
                if (!containsDepartment(departmentsDTO, departmentName)) {
                    ApiDepartmentDTO apiDepartmentDTO = new ApiDepartmentDTO(deptIdCounter, departmentName);
                    departmentsDTO.add(apiDepartmentDTO);
                    deptIdCounter++;
                }
            }

        }
        Collections.sort(departmentsDTO);
        return new ResponseEntity<>(departmentsDTO, HttpStatus.OK);
    }

    public boolean containsCourse(List<ApiCourseDTO> coursesDTO, String courseCatalogNumber) {
        for (ApiCourseDTO course : coursesDTO) {
            if (course.catalogNumber.equals(courseCatalogNumber)) {
                return true;
            }
        }
        return false;
    }

    @GetMapping("/api/departments/{id}/courses")
    public ResponseEntity<List<ApiCourseDTO>> getCoursesFromSpecificDepartment(@PathVariable long id) {
        System.out.println("Department ID is :" + id);
        long courseIdCounter = 1;
        for (ApiDepartmentDTO departmentDTO : departmentsDTO) {
            if (departmentDTO.getDeptId() == id) {
                String departmentDTOName = departmentDTO.getName();
                for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
                    if (courses.get(0).getSubjectName().toUpperCase().equals(departmentDTOName)) {
                        for (Course course : courses) {
                            if (!containsCourse(coursesDTO, course.getSubjectCatalogNumber())) {
                                ApiCourseDTO courseDTO = new ApiCourseDTO(courseIdCounter, course.getSubjectCatalogNumber());
                                coursesDTO.add(courseDTO);
                                courseIdCounter++;
                            }

                        }
                    }
                }
            }
        }
        Collections.sort(coursesDTO);
        return new ResponseEntity<>(coursesDTO, HttpStatus.OK);
    }

    @GetMapping("/api/departments/{departmentId}/courses/{courseId}/offerings")
    ResponseEntity<List<ApiCourseOfferingDTO>> getCourseOfferingsOfParticularCourse(@PathVariable("departmentId") long departmentId,
                                                                                    @PathVariable("courseId") long courseId) {
        System.out.println("Department ID is :" + departmentId);
        System.out.println("Course ID is :" + courseId);
        for (ApiDepartmentDTO departmentDTO : departmentsDTO) {
            if (departmentDTO.getDeptId() == departmentId) {
                String departmentDTOName = departmentDTO.getName();
                for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
                    if (courses.get(0).getSubjectName().toUpperCase().equals(departmentDTOName)) {//get Specific list that contains only courses belonging to that department
                        for (ApiCourseDTO courseDTO : coursesDTO) {
                            if (courseDTO.courseId == courseId) {
                                String subjectCatalogNumber = courseDTO.catalogNumber;
                                long offeringIdCounter = 1;
                                for (Course course : courses) {
                                    if (course.getSubjectCatalogNumber().equals(subjectCatalogNumber)) {//get courses offered in particular catalog number
                                        ApiCourseOfferingDTO apiCourseOfferingDTO = new ApiCourseOfferingDTO(offeringIdCounter,
                                                course.getLocation(), course.getInstructorsNamesForPrinting(), course.getSemester().getTerm(),
                                                Long.parseLong(course.getSemester().getSemesterCode()), course.getSemester().getYear());
                                        courseOfferingsDTO.add(apiCourseOfferingDTO);
                                        offeringIdCounter++;
                                    }
                                }

                            }
                        }
                    }
                }

            }
        }
        return new ResponseEntity<>(courseOfferingsDTO, HttpStatus.OK);
    }

    @GetMapping("/api/departments/{departmentId}/courses/{courseId}/offerings/{offeringId}")
    ResponseEntity<List<ApiOfferingSectionDTO>> getListOfSectionsOfParticularCourseOffering(
            @PathVariable("departmentId") long departmentId,
            @PathVariable("courseId") long courseId,
            @PathVariable("offeringId") long offeringId

    ) {
        for (ApiDepartmentDTO departmentDTO : departmentsDTO) {
            if (departmentDTO.getDeptId() == departmentId) {
                String departmentDTOName = departmentDTO.getName();
                for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
                    if (courses.get(0).getSubjectName().toUpperCase().equals(departmentDTOName)) {//get Specific list that contains only courses belonging to that department
                        for (ApiCourseDTO courseDTO : coursesDTO) {
                            if (courseDTO.courseId == courseId) {
                                String subjectCatalogNumber = courseDTO.catalogNumber;
                                long offeringIdCounter = 1;
                                for (Course course : courses) {
                                    if (course.getSubjectCatalogNumber().equals(subjectCatalogNumber)) {//get courses offered in particular catalog number
                                        for (ApiCourseOfferingDTO apiCourseOfferingDTO : courseOfferingsDTO) {
                                            if (apiCourseOfferingDTO.courseOfferingId == offeringId) {
                                                String location = apiCourseOfferingDTO.location;
                                                String term = apiCourseOfferingDTO.term;
                                                long semesterCode = apiCourseOfferingDTO.semesterCode;
                                                long year = apiCourseOfferingDTO.year;
                                                String instructors = apiCourseOfferingDTO.instructors;
                                                if (location.equals(course.getLocation()) && semesterCode == Long.parseLong(course.getSemester().getSemesterCode())
                                                        && year == course.getSemester().getYear() && instructors.equals(course.getInstructorsNamesForPrinting())) {
                                                    for (Section section : course.getSectionsList()) {
                                                        ApiOfferingSectionDTO apiOfferingSectionDTO = new ApiOfferingSectionDTO(section.getComponentCode(), section.getEnrolmentCapacity(), section.getEnrolmentTotal());
                                                        offeringSectionsDTO.add(apiOfferingSectionDTO);
                                                    }


                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return new ResponseEntity<>(offeringSectionsDTO, HttpStatus.OK);
    }

    @PostMapping("api/addoffering")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addOffering(@RequestBody ApiOfferingDataDTO dto) {
        String semesterCode = dto.semester;
        String subjectName = dto.subjectName;
        String subjectCatalogNumber = dto.catalogNumber;
        String campusLocation = dto.location;
        int enrolmentCap = dto.enrollmentCap;
        String componentCode = dto.component;
        int enrolmentTotal = dto.enrollmentTotal;
        List<String> instructors = Arrays.asList(dto.instructor.split(", "));

        Semester semester = new Semester(semesterCode);
        Section section = new Section(componentCode, enrolmentCap, enrolmentTotal);
        Course course = new Course(semester, subjectName, subjectCatalogNumber, campusLocation, instructors, section);

        ExtractDataFromFile dataExtractor = new ExtractDataFromFile();

        //return new ResponseEntity<>(, HttpStatus.CREATED);
    }


    @GetMapping("api/watchers")
    public List<ApiWatcherDTO> getWatchers() {
        List<ApiWatcherDTO> watchers = new ArrayList<>();

        return watchers;
    }
}
