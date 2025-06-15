package models;

import java.time.LocalDate;

public class NegativeNote {
    private int id;
    private int studentId;
    private int classId;
    private int points;
    private String noteText;
    private LocalDate dateAssigned;

    public NegativeNote(int id, int studentId, int classId, int points, String noteText, LocalDate dateAssigned) {
        this.id = id;
        this.studentId = studentId;
        this.classId = classId;
        this.points = points;
        this.noteText = noteText;
        this.dateAssigned = dateAssigned;
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getClassId() {
        return classId;
    }

    public int getPoints() {
        return points;
    }

    public String getNoteText() {
        return noteText;
    }

    public LocalDate getDateAssigned() {
        return dateAssigned;
    }

}
