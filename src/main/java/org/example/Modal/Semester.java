package org.example.Modal;

public class Semester {
    private int year;
    private String term;

    public Semester(String semesterCode) {
        int semesterNum = Integer.parseInt(semesterCode);
        this.year = semesterNum / 10;
        int termNum = semesterNum % 10;

        switch (termNum) {
            case 1 -> this.term = "Spring";
            case 4 -> this.term = "Summer";
            case 7 -> this.term = "Fall";
        }
    }


}
