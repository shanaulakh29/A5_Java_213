// Watcher
package org.example.Modal;

import java.util.ArrayList;
import java.util.List;

public class Watcher {
    private long id;
    private long deptId;
    private String deptName;
    private long courseId;
    private String catalogNumber;
    private List<String> events = new ArrayList<>();

    public Watcher(long id, long deptId, String deptName, long courseId, String catalogNumber) {
        this.id = id;
        this.deptId = deptId;
        this.deptName = deptName;
        this.courseId = courseId;
        this.catalogNumber = catalogNumber;
    }

    public long getId() {
        return id;
    }

    public long getDeptId() {
        return deptId;
    }

    public String getDepartmentName() {
        return deptName;
    }

    public long getCourseId() {
        return courseId;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public List<String> getEvents() {
        return events;
    }

    public void addObserver() {
        Observer observer = new Observer() {
            @Override
            public void stateChanged(String event) {
                events.add(event);
            }
        };
    }
}
//