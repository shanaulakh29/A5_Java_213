package org.example.Modal;

public interface CourseSectionChangeObserver{
    long getId();
    void newSectionBeingAdded(Course course);
}
