package org.example.Controllers;

import org.example.DTO.*;
//import org.example.Modal.CourseDetail;
//import org.example.Modal.CourseGroup;
//import org.example.Modal.CourseInfo;
import org.example.Modal.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class Controller {
    private long START_SEMESTER_MODIFIABLE;
    private long WATCHER_ID_COUNTER = 1;
    private long DEPARTMENT_ID_COUNTER = 1;
    private long COURSE_ID_COUNTER = 1;
    Facade facade = new Facade();
    List<List<Course>> AllGroupedCoursesBelongingToSameSubject = facade.getListOfGroupedCoursesBasedOnSubject();
    List<ApiDepartmentDTO> departmentsDTO = new ArrayList<>();
    List<ApiCourseDTO> coursesDTO = new ArrayList<>();
    List<ApiCourseOfferingDTO> courseOfferingsDTO = new ArrayList<>();
    List<ApiOfferingSectionDTO> offeringSectionsDTO = new ArrayList<>();
    List<ApiWatcherDTO> watchersDTO = new ArrayList<>();


    @GetMapping("/api/about")
    public ResponseEntity<ApiAboutDTO> getHelloMessage() {
        ApiAboutDTO aboutDTO = new ApiAboutDTO("Gurshan Aulakh and Prottoy Zaman Awesome App",
                "Gurshan Aulakh and Prottoy Zaman");
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

        coursesDTO.clear();
        for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
            String departmentName = courses.get(0).getSubjectName().toUpperCase();
            if (departmentsDTO.isEmpty()) {
                ApiDepartmentDTO apiDepartmentDTO = new ApiDepartmentDTO(DEPARTMENT_ID_COUNTER, departmentName);
                departmentsDTO.add(apiDepartmentDTO);
                DEPARTMENT_ID_COUNTER++;
            } else {
                if (!containsDepartment(departmentsDTO, departmentName)) {
                    ApiDepartmentDTO apiDepartmentDTO = new ApiDepartmentDTO(DEPARTMENT_ID_COUNTER, departmentName);
                    departmentsDTO.add(apiDepartmentDTO);
                    DEPARTMENT_ID_COUNTER++;
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

    private boolean isSameDepartmentName(Course course, String departmentName) {
        return course.getSubjectName().toUpperCase().equals(departmentName);
    }

    private String getDepartmentName(long deptId) {
        for (ApiDepartmentDTO apiDepartmentDTO : departmentsDTO) {
            if (apiDepartmentDTO.getDeptId() == deptId) {
                return apiDepartmentDTO.getName();
            }
        }
        return null;
    }

    private void fillCourseDTOListForSelectedDepartment(Course course) {
        if (!containsCourse(coursesDTO, course.getSubjectCatalogNumber())) {
            ApiCourseDTO courseDTO = new ApiCourseDTO(COURSE_ID_COUNTER, course.getSubjectCatalogNumber());
            coursesDTO.add(courseDTO);
            COURSE_ID_COUNTER++;
        }
    }

    @GetMapping("/api/departments/{id}/courses")
    public ResponseEntity<List<ApiCourseDTO>> getCoursesFromSpecificDepartment(@PathVariable long id) {
        coursesDTO.clear();

        String departmentName = getDepartmentName(id);

        if (departmentName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
            if (isSameDepartmentName(courses.get(0), departmentName)) {
                for (Course course : courses) {
                    fillCourseDTOListForSelectedDepartment(course);
                }
            }
        }

        Collections.sort(coursesDTO);
        return new ResponseEntity<>(coursesDTO, HttpStatus.OK);
    }


    private void addCourseOfferingsToCourseOfferingsDTOList(ApiCourseDTO courseDTO, Long courseId,
                                                            List<Course> courses) {
        if (courseDTO.courseId == courseId) {
            String subjectCatalogNumber = courseDTO.catalogNumber;
            long offeringIdCounter = 1;
            for (Course course : courses) {
                if (course.getSubjectCatalogNumber().equals(subjectCatalogNumber)) {
                    //get courses offered in particular catalog number
                    ApiCourseOfferingDTO apiCourseOfferingDTO = new ApiCourseOfferingDTO(offeringIdCounter,
                            course.getLocation(), course.getInstructorsNamesForPrinting(),
                            course.getSemester().getTerm(),
                            Long.parseLong(course.getSemester().getSemesterCode()), course.getSemester().getYear());
                    courseOfferingsDTO.add(apiCourseOfferingDTO);
                    offeringIdCounter++;
                }
            }

        }
    }

    @GetMapping("/api/departments/{departmentId}/courses/{courseId}/offerings")
    ResponseEntity<List<ApiCourseOfferingDTO>> getCourseOfferingsOfParticularCourse(
            @PathVariable("departmentId") long departmentId,
            @PathVariable("courseId") long courseId) {

        courseOfferingsDTO.clear();
        String departmentName = getDepartmentName(departmentId);

        for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
            if (isSameDepartmentName(courses.get(0), departmentName)) {
                for (ApiCourseDTO courseDTO : coursesDTO) {
                    addCourseOfferingsToCourseOfferingsDTOList(courseDTO, courseId, courses);
                }
            }
        }
        if (courseOfferingsDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(courseOfferingsDTO, HttpStatus.OK);
    }


    private void addIntoSectionsDTOListIfCourseInfoMatches(ApiCourseOfferingDTO apiCourseOfferingDTO, Course course) {
        String location = apiCourseOfferingDTO.location;
        String term = apiCourseOfferingDTO.term;
        long semesterCode = apiCourseOfferingDTO.semesterCode;
        long year = apiCourseOfferingDTO.year;
        String instructors = apiCourseOfferingDTO.instructors;
        if (location.equals(course.getLocation()) && semesterCode ==
                Long.parseLong(course.getSemester().getSemesterCode())
                && year == course.getSemester().getYear()
                && instructors.equals(course.getInstructorsNamesForPrinting())) {
            for (Section section : course.getSectionsList()) {
                ApiOfferingSectionDTO apiOfferingSectionDTO = new ApiOfferingSectionDTO(section.getComponentCode(),
                        section.getEnrolmentTotal(), section.getEnrolmentCapacity());
                offeringSectionsDTO.add(apiOfferingSectionDTO);
            }


        }
    }

    private void isCourseOfferingIdMatchesProvidedOfferingIdThenAddSectionsIntoSectionDTOList(Course course,
                                                                                              long offeringId) {
        for (ApiCourseOfferingDTO apiCourseOfferingDTO : courseOfferingsDTO) {
            if (apiCourseOfferingDTO.courseOfferingId == offeringId) {
                addIntoSectionsDTOListIfCourseInfoMatches(apiCourseOfferingDTO, course);
            }
        }
    }

    private void addCourseSectionsToOfferingSectionDTO(ApiCourseDTO courseDTO, Long courseId, List<Course> courses,
                                                       long offeringId) {
        if (courseDTO.courseId == courseId) {
            String subjectCatalogNumber = courseDTO.catalogNumber;
            for (Course course : courses) {
                if (course.getSubjectCatalogNumber().equals(subjectCatalogNumber)) {
                    isCourseOfferingIdMatchesProvidedOfferingIdThenAddSectionsIntoSectionDTOList(course, offeringId);

                }
            }
        }
    }

    @GetMapping("/api/departments/{departmentId}/courses/{courseId}/offerings/{offeringId}")
    ResponseEntity<List<ApiOfferingSectionDTO>> getListOfSectionsOfParticularCourseOffering(
            @PathVariable("departmentId") long departmentId,
            @PathVariable("courseId") long courseId,
            @PathVariable("offeringId") long offeringId

    ) {

        offeringSectionsDTO.clear();
        String departmentName = getDepartmentName(departmentId);
        for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
            if (isSameDepartmentName(courses.get(0), departmentName)) {
                for (ApiCourseDTO courseDTO : coursesDTO) {
                    addCourseSectionsToOfferingSectionDTO(courseDTO, courseId, courses, offeringId);

                }
            }
        }
        if (offeringSectionsDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(offeringSectionsDTO, HttpStatus.OK);
    }


    private long getStartSemester(String departmentName) {
        long startSemester = Long.MAX_VALUE;
        for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
            if (isSameDepartmentName(courses.get(0), departmentName)) {
                for (Course course : courses) {
                    startSemester = Math.min(startSemester, Long.parseLong(course.getSemester().getSemesterCode()));
                }
            }
        }
        return startSemester;
    }

    private long getEndSemester(String departmentName) {

        long endSemester = Long.MIN_VALUE;
        for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
            if (isSameDepartmentName(courses.get(0), departmentName)) {
                for (Course course : courses) {
                    endSemester = Math.max(endSemester, Long.parseLong(course.getSemester().getSemesterCode()));

                }
            }
        }
        return endSemester;

    }

    private void addIntoModifiableStartValue() {
        if (START_SEMESTER_MODIFIABLE % 10 == 1 || START_SEMESTER_MODIFIABLE % 10 == 4) {
            START_SEMESTER_MODIFIABLE += 3;
        } else if (START_SEMESTER_MODIFIABLE % 10 == 7) {
            START_SEMESTER_MODIFIABLE += 4;
        }
    }


    @GetMapping("/api/stats/students-per-semester")
    public ResponseEntity<List<ApiGraphDataPointDTO>> getTotalStudentsEnrolledPerSemester(
            @RequestParam("deptId") long id) {
        List<ApiGraphDataPointDTO> graphDataPointsDTO = new ArrayList<>();

        String departmentName = getDepartmentName(id);
//        facade.setStartSemesterToOriginalStartSemester();
        if (departmentName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        long startSemesterForGraph = getStartSemester(departmentName);
        long endSemesterForGraph = getEndSemester(departmentName);
        START_SEMESTER_MODIFIABLE = startSemesterForGraph;

        while (startSemesterForGraph <= endSemesterForGraph) {
            long totalStudentsEnrolledPerSemester = 0;
            totalStudentsEnrolledPerSemester = getTotalStudentsEnrolledPerSemester(departmentName,
                    startSemesterForGraph, totalStudentsEnrolledPerSemester);
            ApiGraphDataPointDTO apiGraphDataPointDTO = new ApiGraphDataPointDTO(startSemesterForGraph,
                    totalStudentsEnrolledPerSemester);
            graphDataPointsDTO.add(apiGraphDataPointDTO);
            addIntoModifiableStartValue();
            startSemesterForGraph = START_SEMESTER_MODIFIABLE;
        }

        return new ResponseEntity<>(graphDataPointsDTO, HttpStatus.OK);
    }

    private long getTotalStudentsEnrolledPerSemester(String departmentName, long startSemesterForGraph,
                                                     long totalStudentsEnrolledPerSemester) {
        for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
            if (isSameDepartmentName(courses.get(0), departmentName)) {
                for (Course course : courses) {
                    totalStudentsEnrolledPerSemester = getTotalStudentsEnrolledPerSemester(startSemesterForGraph,
                            totalStudentsEnrolledPerSemester, course);
                }

            }
        }
        return totalStudentsEnrolledPerSemester;
    }

    private static long getTotalStudentsEnrolledPerSemester(long startSemesterForGraph,
                                                            long totalStudentsEnrolledPerSemester, Course course) {
        if (Long.parseLong(course.getSemester().getSemesterCode()) == startSemesterForGraph) {
            for (Section section : course.getSectionsList()) {
                if (section.getComponentCode().equals("LEC")) {
                    totalStudentsEnrolledPerSemester += section.getEnrolmentTotal();
                }
            }

        }
        return totalStudentsEnrolledPerSemester;
    }


    @PostMapping("api/addoffering")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiOfferingSectionDTO> addOffering(@RequestBody ApiOfferingDataDTO dto) {
        if (dto.semester == null || dto.subjectName == null || dto.catalogNumber == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        boolean isDepartmentAlreadyExist = false;
        for (ApiDepartmentDTO departmentDTO : departmentsDTO) {
            if (departmentDTO.name.equals(dto.subjectName)) {
                isDepartmentAlreadyExist = true;
                break;
            }
        }
        if (!isDepartmentAlreadyExist) {
            ApiDepartmentDTO newDepartmentDTO = new ApiDepartmentDTO(DEPARTMENT_ID_COUNTER, dto.subjectName);
            departmentsDTO.add(newDepartmentDTO);
            DEPARTMENT_ID_COUNTER++;
        }
        boolean isCourseCatalogNumberAlreadyExist = false;
        for (ApiCourseDTO courseDTO : coursesDTO) {
            if (courseDTO.catalogNumber.equals(dto.catalogNumber)) {
                isCourseCatalogNumberAlreadyExist = true;
                break;
            }
        }
        if (!isCourseCatalogNumberAlreadyExist) {
            ApiCourseDTO apiCourseDTO = new ApiCourseDTO(COURSE_ID_COUNTER, dto.catalogNumber);
            coursesDTO.add(apiCourseDTO);
        }

        facade.addNewOffering(dto);

        ApiOfferingSectionDTO newSectionDTO = new ApiOfferingSectionDTO(
                dto.component,
                dto.enrollmentCap,
                dto.enrollmentTotal
        );

        return new ResponseEntity<>(newSectionDTO, HttpStatus.CREATED);
    }


    @GetMapping("api/watchers")
    public ResponseEntity<List<ApiWatcherDTO>> getWatchers() {
        return new ResponseEntity<>(watchersDTO, HttpStatus.OK);

    }


    private String getCourseCatalogNumber(String departmentName, long courseId) {
        for (ApiCourseDTO apiCourseDTO : coursesDTO) {
            if (apiCourseDTO.courseId == courseId) {
                return apiCourseDTO.catalogNumber;
            }
        }
        return null;
    }

    @PostMapping("/api/watchers")
    public ResponseEntity<HttpStatus> createWatcher(@RequestBody ApiWatcherCreateDTO apiWatcherCreateDTO) {
        long deptId = apiWatcherCreateDTO.deptId;
        long courseId = apiWatcherCreateDTO.courseId;
        String departmentName = getDepartmentName(deptId);
        if (departmentName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String courseCatalogNumber = getCourseCatalogNumber(departmentName, courseId);
        if (courseCatalogNumber == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        ApiDepartmentDTO departmentDTO = new ApiDepartmentDTO(deptId, departmentName);
        ApiCourseDTO courseDTO = new ApiCourseDTO(courseId, courseCatalogNumber);
        List<String> events = new ArrayList<>();
        ApiWatcherDTO apiWatcherDTO = new ApiWatcherDTO(WATCHER_ID_COUNTER, departmentDTO, courseDTO, events);
        facade.addObserver(new CourseSectionChangeObserver() {
            @Override
            public long getId() {
                return WATCHER_ID_COUNTER;
            }

            @Override
            public void newSectionBeingAdded(Course course) {

                String watcherCatalogNumber = apiWatcherDTO.course.catalogNumber;
                String watcherDepartmentName = apiWatcherDTO.department.name;

                if (watcherCatalogNumber.equals(course.getSubjectCatalogNumber()) &&
                        watcherDepartmentName.equals(course.getSubjectName())) {
                    ZonedDateTime zonedDateTime = ZonedDateTime.now();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z yyyy");
                    String formattedDate = zonedDateTime.format(dateTimeFormatter);
                    apiWatcherDTO.events.add(formattedDate + " Added section " + course.getSection().getComponentCode()
                            + " with enrollment (" + course.getSection().getEnrolmentTotal() + "/"
                            + course.getSection().getEnrolmentCapacity() + ")" +
                            " to offering " + course.getSemester().getTerm() + " " + course.getSemester().getYear());

                }
            }


        });
        watchersDTO.add(apiWatcherDTO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/api/watchers/{id}")
    public ResponseEntity<ApiWatcherDTO> getWatcher(@PathVariable long id) {
        for (ApiWatcherDTO apiWatcherDTO : watchersDTO) {
            if (apiWatcherDTO.id == id) {
                return new ResponseEntity<>(apiWatcherDTO, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/api/watchers/{id}")
    public ResponseEntity<HttpStatus> deleteWatcher(@PathVariable long id) {

        ApiWatcherDTO apiWatcherDTOToBeDeleted = null;
        for (ApiWatcherDTO apiWatcherDTO : watchersDTO) {
            if (apiWatcherDTO.id == id) {
                apiWatcherDTOToBeDeleted = apiWatcherDTO;
                break;
            }
        }
        if (apiWatcherDTOToBeDeleted != null) {
            watchersDTO.remove(apiWatcherDTOToBeDeleted);
            facade.removeObserver(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}

