package org.example.Modal;

public class Section implements Comparable<Section>{
    private final String componentCode;
    private int enrolmentCapacity;
    private int enrolmentTotal;

    public Section(String componentCode, int enrolmentCapacity, int enrolmentTotal) {
        this.componentCode = componentCode;
        this.enrolmentCapacity = enrolmentCapacity;
        this.enrolmentTotal = enrolmentTotal;
    }

    public void addEnrolment(int extraCapacity,int extraEnrollment) {
        this.enrolmentCapacity += extraCapacity;
        this.enrolmentTotal += extraEnrollment;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public int getEnrolmentCapacity() {
        return enrolmentCapacity;
    }

    public int getEnrolmentTotal() {
        return enrolmentTotal;
    }
    @Override
    public int compareTo(Section other) {
        return this.getComponentCode().compareTo(other.getComponentCode());
    }
}
