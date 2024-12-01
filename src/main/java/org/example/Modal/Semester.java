package org.example.Modal;

public class Semester {
    private final String semesterCode;
    private int year;
    private String term;

    public Semester(String semesterCode) {
        this.semesterCode = semesterCode;
        int semesterNum = Integer.parseInt(semesterCode);
        this.year = semesterNum / 10;
        int termNum = semesterNum % 10;

        switch (termNum) {
            case 1 -> this.term = "Spring";
            case 4 -> this.term = "Summer";
            case 7 -> this.term = "Fall";
        }
    }

    public String getSemesterCode() {
        return semesterCode;
    }

    public int getYear() {
        return this.year;
    }

    public String getTerm() {
        return this.term;
    }
}
