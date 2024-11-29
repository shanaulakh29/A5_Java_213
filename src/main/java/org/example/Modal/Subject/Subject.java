package org.example.Modal.Subject;

public class Subject implements SubjectDetails {
    private String subjectName;
    private String subjectCode;
    public Subject(String subjectName, String subjectCode) {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
    }
    @Override
  public String getSubjectName() {
        return subjectName;
  }
    @Override
  public String getSubjectCode() {
        return subjectCode;
  }


    @Override
    public boolean equals(Object obj){
        Subject subject = (Subject) obj;
        return this.getSubjectName().equals(subject.getSubjectName())&&
                this.getSubjectCode().equals(subject.getSubjectCode());

    }

}
