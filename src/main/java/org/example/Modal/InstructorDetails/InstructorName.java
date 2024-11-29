package org.example.Modal.InstructorDetails;

public class InstructorName implements Instructor {
  private String instructorName;
  public InstructorName(String instructorName) {
      this.instructorName = instructorName;
  }

    @Override
    public String getInstructorName() {
      return instructorName;
    }
}
