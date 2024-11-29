package org.example.Modal;

import org.example.Modal.CourseLocation.CampusLocation;
import org.example.Modal.CourseLocation.Location;
import org.example.Modal.CourseOfferingsDetails.CourseOfferingDetails;
import org.example.Modal.CourseOfferingsDetails.OfferringDetails;
import org.example.Modal.InstructorDetails.Instructor;
import org.example.Modal.InstructorDetails.InstructorName;
import org.example.Modal.Semester.Semester;
import org.example.Modal.Semester.SemesterName;
import org.example.Modal.Subject.Subject;
import org.example.Modal.Subject.SubjectDetails;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExtractDataFromFile {
    public List<CourseDetail> courses=new ArrayList<>();
    CourseGroup courseGroup=new CourseGroup();
    public void extractDataFromFile(){
        try{
            FileReader fileReader=new FileReader("./data/small_data.csv");
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            String line;
            bufferedReader.readLine();
            while((line=bufferedReader.readLine())!=null){
                String[] data=line.split(",");
                Semester semester=new SemesterName(data[0]);
              SubjectDetails subjectDetails=new Subject(data[1],data[2]);
              CampusLocation campusLocation=new Location(data[3]);
              Instructor instructor=new InstructorName(data[6]);
              int enrollmentTotal=Integer.parseInt(data[4]);
                int enrollmentCapacity=Integer.parseInt(data[5]);
                OfferringDetails offerringDetails=new CourseOfferingDetails(data[7],enrollmentCapacity,enrollmentTotal);


              courses.add(new CourseDetail(semester,subjectDetails,instructor,campusLocation,offerringDetails));
            courseGroup.groupCourses(courses.get(courses.size()-1));
            }
        }catch(FileNotFoundException e){
            System.out.println("File not found" +e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//public static void main(String[] args) {
//        ExtractDataFromFile extractDataFromFile=new ExtractDataFromFile();
//        extractDataFromFile.extractDataFromFile();
//}


}
