package com.ylo.ylo_gradebook_v1;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import models.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class StudentWindow extends SessionController {

    @Override
    public void onLogoutButtonClicked(MouseEvent event) {
        super.onLogoutButtonClicked(event);
    }

    @Override
    public void showInfo(String title, String content) {
        super.showInfo(title, content);
    }

    @Override
    public void showWarning(String title, String content) {
        super.showWarning(title, content);
    }

    @Override
    public void showError(String title, String content) {
        super.showError(title, content);
    }


    //-------------------- FXML for mainPane --------------------
    @FXML
    private TableView<StudentGrade> newGradesTable;
    @FXML
    private TableColumn<StudentGrade, String> gradeColumnM;
    @FXML
    private TableColumn<StudentGrade, String> subjectColumnM;
    @FXML
    private TableColumn<StudentGrade, String> typeColumnM;
    @FXML
    private TableColumn<StudentGrade, String> dateColumnM;
    @FXML
    private Label NotificationLabel;
    @FXML
    private Label avgLabel;
    @FXML
    private AnchorPane showAverageField;
    @FXML
    private AnchorPane hideAverageField;

    //-------------------- FXML for gradePane --------------------
    @FXML
    private TableView<StudentGrade> gradeTableG;
    @FXML
    private TableColumn<StudentGrade, String> gradeColumnG;
    @FXML
    private TableColumn<StudentGrade, String> subjectColumnG;
    @FXML
    private TableColumn<StudentGrade, String> avgColumnG;

    //-------------------- FXML for gradePane --------------------
    @FXML
    private TableView<NegativeNote> negativeNotesTable;
    @FXML
    private TableColumn<NegativeNote, String> textNN;
    @FXML
    private TableColumn<NegativeNote, String> pointsNN;
    @FXML
    private TableColumn<NegativeNote, String> dateNN;

    //-------------------- FXML for deadLinePane --------------------
    @FXML
    private TableView<Deadlines> deadLineTable;
    @FXML
    private TableColumn<Deadlines, String> subjectDL;
    @FXML
    private TableColumn<Deadlines, String> typeDL;
    @FXML
    private TableColumn<Deadlines, String> dateDL;
    @FXML
    private TableColumn<Deadlines, String> textDL;

    //-------------------- FXML for accountPane --------------------
    @FXML
    private TextField firstNameACC;
    @FXML
    private TextField lastNameACC;
    @FXML
    private TextField nicknameACC;
    @FXML
    private TextField classNameACC;

    //-------------------- FXML for notesPane --------------------
    @FXML
    private TableView<Note> notesTable;
    @FXML
    private TableColumn<Note, String> titleColumn;
    @FXML
    private TableColumn<Note, String> contentColumn;
    @FXML
    private TableColumn<Note, LocalDate> dateColumn;
    @FXML
    private TableColumn<Note, Void> deleteColumn;

    //-------------------- FXML for settingsPane --------------------
    @FXML
    private Label errorLabelACC;

    //-------------------- WelcomeLabels for StudentWindow --------------------
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label welcomeLabel2;
    @FXML
    private Label welcomeLabel3;
    @FXML
    private Label welcomeLabel4;
    @FXML
    private Label welcomeLabel5;
    @FXML
    private Label welcomeLabel6;
    @FXML
    private Label welcomeLabel7;

    private List<Label> welcomeLabels;


    //-------------------- FXML Anchors for StudentWindow --------------------
    @FXML
    private AnchorPane mainPane;
    @FXML
    private AnchorPane GradesPane;
    @FXML
    private AnchorPane deadLinesPane;
    @FXML
    private AnchorPane NegativeNotesPane;
    @FXML
    private AnchorPane AccountPane;
    @FXML
    private AnchorPane SettingsPane;
    @FXML
    private AnchorPane notesPane;

    //-----------------------------------------------------------------
    //-------------------- Methods for initializing the TeacherWindow --------------------
    //-----------------------------------------------------------------
    @FXML
    public void initialize() {
        // ------ Initialize for welcome labels ------
        Users loggedUser = Session.getLoggedUser();
        welcomeLabels = Arrays.asList(welcomeLabel, welcomeLabel2, welcomeLabel3, welcomeLabel4, welcomeLabel5, welcomeLabel6, welcomeLabel7);
        for (Label label : welcomeLabels) {
            label.setText("Witaj, " + loggedUser.getFirstName() + "!");
        }

        // ------ Initialize for mainPane ------
        updateNotificationLabel();
        subjectColumnM.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubject().getName()));
        gradeColumnM.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGrades()));
        dateColumnM.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        typeColumnM.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        loadNewGradesForStudent(loggedUser.getId());
        loadStudentAverage(loggedUser.getId());

        // ------ Initialize for gradePane ------
        subjectColumnG.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSubject().getName()));
        gradeColumnG.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getGrades()));
        avgColumnG.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getAverageGrade())));
        gradeTableG.setItems(loadStudentGrades());

        // ------ Initialize for negativeNotesPane ------
        textNN.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNoteText()));
        pointsNN.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getPoints())));
        dateNN.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getDateAssigned().toString())
        );
        negativeNotesTable.setItems(loadNegativeNotes());
        negativeNotesTable.setPlaceholder(new Label("Nie masz żadnych uwag. Wzorowy uczeń!"));
        // ------ Initialize for deadLinesPane ------
        subjectDL.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSubject().getName()));
        typeDL.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getType()));
        dateDL.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDeadlineDate().toString()));
        textDL.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescription()));
        // Load deadlines for the logged user
        deadLineTable.setItems(loadDeadlinesForLoggedUser());
        deadLineTable.setPlaceholder(new Label("Nie masz żadnych terminów. Wzorowy uczeń!"));
        // ------ Initialize for notesPane ------
        loadUserNotes();
        deleteColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Usuń");

            {
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setOnAction(event -> {
                    Note note = getTableView().getItems().get(getIndex());
                    deleteNote(note);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    deleteButton.setMaxWidth(Double.MAX_VALUE);
                    deleteButton.setMaxHeight(Double.MAX_VALUE);
                    setGraphic(deleteButton);
                }
            }
        });
        notesTable.setPlaceholder(new Label("Brak notatek. Dodaj nową notatkę!"));
        // ------ Initialize for accountPane ------
        setUserData();
        // ------ Initialize for settingsPane ------


    }

    //-----------------------------------------------------------------
    // -------------------- Methods for mainPane --------------------
    //-----------------------------------------------------------------
    public void loadNewGradesForStudent(int studentId) {
        ObservableList<StudentGrade> newGrades = FXCollections.observableArrayList();

        String sql = """
                    SELECT g.grade_value, g.date_assigned, g.weight, s.id AS subject_id, s.name AS subject_name
                    FROM grades g
                    JOIN subjects s ON g.subject_id = s.id
                    WHERE g.student_id = ? AND g.date_assigned >= CURDATE() - INTERVAL 7 DAY
                    ORDER BY g.date_assigned DESC
                """;
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String grade = rs.getString("grade_value");
                LocalDate date = rs.getDate("date_assigned").toLocalDate();
                int weight = rs.getInt("weight");
                int subjectId = rs.getInt("subject_id");

                String subjectName = rs.getString("subject_name");
                Subject subject = new Subject(subjectId, subjectName);
                String type = getTypeFromWeight(weight);
                StudentGrade sg = new StudentGrade(subject, grade, date, type);
                newGrades.add(sg);
            }

            newGradesTable.setItems(newGrades);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //Method to load the average grade for the student
    private void loadStudentAverage(int studentId) {
        String sql = "SELECT AVG(grade_value) AS avg FROM grades WHERE student_id = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double average = rs.getDouble("avg");

                String formattedAverage = String.format("%.2f", average);
                avgLabel.setText(formattedAverage);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //-----------------------------------------------------------------
    // -------------------- Methods for gradePane --------------------
    //-----------------------------------------------------------------
    private ObservableList<StudentGrade> loadStudentGrades() {
        ObservableList<StudentGrade> gradeList = FXCollections.observableArrayList();
        int studentId = Session.getLoggedUser().getId();
        String sql = """
                SELECT s.name AS subject_name, 
                       GROUP_CONCAT(g.grade_value ORDER BY g.date_assigned SEPARATOR ', ') AS grade_list,
                       ROUND(AVG(g.grade_value), 2) AS average
                FROM grades g
                JOIN subjects s ON g.subject_id = s.id
                WHERE g.student_id = ?
                GROUP BY s.id
                """;
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String subjectName = rs.getString("subject_name");
                String grades = rs.getString("grade_list");
                double avg = rs.getDouble("average");

                Subject subject = new Subject(subjectName);
                StudentGrade studentGrade = new StudentGrade(subject, grades, null, null);
                studentGrade.setAverageGrade(avg);

                gradeList.add(studentGrade);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return gradeList;
    }

    //-----------------------------------------------------------------
    // -------------------- Methods for negativeNotesPane --------------------
    //-----------------------------------------------------------------
    private ObservableList<NegativeNote> loadNegativeNotes() {
        ObservableList<NegativeNote> notes = FXCollections.observableArrayList();
        int studentId = Session.getLoggedUser().getId();
        String sql = """
                SELECT id, student_id, class_id, points, note_text, data_assigned 
                FROM negative_notes 
                WHERE student_id = ? 
                ORDER BY data_assigned DESC
                """;
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                NegativeNote note = new NegativeNote(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getInt("class_id"),
                        rs.getInt("points"),
                        rs.getString("note_text"),
                        rs.getDate("data_assigned").toLocalDate()
                );
                notes.add(note);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }

    //-----------------------------------------------------------------
    // -------------------- Methods for deadLinesPane --------------------
    //-----------------------------------------------------------------
    private ObservableList<Deadlines> loadDeadlinesForLoggedUser() {
        ObservableList<Deadlines> deadlines = FXCollections.observableArrayList();
        int classId = Session.getLoggedUser().getClassId();

        String sql = """
                SELECT d.id, d.class_id, d.type, d.deadline_date, d.description,
                       d.subject_id, d.date_assigned, s.name AS subject_name
                FROM deadlines d
                JOIN subjects s ON d.subject_id = s.id
                WHERE d.class_id = ?
                ORDER BY d.deadline_date ASC
                """;

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Subject subject = new Subject(rs.getString("subject_name")); // jak wcześniej

                Deadlines deadline = new Deadlines(
                        rs.getInt("id"),
                        rs.getInt("class_id"),
                        rs.getString("type"),
                        rs.getDate("deadline_date").toLocalDate(),
                        rs.getString("description"),
                        rs.getInt("subject_id"),
                        rs.getDate("date_assigned").toLocalDate()
                );
                deadline.setSubject(subject);
                deadlines.add(deadline);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return deadlines;
    }

    //-----------------------------------------------------------------
    //-------------------- Methods for notePane --------------------
    //-----------------------------------------------------------------

    @FXML
    private void refreshNotes() {
        loadUserNotes();
        notesTable.refresh();
    }

    private void loadUserNotes() {
        Users user = Session.getLoggedUser();
        int userId = user.getId();

        String sql = "SELECT id, user_id, title, content, created_at FROM notes WHERE user_id = ?";

        ObservableList<Note> notes = FXCollections.observableArrayList();

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                notes.add(new Note(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getDate("created_at").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt")); //

        notesTable.setItems(notes);
        notesTable.refresh();
    }

    private void deleteNote(Note note) {
        String sql = "DELETE FROM notes WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, note.getId());
            stmt.executeUpdate();

            notesTable.getItems().remove(note);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //-----------------------------------------------------------------
    //-------------------- Methods for accountPane --------------------
    //-----------------------------------------------------------------
    private void setUserData() {
        Users user = Session.getLoggedUser(); //

        if (user != null) {
            firstNameACC.setText(user.getFirstName());
            lastNameACC.setText(user.getLastName());
            nicknameACC.setText(user.getUsername());

            int classId = user.getClassId();
            classNameACC.setText(getClassName(classId)); //
        } else {
            System.out.println("Brak zalogowanego użytkownika w sesji.");
        }
    }

    //-----------------------------------------------------------------
    //-------------------- Methods for settingsPane --------------------
    //-----------------------------------------------------------------
    @FXML
    private void onChangeEmailButtonClicked() {
        errorLabelACC.setText("Opcja niedostępna.");
    }

    @FXML
    private void onChangePasswordButtonClicked() {
        ViewLoadingManager.loadResetPasswordView();

    }

    //-----------------------------------------------------------------
    //-------------------- Methods for right side buttons --------------------
    //-----------------------------------------------------------------
    PopUpWindow popUpWindow = new PopUpWindow();

    @Override
    public void addNewPersonalNoteButton() {
        super.addNewPersonalNoteButton();
        openPopUpWindow("notePane");
    }

    @FXML
    private void showAvg() {
        showAverageField.setVisible(false);
        hideAverageField.setVisible(true);
    }

    @FXML
    private void hideAvg() {
        showAverageField.setVisible(true);
        hideAverageField.setVisible(false);
    }


    //-----------------------------------------------------------------
    // -------------------- Methods for loading sites --------------------
    //-----------------------------------------------------------------
    @FXML
    public void switchToMainPane() {
        hideAllPanes();
        mainPane.setVisible(true);
    }

    @FXML
    public void switchToGradesPane() {
        hideAllPanes();
        GradesPane.setVisible(true);
    }

    @FXML
    public void switchToNegativeNotesPane() {
        hideAllPanes();
        NegativeNotesPane.setVisible(true);
    }

    @FXML
    public void switchToAccountPane() {
        hideAllPanes();
        AccountPane.setVisible(true);
    }

    @FXML
    public void switchToSettingsPane() {
        hideAllPanes();
        SettingsPane.setVisible(true);
    }

    @FXML
    public void switchToNotesPane() {
        hideAllPanes();
        notesPane.setVisible(true);
    }

    @FXML
    public void switchToDeadLinesPane() {
        hideAllPanes();
        deadLinesPane.setVisible(true);
    }

    //-----------------------------------------------------------------
    // -------------------- auxiliary methods --------------------
    //-----------------------------------------------------------------
    private String getTypeFromWeight(int weight) {
        return switch (weight) {
            case 3 -> "Sprawdzian";
            case 2 -> "Kartkówka";
            case 1 -> "Praca domowa";
            default -> "Inne";
        };
    }

    //Method to show notification if there are new grades or deadlines
    private int countNewGrades(int studentId) {
        String sql = "SELECT COUNT(*) FROM grades WHERE student_id = ? AND date_assigned >= CURDATE() - INTERVAL 7 DAY";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int countNewDeadlines(int classId) {
        String sql = "SELECT COUNT(*) FROM deadlines WHERE class_id = ? AND date_assigned >= CURDATE() - INTERVAL 7 DAY";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, classId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    private void updateNotificationLabel() {
        Users loggedUser = Session.getLoggedUser();
        int classId = getLoggedUserClassId();

        int grades = countNewGrades(loggedUser.getId());
        int deadLines = countNewDeadlines(classId);


        String gradesWord = (grades == 1)
                ? "nową ocenę"
                : (grades % 10 >= 2 && grades % 10 <= 4 && (grades % 100 < 10 || grades % 100 >= 20))
                ? "nowe oceny"
                : "nowych ocen";

        String deadLinesWord = (deadLines == 1)
                ? "nowy termin"
                : (deadLines % 10 >= 2 && deadLines % 10 <= 4 && (deadLines % 100 < 10 || deadLines % 100 >= 20))
                ? "nowe terminy"
                : "nowych terminów";

        NotificationLabel.setText("Masz " + grades + " " + gradesWord + " oraz " + deadLines + " " + deadLinesWord + ".");

    }

    private int getLoggedUserClassId() {
        Users loggedUser = Session.getLoggedUser();
        Classes userClass = loggedUser.getUserClass();
        return (userClass != null) ? userClass.getId() : -1;
    }

    private void hideAllPanes() {
        mainPane.setVisible(false);
        GradesPane.setVisible(false);
        NegativeNotesPane.setVisible(false);
        AccountPane.setVisible(false);
        SettingsPane.setVisible(false);
        deadLinesPane.setVisible(false);
        notesPane.setVisible(false);
    }

    private String getClassName(int classId) {
        return switch (classId) {
            case 1 -> "1TIA";
            case 2 -> "1TIB";
            default -> "Brak przypisanej klasa";
        };
    }

}
