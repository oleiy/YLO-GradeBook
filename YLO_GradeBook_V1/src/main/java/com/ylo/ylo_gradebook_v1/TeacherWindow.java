package com.ylo.ylo_gradebook_v1;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import models.Note;
import models.StudentGrade;
import models.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TeacherWindow extends SessionController {
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
    private ComboBox<String> classComboBox;
    @FXML
    private TableView<Users> studentsTable;
    @FXML
    private TableColumn<Users, String> firstNameColumn;
    @FXML
    private TableColumn<Users, String> lastNameColumn;
    @FXML
    private TableColumn<Users, Integer> journalNumberColumn;

    //-------------------- FXML for GradesPane --------------------
    @FXML
    private ComboBox<String> chooseClassG;
    @FXML
    private ComboBox<String> chooseSubjectG;
    @FXML
    private TableView<StudentGrade> studentsGradesTable;
    @FXML
    private TableColumn<StudentGrade, Integer> numberColumnG;
    @FXML
    private TableColumn<StudentGrade, String> nameColumnG;
    @FXML
    private TableColumn<StudentGrade, String> surnameColumnG;
    @FXML
    private TableColumn<StudentGrade, String> gradesColumnG;
    @FXML
    private TableColumn<StudentGrade, Double> avgColumnG;

    //-------------------- FXML for NotesPane --------------------
    @FXML
    private TableView<Note> notesTable;
    @FXML
    private TableColumn<Note, String> titleColumn;
    @FXML
    private TableColumn<Note, String> contentColumn;
    @FXML
    private TableColumn<Note, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<Note, Void> deleteColumn;

    //-------------------- FXML for AccountPane --------------------
    @FXML
    private TextField firstNameACC;
    @FXML
    private TextField lastNameACC;
    @FXML
    private TextField nicknameACC;
    @FXML
    private TextField classNameACC;

    //-------------------- FXML for settingsPane --------------------
    @FXML
    private Label errorLabelACC;

    //-------------------- WelcomeLabels for TeacherWindow --------------------
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
    private List<Label> welcomeLabels;

    private ObservableList<Users> studentsList = FXCollections.observableArrayList();

    //-------------------- FXML Anchors for TeacherWindow --------------------
    @FXML
    private AnchorPane mainPane;
    @FXML
    private AnchorPane GradesPane;
    @FXML
    private AnchorPane NotesPane;
    @FXML
    private AnchorPane AccountPane;
    @FXML
    private AnchorPane SettingsPane;

    //-----------------------------------------------------------------
    //-------------------- Methods for initializing the TeacherWindow --------------------
    //-----------------------------------------------------------------
    @FXML
    public void initialize() {
        // Initialize for welcome labels
        Users loggedUser = Session.getLoggedUser();
        welcomeLabels = Arrays.asList(welcomeLabel, welcomeLabel2, welcomeLabel3, welcomeLabel4, welcomeLabel5);
        for (Label label : welcomeLabels) {
            label.setText("Witaj, " + loggedUser.getFirstName() + "!");
        }

        // ------ Initialize the mainPane ------
        classComboBox.setItems(FXCollections.observableArrayList("1TIA", "1TIB"));
        classComboBox.setOnAction(event -> loadStudentsTable(classComboBox.getValue()));
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        journalNumberColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(studentsList.indexOf(cellData.getValue()) + 1).asObject());
        loadStudentsTable("1TIA");

        // ------ Initialize for GradesPane ------
        numberColumnG.setCellValueFactory(new PropertyValueFactory<>("journalNumber"));
        nameColumnG.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        surnameColumnG.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        gradesColumnG.setCellValueFactory(new PropertyValueFactory<>("grades"));
        avgColumnG.setCellValueFactory(new PropertyValueFactory<>("averageGrade"));

        chooseClassG.setItems(loadClassesFromDatabase());
        chooseSubjectG.setItems(loadSubjectsFromDatabase());

        chooseClassG.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> onClassOrSubjectChanged());
        chooseSubjectG.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> onClassOrSubjectChanged());
        studentsGradesTable.setPlaceholder(new Label("Wybierz klasę i przedmiot, aby zobaczyć oceny"));

        // ------ Initialize the NotesPane ------
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


        // ------ Initialize the AccountPane ------
        setUserData();

    }

    //-----------------------------------------------------------------
    //-------------------- Methods for loadingstudents table --------------------
    //-----------------------------------------------------------------

    private void loadGrades(String className, String subjectName) {
        String sql = "SELECT u.id, u.first_name, u.last_name, " +
                "IFNULL(GROUP_CONCAT(g.grade_value ORDER BY g.date_assigned SEPARATOR ', '), 'Brak ocen') AS grades " +
                "FROM users u " +
                "LEFT JOIN grades g ON u.id = g.student_id AND g.subject_id = ? " +
                "WHERE u.class_id = ? " +
                "GROUP BY u.id;";

        ObservableList<StudentGrade> gradesList = FXCollections.observableArrayList();
        int journalNumber = 1;

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int classId = getClassIdByName(className);
            int subjectId = getSubjectIdByName(subjectName);

            stmt.setInt(1, subjectId);
            stmt.setInt(2, classId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String gradesStr = (rs.getObject("grades") != null) ? rs.getString("grades").trim() : "Brak ocen";
                double avgGrade = gradesStr.equals("Brak ocen") ? 0 : calculateAverage(gradesStr);

                gradesList.add(new StudentGrade(
                        journalNumber++,
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        gradesStr,
                        avgGrade
                ));
            }

            studentsGradesTable.setItems(gradesList);
            studentsGradesTable.refresh();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClassOrSubjectChanged() {
        String selectedClass = chooseClassG.getSelectionModel().getSelectedItem();
        String selectedSubject = chooseSubjectG.getSelectionModel().getSelectedItem();

        if (selectedClass != null && selectedSubject != null) {
            loadGrades(selectedClass, selectedSubject);
        }
    }

    private int getSubjectIdByName(String subjectName) {
        String sql = "SELECT id FROM subjects WHERE name = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, subjectName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania ID przedmiotu: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
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
    //-------------------- Methods for loadingstudents table --------------------
    //-----------------------------------------------------------------
    @FXML
    private void loadStudentsTable(String className) {
        studentsList.clear();

        int classId = switch (className) {
            case "1TIA" -> 1;
            case "1TIB" -> 2;
            default -> -1;
        };

        String sql = "SELECT * FROM users WHERE role = 'STUDENT' AND class_id = ? ORDER BY last_name ASC";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            ResultSet rs = stmt.executeQuery();

            int journalNumber = 1;
            while (rs.next()) {
                Users user = new Users(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("role"), rs.getInt("class_id"));

                studentsList.add(user);
                journalNumber++;
            }

            studentsTable.setItems(studentsList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void addNewNegativeNoteButton() {
        openPopUpWindow("neggativeNotePane");
    }

    public void addNewDeadLineButton() {
        openPopUpWindow("deadLinePane");
    }

    public void addNewGradeButton() {
        openPopUpWindow("gradePane");
    }

    //-----------------------------------------------------------------
    // -------------------- Methods for loading sites --------------------
    //-----------------------------------------------------------------

    private void hideAllPanes() {
        mainPane.setVisible(false);
        GradesPane.setVisible(false);
        NotesPane.setVisible(false);
        AccountPane.setVisible(false);
        SettingsPane.setVisible(false);
    }

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
    public void switchToNotesPane() {
        hideAllPanes();
        NotesPane.setVisible(true);
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


    //-----------------------------------------------------------------
    // -------------------- Auxiliary methods  --------------------
    //-----------------------------------------------------------------
    private String getClassName(int classId) {
        return switch (classId) {
            case 1 -> "1TIA";
            case 2 -> "1TIB";
            default -> "Brak przypisanej klasa";
        };
    }

    private int getClassIdByName(String className) {
        return switch (className) {
            case "1TIA" -> 1;
            case "1TIB" -> 2;
            default -> -1; //
        };
    }


    private ObservableList<String> loadSubjectsFromDatabase() {
        ObservableList<String> subjectsList = FXCollections.observableArrayList();
        String sql = "SELECT name FROM subjects ORDER BY name ASC";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                subjectsList.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subjectsList;
    }

    private ObservableList<String> loadClassesFromDatabase() {
        ObservableList<String> classesList = FXCollections.observableArrayList();
        String sql = "SELECT name FROM classes ORDER BY name ASC";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                classesList.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classesList;
    }


    private double calculateAverage(String gradesStr) {
        if (gradesStr.equals("Brak ocen")) {
            return 0.0;
        }

        String[] gradesArray = gradesStr.split(", ");
        double sum = 0.0;
        int count = 0;

        for (String grade : gradesArray) {
            try {
                sum += Integer.parseInt(grade.trim());
                count++;
            } catch (NumberFormatException e) {
                System.err.println("Błąd konwersji oceny: " + grade);
            }
        }

        double average = (count > 0) ? (sum / count) : 0.0;
        return Math.round(average * 100.0) / 100.0;
    }


}
