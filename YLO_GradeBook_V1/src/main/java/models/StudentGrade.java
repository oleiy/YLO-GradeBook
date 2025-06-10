package models;

import java.time.LocalDate;

public class StudentGrade {
    private int journalNumber;
    private String firstName;
    private String lastName;
    private String grades;
    private double averageGrade;
    private Subject subject;
    private LocalDate date;
    private String type;

    public StudentGrade(int journalNumber, String firstName, String lastName, String grades, double averageGrade) {
        this.journalNumber = journalNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.grades = grades;
        this.averageGrade = averageGrade;
    }

    public StudentGrade(Subject subject, String grade, LocalDate date, String type) {
        this.subject = subject;
        this.grades = grade;
        this.date = date;
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getJournalNumber() {
        return journalNumber;
    }


    public void setAverageGrade(double averageGrade) {
        this.averageGrade = averageGrade;
    }

    public String getType() {
        return type;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGrades() {
        return grades;
    }


    public Subject getSubject() {
        return subject;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAverageGrade() {
        return averageGrade;
    }


}
