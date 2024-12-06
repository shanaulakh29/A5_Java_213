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

//    private void addAllCoursesBasedOnDepartmentInCoursesDTOList(List<Course> courses) {
//        long courseIdCounter = 1;
//        for (Course course : courses) {
//            if (!containsCourse(coursesDTO, course.getSubjectCatalogNumber())) {
//                ApiCourseDTO courseDTO = new ApiCourseDTO(courseIdCounter, course.getSubjectCatalogNumber());
//                coursesDTO.add(courseDTO);
//                courseIdCounter++;
//            }
//        }
//    }

    @GetMapping("/api/departments/{id}/courses")
    public ResponseEntity<List<ApiCourseDTO>> getCoursesFromSpecificDepartment(@PathVariable long id) {
        System.out.println("Department ID is :" + id);

        String departmentName = getDepartmentName(id);
        for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
            if (isSameDepartmentName(courses.get(0), departmentName)) {
                for (Course course : courses) {

                    if (!containsCourse(coursesDTO, course.getSubjectCatalogNumber())) {
                        ApiCourseDTO courseDTO = new ApiCourseDTO(COURSE_ID_COUNTER, course.getSubjectCatalogNumber());
                        coursesDTO.add(courseDTO);
                        COURSE_ID_COUNTER++;
                    }
                }
                System.out.println("-----------------------------------------------------");
            }
        }

        Collections.sort(coursesDTO);
        return new ResponseEntity<>(coursesDTO, HttpStatus.OK);
    }


    private void addCourseOfferingsToCourseOfferingsDTOList(ApiCourseDTO courseDTO, Long courseId, List<Course> courses) {
        if (courseDTO.courseId == courseId) {
            String subjectCatalogNumber = courseDTO.catalogNumber;
            long offeringIdCounter = 1;
            for (Course course : courses) {
                if (course.getSubjectCatalogNumber().equals(subjectCatalogNumber)) {
                    //get courses offered in particular catalog number
                    ApiCourseOfferingDTO apiCourseOfferingDTO = new ApiCourseOfferingDTO(offeringIdCounter,
                            course.getLocation(), course.getInstructorsNamesForPrinting(), course.getSemester().getTerm(),
                            Long.parseLong(course.getSemester().getSemesterCode()), course.getSemester().getYear());
                    courseOfferingsDTO.add(apiCourseOfferingDTO);
                    offeringIdCounter++;
                }
            }

        }
    }

    @GetMapping("/api/departments/{departmentId}/courses/{courseId}/offerings")
    ResponseEntity<List<ApiCourseOfferingDTO>> getCourseOfferingsOfParticularCourse(@PathVariable("departmentId") long departmentId,
                                                                                    @PathVariable("courseId") long courseId) {
        System.out.println("Department ID is :" + departmentId);
        System.out.println("Course ID is :" + courseId);
        String departmentName = getDepartmentName(departmentId);
        for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
            if (isSameDepartmentName(courses.get(0), departmentName)) {
                for (ApiCourseDTO courseDTO : coursesDTO) {
                    addCourseOfferingsToCourseOfferingsDTOList(courseDTO, courseId, courses);

                }
            }
        }

        return new ResponseEntity<>(courseOfferingsDTO, HttpStatus.OK);
    }


    private void addIntoSectionsDTOListIfCourseInfoMatches(ApiCourseOfferingDTO apiCourseOfferingDTO, Course course) {
        String location = apiCourseOfferingDTO.location;
        String term = apiCourseOfferingDTO.term;
        long semesterCode = apiCourseOfferingDTO.semesterCode;
        long year = apiCourseOfferingDTO.year;
        String instructors = apiCourseOfferingDTO.instructors;
        if (location.equals(course.getLocation()) && semesterCode == Long.parseLong(course.getSemester().getSemesterCode())
                && year == course.getSemester().getYear() && instructors.equals(course.getInstructorsNamesForPrinting())) {
            for (Section section : course.getSectionsList()) {
                ApiOfferingSectionDTO apiOfferingSectionDTO = new ApiOfferingSectionDTO(section.getComponentCode(), section.getEnrolmentTotal(), section.getEnrolmentCapacity());
                offeringSectionsDTO.add(apiOfferingSectionDTO);
            }


        }
    }

    private void isCourseOfferingIdMatchesProvidedOfferingIdThenAddSectionsIntoSectionDTOList(Course course, long offeringId) {
        for (ApiCourseOfferingDTO apiCourseOfferingDTO : courseOfferingsDTO) {
            if (apiCourseOfferingDTO.courseOfferingId == offeringId) {
                addIntoSectionsDTOListIfCourseInfoMatches(apiCourseOfferingDTO, course);
            }
        }
    }

    private void addCourseSectionsToOfferingSectionDTO(ApiCourseDTO courseDTO, Long courseId, List<Course> courses, long offeringId) {
        if (courseDTO.courseId == courseId) {
            String subjectCatalogNumber = courseDTO.catalogNumber;
//        long offeringIdCounter = 1;
            for (Course course : courses) {
                if (course.getSubjectCatalogNumber().equals(subjectCatalogNumber)) {//get courses offered in particular catalog number
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

        String departmentName = getDepartmentName(departmentId);
        for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
            if (isSameDepartmentName(courses.get(0), departmentName)) {
                for (ApiCourseDTO courseDTO : coursesDTO) {
                    addCourseSectionsToOfferingSectionDTO(courseDTO, courseId, courses, offeringId);

                }
            }
        }


        return new ResponseEntity<>(offeringSectionsDTO, HttpStatus.OK);
    }



     private long getStartSemester(String departmentName) {
        boolean isFirstLoop=true;
        long startSemester = 0;
        for(List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
            if(isSameDepartmentName(courses.get(0), departmentName)){
                for(Course course : courses) {
                    if(isFirstLoop){
                        startSemester=Long.parseLong(course.getSemester().getSemesterCode());
                        isFirstLoop=false;
                    }
                    if(!isFirstLoop){
                        if(startSemester>Long.parseLong(course.getSemester().getSemesterCode())){
                            startSemester=Long.parseLong(course.getSemester().getSemesterCode());
                        }
                    }
                }
            }
        }
        return startSemester;
     }
     private long getEndSemester(String departmentName) {
        boolean isFirstLoop=true;
        long endSemester = 0;
         for(List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
             if(isSameDepartmentName(courses.get(0), departmentName)){
                 for(Course course : courses) {
                     if(isFirstLoop){
                         endSemester=Long.parseLong(course.getSemester().getSemesterCode());
                         isFirstLoop=false;
                     }
                     if(!isFirstLoop){
                         if(endSemester<Long.parseLong(course.getSemester().getSemesterCode())){
                             endSemester=Long.parseLong(course.getSemester().getSemesterCode());
                         }
                     }
                 }
             }
         }
        return endSemester;

     }
     private void addIntoModifiableStartValue(){
         if(START_SEMESTER_MODIFIABLE%10==1 || START_SEMESTER_MODIFIABLE%10==4){
             START_SEMESTER_MODIFIABLE+=3;
         }else if(START_SEMESTER_MODIFIABLE%10==7){
             START_SEMESTER_MODIFIABLE+=4;
         }
     }

    @GetMapping("/api/stats/students-per-semester")
    public ResponseEntity<List<ApiGraphDataPointDTO>> getTotalStudentsEnrolledPerSemester(@RequestParam("deptId") long id) {
        List<ApiGraphDataPointDTO> graphDataPointsDTO = new ArrayList<>();

        String departmentName = getDepartmentName(id);
//        facade.setStartSemesterToOriginalStartSemester();

        long startSemesterForGraph= getStartSemester(departmentName);
        long endSemesterForGraph = getEndSemester(departmentName);
        START_SEMESTER_MODIFIABLE=startSemesterForGraph;

        System.out.println("StartSemesterForGraph: " + startSemesterForGraph);
        System.out.println("EndSemesterForGraph: " + endSemesterForGraph);
        while(startSemesterForGraph<=endSemesterForGraph) {
            long totalStudentsEnrolledPerSemester=0;
            for(List<Course> courses : AllGroupedCoursesBelongingToSameSubject){
                if(isSameDepartmentName(courses.get(0), departmentName)) {
                    for(Course course : courses) {
                        if(Long.parseLong(course.getSemester().getSemesterCode())==startSemesterForGraph) {
                            totalStudentsEnrolledPerSemester+=course.getSection().getEnrolmentTotal();
                        }
                    }
                }
            }
            ApiGraphDataPointDTO apiGraphDataPointDTO=new ApiGraphDataPointDTO(startSemesterForGraph,
                    totalStudentsEnrolledPerSemester);
            graphDataPointsDTO.add(apiGraphDataPointDTO);
            addIntoModifiableStartValue();
           startSemesterForGraph=START_SEMESTER_MODIFIABLE;
            System.out.println("StartSemesterIncremented " + startSemesterForGraph);
        }

        return new ResponseEntity<>(graphDataPointsDTO, HttpStatus.OK);
    }

//    @GetMapping("/api/stats/students-per-semester")
//    public ResponseEntity<List<ApiGraphDataPointDTO>> getTotalStudentsEnrolledPerSemester(@RequestParam("deptId") long id) {
//        List<ApiGraphDataPointDTO> graphDataPointsDTO = new ArrayList<>();
//        String departmentName = getDepartmentName(id);
//        facade.setStartSemesterToOriginalStartSemester();
//      long startSemesterForGraph= facade.getOriginalStartSemester();
//      long endSemesterForGraph = facade.getEndSemesterForDepartmentDataFromCSV();
//
//        System.out.println("StartSemesterForGraph: " + startSemesterForGraph);
//        System.out.println("EndSemesterForGraph: " + endSemesterForGraph);
//      while(startSemesterForGraph<=endSemesterForGraph) {
//          long totalStudentsEnrolledPerSemester=0;
//          for(List<Course> courses : AllGroupedCoursesBelongingToSameSubject){
//              if(isSameDepartmentName(courses.get(0), departmentName)) {
//                  for(Course course : courses) {
//                   if(Long.parseLong(course.getSemester().getSemesterCode())==startSemesterForGraph) {
//                       totalStudentsEnrolledPerSemester+=course.getSection().getEnrolmentTotal();
//                   }
//                  }
//              }
//          }
//          ApiGraphDataPointDTO apiGraphDataPointDTO=new ApiGraphDataPointDTO(startSemesterForGraph,
//                  totalStudentsEnrolledPerSemester);
//          graphDataPointsDTO.add(apiGraphDataPointDTO);
//     facade.addIntoStartSemester();
//     startSemesterForGraph = facade.getStartSemesterForDepartmentDataFromCSV();
//          System.out.println("StartSemesterIncremented " + startSemesterForGraph);
//      }
//
//        return new ResponseEntity<>(graphDataPointsDTO, HttpStatus.OK);
//    }

//        for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
//            if (isSameDepartmentName(courses.get(0), departmentName)) {
//               for(ApiCourseOfferingDTO apiCourseOfferingDTO : courseOfferingsDTO) {
//                   for(Course course: courses) {
//                       if(apiCourseOfferingDTO.semesterCode==Long.parseLong(course.getSemester().getSemesterCode())){
//
//                       }
//                   }
//
//               }
//            }
//            }









    @PostMapping("api/addoffering")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiOfferingSectionDTO> addOffering(@RequestBody ApiOfferingDataDTO dto) {
        if (dto.semester == null || dto.subjectName == null || dto.catalogNumber == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        boolean isDepartmentAlreadyExist=false;
       for(ApiDepartmentDTO departmentDTO: departmentsDTO){
           if(departmentDTO.name.equals(dto.subjectName)){
               isDepartmentAlreadyExist=true;
               break;
           }
       }
       if(!isDepartmentAlreadyExist){
           ApiDepartmentDTO newDepartmentDTO=new ApiDepartmentDTO(DEPARTMENT_ID_COUNTER,dto.subjectName);
           departmentsDTO.add(newDepartmentDTO);
           DEPARTMENT_ID_COUNTER++;
       }
       boolean isCourseCatalogNumberAlreadyExist=false;
       for(ApiCourseDTO courseDTO: coursesDTO){
           if(courseDTO.catalogNumber.equals(dto.catalogNumber)){
             isCourseCatalogNumberAlreadyExist=true;
           }
       }
       if(!isCourseCatalogNumberAlreadyExist){
           ApiCourseDTO apiCourseDTO=new ApiCourseDTO(COURSE_ID_COUNTER,dto.catalogNumber);
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
        System.out.println("Entered Get api/watchers");
        return new ResponseEntity<>(watchersDTO, HttpStatus.OK);

    }


    private String getCourseCatalogNumber(String departmentName, long courseId) {
//        for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject){
//            if(isSameDepartmentName(courses.get(0), departmentName)){
        for (ApiCourseDTO apiCourseDTO : coursesDTO) {
            if (apiCourseDTO.courseId == courseId) {
                return apiCourseDTO.catalogNumber;
            }
//                }
//            }
        }
        return null;
    }

    @PostMapping("/api/watchers")
    public ResponseEntity<HttpStatus> createWatcher(@RequestBody ApiWatcherCreateDTO apiWatcherCreateDTO) {
        long deptId = apiWatcherCreateDTO.deptId;
        long courseId = apiWatcherCreateDTO.courseId;
        String departmentName = getDepartmentName(deptId);
        if (departmentName == null) {
            System.out.println("Department name is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String courseCatalogNumber = getCourseCatalogNumber(departmentName, courseId);
        if (courseCatalogNumber == null) {
            System.out.println("Course catalog number is null");
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

                if (watcherCatalogNumber.equals(course.getSubjectCatalogNumber()) && watcherDepartmentName.equals(course.getSubjectName())) {
                    ZonedDateTime zonedDateTime = ZonedDateTime.now();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z yyyy");
                    String formattedDate = zonedDateTime.format(dateTimeFormatter);
                    apiWatcherDTO.events.add(formattedDate + " Added section " + course.getSection().getComponentCode() + " with enrollment (" + course.getSection().getEnrolmentTotal() + "/" + course.getSection().getEnrolmentCapacity() + ")" +
                            " to offering " + course.getSemester().getTerm() + " " + course.getSemester().getYear());

                }
            }


        });
        watchersDTO.add(apiWatcherDTO);
        System.out.println("apiWatcherDTO length is" + watchersDTO.size());

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

//@GetMapping("/api/departments/{departmentId}/courses/{courseId}/offerings")
//ResponseEntity<List<ApiCourseOfferingDTO>> getCourseOfferingsOfParticularCourse(@PathVariable("departmentId") long departmentId,
//                                                                                @PathVariable("courseId") long courseId) {
//        for (ApiDepartmentDTO departmentDTO : departmentsDTO) {
//            if (departmentDTO.getDeptId() == id) {
//                String departmentDTOName = departmentDTO.getName();
//                for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
//                    if (courses.get(0).getSubjectName().toUpperCase().equals(departmentDTOName)) {
//                        for (Course course : courses) {
//                            if (!containsCourse(coursesDTO, course.getSubjectCatalogNumber())) {
//                                ApiCourseDTO courseDTO = new ApiCourseDTO(courseIdCounter, course.getSubjectCatalogNumber());
//                                coursesDTO.add(courseDTO);
//                                courseIdCounter++;
//                            }
//
//                        }
//                        System.out.println("-----------------------------------------------------");
//                    }
//                }
//                System.out.println("----");
//            }
//        }

//@GetMapping("/api/departments/{departmentId}/courses/{courseId}/offerings")
//ResponseEntity<List<ApiCourseOfferingDTO>> getCourseOfferingsOfParticularCourse(@PathVariable("departmentId") long departmentId,
//                                                                                @PathVariable("courseId") long courseId) {
//        for (ApiDepartmentDTO departmentDTO : departmentsDTO) {
//            if (departmentDTO.getDeptId() == departmentId) {
//                String departmentDTOName = departmentDTO.getName();
//                for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
//                    if (courses.get(0).getSubjectName().toUpperCase().equals(departmentDTOName)) {//get Specific list that contains only courses belonging to that department
//                        for (ApiCourseDTO courseDTO : coursesDTO) {
//                            if (courseDTO.courseId == courseId) {
//                                String subjectCatalogNumber = courseDTO.catalogNumber;
//                                long offeringIdCounter = 1;
//                                for (Course course : courses) {
//                                    if (course.getSubjectCatalogNumber().equals(subjectCatalogNumber)) {//get courses offered in particular catalog number
//                                        ApiCourseOfferingDTO apiCourseOfferingDTO = new ApiCourseOfferingDTO(offeringIdCounter,
//                                                course.getLocation(), course.getInstructorsNamesForPrinting(), course.getSemester().getTerm(),
//                                                Long.parseLong(course.getSemester().getSemesterCode()), course.getSemester().getYear());
//                                        courseOfferingsDTO.add(apiCourseOfferingDTO);
//                                        offeringIdCounter++;
//                                    }
//                                }
//
//                            }
//                        }
//                    }
//                }
//
//            }
//        }


//@GetMapping("/api/departments/{departmentId}/courses/{courseId}/offerings/{offeringId}")
//ResponseEntity<List<ApiOfferingSectionDTO>> getListOfSectionsOfParticularCourseOffering(
//        for (ApiDepartmentDTO departmentDTO : departmentsDTO) {
//            if (departmentDTO.getDeptId() == departmentId) {
//                String departmentDTOName = departmentDTO.getName();
//                for (List<Course> courses : AllGroupedCoursesBelongingToSameSubject) {
//                    if (courses.get(0).getSubjectName().toUpperCase().equals(departmentDTOName)) {//get Specific list that contains only courses belonging to that department
//                        for (ApiCourseDTO courseDTO : coursesDTO) {
//                            if (courseDTO.courseId == courseId) {
//                                String subjectCatalogNumber = courseDTO.catalogNumber;
//                                long offeringIdCounter = 1;
//                                for (Course course : courses) {
//                                    if (course.getSubjectCatalogNumber().equals(subjectCatalogNumber)) {//get courses offered in particular catalog number
//                                        for (ApiCourseOfferingDTO apiCourseOfferingDTO : courseOfferingsDTO) {
//                                            if (apiCourseOfferingDTO.courseOfferingId == offeringId) {
//                                                String location = apiCourseOfferingDTO.location;
//                                                String term = apiCourseOfferingDTO.term;
//                                                long semesterCode = apiCourseOfferingDTO.semesterCode;
//                                                long year = apiCourseOfferingDTO.year;
//                                                String instructors = apiCourseOfferingDTO.instructors;
//                                                if (location.equals(course.getLocation()) && semesterCode == Long.parseLong(course.getSemester().getSemesterCode())
//                                                        && year == course.getSemester().getYear() && instructors.equals(course.getInstructorsNamesForPrinting())) {
//                                                    for (Section section : course.getSectionsList()) {
//                                                        ApiOfferingSectionDTO apiOfferingSectionDTO = new ApiOfferingSectionDTO(section.getComponentCode(), section.getEnrolmentTotal(), section.getEnrolmentCapacity());
//                                                        offeringSectionsDTO.add(apiOfferingSectionDTO);
//                                                    }
//
//
//                                                }
//
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }