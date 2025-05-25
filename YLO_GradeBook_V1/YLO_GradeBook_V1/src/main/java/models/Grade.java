package models;

import java.time.LocalDate;

public class Grade {
    private int id;
    private int studentId;
    private int subjectId;
    private int gradeValue;
    private LocalDate dateAssigned;

    public Grade(int id, int studentId, int subjectId, int gradeValue, LocalDate dateAssigned) {
        this.id = id;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.gradeValue = gradeValue;
        this.dateAssigned = dateAssigned;
    }

    public int getId() { return id; }
    public int getStudentId() { return studentId; }
    public int getSubjectId() { return subjectId; }
    public int getGradeValue() { return gradeValue; }
    public LocalDate getDateAssigned() { return dateAssigned; }

}
