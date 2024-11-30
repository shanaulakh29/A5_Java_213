package org.example.Modal;

public class Subject {
    private String department;
    private String catalogNumber;

    public Subject(String department, String catalogNumber) {
        this.department = department;
        this.catalogNumber = catalogNumber;
    }

    public String getDepartment() {
        return department;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }
}
