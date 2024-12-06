package org.example.Modal;

public class Semester {
    private String semesterCode;
    private int year;
    private String term;

    public Semester(String semesterCode) {
        this.semesterCode = semesterCode;
        int semesterNum = Integer.parseInt(semesterCode);
        int yearToBeCalculated = semesterNum / 10;
        this.year = calculateYear(yearToBeCalculated);

        int termNum = semesterNum % 10;

        switch (termNum) {
            case 1 -> this.term = "Spring";
            case 4 -> this.term = "Summer";
            case 7 -> this.term = "Fall";
        }
    }

    public int calculateYear(int yearToBeCalculated) {
        int z = yearToBeCalculated % 10;
        yearToBeCalculated = yearToBeCalculated / 10;
        int y = yearToBeCalculated % 10;
        yearToBeCalculated = yearToBeCalculated / 10;
        int x = yearToBeCalculated % 10;
        return 1900 + 100 * x + 10 * y + z;
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

    @Override
    public boolean equals(Object o) {
        Semester semester = (Semester) o;
        return this.semesterCode.equals(semester.semesterCode);
    }

}

