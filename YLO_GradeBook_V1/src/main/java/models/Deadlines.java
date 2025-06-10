package models;

import java.time.LocalDate;

public class Deadlines {
    private int id;
    private int classId;
    private String type;
    private LocalDate deadlineDate;
    private String description;
    private int subjectId;
    private LocalDate dateAssigned;

    public Deadlines(int id, int classId, String type, LocalDate deadlineDate,
                     String description, int subjectId, LocalDate dateAssigned) {
        this.id = id;
        this.classId = classId;
        this.type = type;
        this.deadlineDate = deadlineDate;
        this.description = description;
        this.subjectId = subjectId;
        this.dateAssigned = dateAssigned;
    }

    private Subject subject;

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }


    public int getId() {
        return id;
    }

    public int getClassId() {
        return classId;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDeadlineDate() {
        return deadlineDate;
    }

    public String getDescription() {
        return description;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public LocalDate getDateAssigned() {
        return dateAssigned;
    }

    // (opcjonalnie dodaj settery, jeśli planujesz edytowalność)
}
