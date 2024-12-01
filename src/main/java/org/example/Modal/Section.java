package org.example.Modal;

public class Section {
    private final String componentCode;
    private int enrolmentCapacity;
    private int enrolmentTotal;

    public Section(String componentCode, int enrolmentCapacity, int enrolmentTotal) {
        this.componentCode = componentCode;
        this.enrolmentCapacity = enrolmentCapacity;
        this.enrolmentTotal = enrolmentTotal;
    }

    public void addEnrolment(int toBeEnrolled, int extraCapacity) {
        this.enrolmentTotal += toBeEnrolled;
        this.enrolmentCapacity += extraCapacity;
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

}
