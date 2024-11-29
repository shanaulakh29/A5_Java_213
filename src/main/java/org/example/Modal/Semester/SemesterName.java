package org.example.Modal.Semester;

public class SemesterName implements Semester {
    private String semesterName;
    private String semesterCode;
    public SemesterName(String semesterCode) {
       this.semesterName= this.getSemesterNameAsPerSemesterCode(semesterCode);
       this.semesterCode = semesterCode;
    }
@Override
   public String getSemesterCode(){
        return semesterCode;
   }

    public String getSemesterNameAsPerSemesterCode(String semesterCode) {

        int length=semesterCode.length();
        if(semesterCode.charAt(length-1)=='7'){
            return "Fall";
        }else if(semesterCode.charAt(length-1)=='1'){
            return "Spring";
        }else if(semesterCode.charAt(length-1)=='4'){
            return "Summer";
        }else{
            throw new IllegalArgumentException("Invalid semester code");
        }
    }
    @Override
    public String getSemesterName() {
        return semesterName;
    }

}
