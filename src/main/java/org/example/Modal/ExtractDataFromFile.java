package org.example.Modal;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExtractDataFromFile {
    public List<CourseDetail> courses = new ArrayList<>();
    CourseGroup courseGroup = new CourseGroup();

    public void extractDataFromFile() {
        try {
            FileReader fileReader = new FileReader("./data/course_data_2018.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                List<String> data = new ArrayList<>();
                boolean quotationFound = false;
                StringBuilder currentValue = new StringBuilder();
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (c == '\"') {
                        quotationFound = !quotationFound;
                    } else if ((c == ',' && !quotationFound)) {
                        data.add(currentValue.toString().trim());
                        currentValue = new StringBuilder();
                    } else if (i == line.length() - 1) {
                        currentValue.append(c);
                        data.add(currentValue.toString().trim());
                        currentValue = new StringBuilder();

                    } else {
                        currentValue.append(c);
                    }
                }

                for (int i = 0; i < data.size(); i++) {
                    System.out.println(data.get(i));
                }
                Semester semester = new SemesterName(data.get(0));
                SubjectDetails subjectDetails = new Subject(data.get(1), data.get(2));
                CampusLocation campusLocation = new Location(data.get(3));
                List<String> instructorNames = Arrays.stream((data.get(6).split(","))).toList();

                Instructor instructor = new InstructorName(instructorNames);
                // DEBUG //
                System.out.println("Ignore after this");
                for (String name : instructorNames) {
                    System.out.println(name);
                }
                System.out.println("Enrol Cap: " + data.get(4) + "of " + semester.getSemesterCode() + "Subject " + subjectDetails.getSubjectName() + subjectDetails.getSubjectCode());
                System.out.println("Enrol Cap: " + data.get(5) + "of " + semester.getSemesterCode() + "Subject " + subjectDetails.getSubjectName() + subjectDetails.getSubjectCode());
                // END OF DEBUG //
                int enrollmentTotal = Integer.parseInt(data.get(4));
                int enrollmentCapacity = Integer.parseInt(data.get(5));
                OfferringDetails offerringDetails = new CourseOfferingDetails(data.get(7), enrollmentCapacity, enrollmentTotal);


                courses.add(new CourseDetail(semester, subjectDetails, instructor, campusLocation, offerringDetails));
                courseGroup.groupCourses(courses.get(courses.size() - 1));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//public static void main(String[] args) {
//        ExtractDataFromFile extractDataFromFile=new ExtractDataFromFile();
//        extractDataFromFile.extractDataFromFile();
//}


}
