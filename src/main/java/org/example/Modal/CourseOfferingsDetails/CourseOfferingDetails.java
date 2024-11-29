package org.example.Modal.CourseOfferingsDetails;

import org.example.Modal.*;

public class CourseOfferingDetails implements OfferringDetails {

    private String componentCode;
    private int enrollmentTotal;
    private int enrollmentCapacity;
    public CourseOfferingDetails(String componentCode, int enrollmentTotal, int enrollmentCapacity) {
        this.componentCode = componentCode;
        this.enrollmentTotal = enrollmentTotal;
        this.enrollmentCapacity = enrollmentCapacity;

    }


    @Override
    public String getComponentCode() {
        return componentCode;
    }

    @Override
    public int getEnrollmentCapacity() {
        return enrollmentCapacity;
    }

    @Override
    public int getEnrollmentTotal() {
        return enrollmentTotal;
    }
}
//        this.code=new CourseComponentCode(componentCode);
//    public enrollmentDetailsBasedOnComponentCode(String componentCode){
//        switch(componentCode){
//            case "Lec"->
//        }
//    }
//    private ComponentCode code;
//    private CourseLabDetails courseLabDetails;
//    private CourseLecDetails courseLecDetails;