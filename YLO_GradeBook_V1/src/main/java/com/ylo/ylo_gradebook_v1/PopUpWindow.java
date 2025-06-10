package com.ylo.ylo_gradebook_v1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import models.Users;

import java.sql.*;
import java.time.LocalDate;

public class PopUpWindow {

    // -------------------- FXML Components --------------------

    // -------------------- FXMXL Components for gradePane --------------------
    @FXML
    private ComboBox<String> classComboBox;
    @FXML
    private ComboBox<String> studentComboBox;
    @FXML
    private ComboBox<String> subjectComboBox;
    @FXML
    private ComboBox<String> gradeComboBox;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private Label errorLabelG;


    // -------------------- FXMXL Components for negativeNotePane --------------------
    @FXML
    private ComboBox<String> chooseClassNN;
    @FXML
    private ComboBox<String> chooseStudentNN;
    @FXML
    private TextField pointsNN;
    @FXML
    private TextArea textAreaNN;
    @FXML
    private Label errorLabelNN;

    // -------------------- FXMXL Components for deadLinePane --------------------
    @FXML
    private ComboBox<String> chooseClassDL;
    @FXML
    private ComboBox<String> chooseTypeDL;
    @FXML
    private ComboBox<String> chooseSubjectDL;
    @FXML
    private DatePicker dateDL;
    @FXML
    private TextArea descrpDL;
    @FXML
    private Label errorLabelDL;

    // -------------------- FXMXL Components for notePane --------------------
    @FXML
    private TextField titleN;
    @FXML
    private TextArea textAreaN;
    @FXML
    private Label errorLabelN;

    // -------------------- AnchorPane for changing Panes --------------------
    @FXML
    public AnchorPane gradePane;
    @FXML
    public AnchorPane negativeNotePane;
    @FXML
    public AnchorPane deadLinePane;
    @FXML
    public AnchorPane notePane;

    public ObservableList<String> getSubjectsList() {
        return FXCollections.observableArrayList(subjectsList);
    }


    private ObservableList<String> studentsList = FXCollections.observableArrayList();
    private ObservableList<String> subjectsList = FXCollections.observableArrayList();
    private ObservableList<String> gradesList = FXCollections.observableArrayList(
            "1.0 - niedostateczny",
            "2.0 - dopuszczalny",
            "2.0 - dopuszczalny +",
            "3.0 - dostateczny",
            "3.5 - dostateczny +",
            "4.0 - dobry",
            "4.5 - dobry +",
            "5.0 - bardzo dobry",
            "5.5 - bardzo dobry +",
            "6.0 - celujący"
    );
    private ObservableList<String> typeList = FXCollections.observableArrayList(
            "Sprawdzian",
            "Kartkówka",
            "Odpowiedź ustna",
            "Praca domowa",
            "Inne"
    );

    // -------------------- Method for initialize pane --------------------
    @FXML
    public void initialize() {
        // Initialize for gradePane
        typeComboBox.setItems(typeList);
        loadSubjectsToComboBox();
        gradeComboBox.setItems(gradesList);
        classComboBox.setItems(FXCollections.observableArrayList("1TIA", "1TIB"));
        classComboBox.setOnAction(event -> {
            String selectedClass = classComboBox.getValue();
            loadStudentsToComboBox(selectedClass);
        });

        // Initialize for negativeNotePane
        chooseClassNN.setItems(FXCollections.observableArrayList("1TIA", "1TIB"));
        chooseClassNN.setOnAction(event -> {
            String selectedClass = chooseClassNN.getValue();
            loadStudentsToComboBox(selectedClass);
            chooseStudentNN.setItems(FXCollections.observableArrayList(studentsList));
        });

        // Initialize for deadLinePane
        chooseClassDL.setItems(FXCollections.observableArrayList("1TIA", "1TIB"));
        chooseTypeDL.setItems(FXCollections.observableArrayList("Wydarzenie", "Sprawdzian", "Kartkówka", "Zadanie"));
        chooseSubjectDL.setItems(FXCollections.observableArrayList(subjectsList));
    }

    public void showPane(String paneId) {
        gradePane.setVisible(false);
        negativeNotePane.setVisible(false);
        deadLinePane.setVisible(false);
        notePane.setVisible(false);

        switch (paneId) {
            case "gradePane" -> gradePane.setVisible(true);
            case "neggativeNotePane" -> negativeNotePane.setVisible(true);
            case "deadLinePane" -> deadLinePane.setVisible(true);
            case "notePane" -> notePane.setVisible(true);
        }
    }

    // -------------------- Methods to add new grade --------------------
    private void addGradeToDatabase(int studentId, int subjectId, double gradeValue, int weight, String dateAssigned) {
        String sql = "INSERT INTO grades (student_id, subject_id, grade_value, weight, date_assigned) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, subjectId);
            stmt.setDouble(3, gradeValue);
            stmt.setInt(4, weight); // Dodajemy wagę do bazy
            stmt.setDate(5, Date.valueOf(dateAssigned));

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                errorLabelG.setText("Ocena została dodana pomyślnie!");
                errorLabelG.setStyle("-fx-text-fill: green;");
            } else {
                errorLabelG.setText("Błąd podczas dodawania oceny.");
                errorLabelG.setStyle("-fx-text-fill: red;");
            }
        } catch (SQLException e) {
            errorLabelG.setText("Błąd: " + e.getMessage());
            errorLabelG.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }


    @FXML
    private void onAddGradeButtonClicked() {
        String selectedClass = classComboBox.getValue();
        String selectedStudent = studentComboBox.getValue();
        String selectedSubject = subjectComboBox.getValue();
        String selectedGrade = gradeComboBox.getValue();
        String selectedType = typeComboBox.getValue();
        String dateAssigned = LocalDate.now().toString(); // Automatyczna data

        if (selectedClass == null || selectedStudent == null || selectedSubject == null ||
                selectedGrade == null || selectedType == null) {
            errorLabelG.setText("Wypełnij wszystkie pola!");
            errorLabelG.setStyle("-fx-text-fill: red;");
            return;
        }

        int studentId = getStudentId(selectedStudent);
        int subjectId = getSubjectId(selectedSubject);
        double gradeValue = Double.parseDouble(selectedGrade.split(" - ")[0]); // Pobranie wartości oceny
        int weight = getWeightFromType(selectedType); // Pobranie wagi oceny

        addGradeToDatabase(studentId, subjectId, gradeValue, weight, dateAssigned);
    }


    // -------------------- Methods to add new negative note --------------------

    @FXML
    private void onAddNegativeNoteClicked() {
        String selectedClass = chooseClassNN.getValue();
        String selectedStudent = chooseStudentNN.getValue();
        String pointsText = pointsNN.getText();
        String noteText = textAreaNN.getText();

        if (selectedClass == null || selectedStudent == null || pointsText.isEmpty() || noteText.isEmpty()) {
            errorLabelNN.setText("Wypełnij wszystkie pola!");
            errorLabelNN.setStyle("-fx-text-fill: red;");
            return;
        }

        int studentId = getStudentId(selectedStudent);
        int classId = getClassId(selectedClass);

        int points;
        try {
            points = Integer.parseInt(pointsText);
        } catch (NumberFormatException e) {
            errorLabelNN.setText("Punkty muszą być liczbą!");
            errorLabelNN.setStyle("-fx-text-fill: red;");
            return;
        }

        addNegativeNoteToDatabase(studentId, classId, points, noteText);
    }

    private void addNegativeNoteToDatabase(int studentId, int classId, int points, String noteText) {
        if (noteText.length() > 60) {
            errorLabelNN.setText("Treść uwagi nie może przekraczać 60 znaków.");
            errorLabelNN.setStyle("-fx-text-fill: red;");
            return;
        }

        String sql = "INSERT INTO negative_notes (student_id, class_id, points, note_text, data_assigned) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, classId);
            stmt.setInt(3, points);
            stmt.setString(4, noteText);
            stmt.setDate(5, java.sql.Date.valueOf(LocalDate.now()));

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                errorLabelNN.setText("Uwaga została dodana pomyślnie!");
                errorLabelNN.setStyle("-fx-text-fill: green;");
            } else {
                errorLabelNN.setText("Błąd podczas dodawania uwagi.");
                errorLabelNN.setStyle("-fx-text-fill: red;");
            }
        } catch (SQLException e) {
            errorLabelNN.setText("Błąd: " + e.getMessage());
            errorLabelNN.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }


    // -------------------- Methods to add new dead line --------------------

    private void addDeadlineToDatabase(int classId, int subjectId, String type, LocalDate date, String description) {
        String sql = "INSERT INTO deadlines (class_id, subject_id, type, deadline_date, description, date_assigned) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, classId);
            stmt.setInt(2, subjectId);
            stmt.setString(3, type);
            stmt.setDate(4, Date.valueOf(date)); // data wykonania
            stmt.setString(5, description);
            stmt.setDate(6, Date.valueOf(LocalDate.now())); // ⬅️ data dodania (dzisiaj)

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                errorLabelDL.setText("Termin został dodany pomyślnie!");
                errorLabelDL.setStyle("-fx-text-fill: green;");
            } else {
                errorLabelDL.setText("Błąd podczas dodawania terminu.");
                errorLabelDL.setStyle("-fx-text-fill: red;");
            }
        } catch (SQLException e) {
            errorLabelDL.setText("Błąd: " + e.getMessage());
            errorLabelDL.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }


    @FXML
    private void onAddDeadlineButtonClicked() {
        String selectedClass = chooseClassDL.getValue();
        String selectedSubject = chooseSubjectDL.getValue();
        String selectedType = chooseTypeDL.getValue();
        LocalDate selectedDate = dateDL.getValue();
        String description = descrpDL.getText();

        if (selectedClass == null || selectedSubject == null || selectedType == null || selectedDate == null || description.isEmpty()) {
            errorLabelDL.setText("Wypełnij wszystkie pola!");
            errorLabelDL.setStyle("-fx-text-fill: red;");
            return;
        }

        int classId = getClassId(selectedClass);
        int subjectId = getSubjectId(selectedSubject);

        addDeadlineToDatabase(classId, subjectId, selectedType, selectedDate, description);
    }


// -------------------- Methods to add new note --------------------

    private void addNoteToDatabase(int userId, String title, String content) {
        String sql = "INSERT INTO notes (user_id, title, content, created_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            LocalDate today = LocalDate.now(); //

            stmt.setInt(1, userId);
            stmt.setString(2, title);
            stmt.setString(3, content);
            stmt.setDate(4, Date.valueOf(today));

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                errorLabelN.setText("Notatka została dodana pomyślnie!");
                errorLabelN.setStyle("-fx-text-fill: green;");
            } else {
                errorLabelN.setText("Błąd podczas dodawania notatki.");
                errorLabelN.setStyle("-fx-text-fill: red;");
            }
        } catch (SQLException e) {
            errorLabelN.setText("Błąd: " + e.getMessage());
            errorLabelN.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }


    @FXML
    private void onAddNoteButtonClicked() {
        String title = titleN.getText();
        String content = textAreaN.getText();

        if (title.isEmpty() || content.isEmpty()) {
            errorLabelN.setText("Wypełnij wszystkie pola!");
            errorLabelN.setStyle("-fx-text-fill: red;");
            return;
        }

        Users user = Session.getLoggedUser();
        if (user == null) {
            errorLabelN.setText("Nie jesteś zalogowany!");
            errorLabelN.setStyle("-fx-text-fill: red;");
            return;
        }

        int userId = user.getId();
        addNoteToDatabase(userId, title, content);
    }


    // -------------------- Auxiliary methods  --------------------

    private int getWeightFromType(String selectedType) {
        return switch (selectedType) {
            case "Sprawdzian" -> 3;
            case "Kartkówka" -> 2;
            case "Odpowiedź ustna" -> 2;
            case "Praca domowa" -> 1;
            case "Inne" -> 1;
            default -> 1;
        };
    }

    private int getStudentId(String studentName) {
        String sql = "SELECT id FROM users WHERE CONCAT(first_name, ' ', last_name) = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int getSubjectId(String subjectName) {
        String sql = "SELECT id FROM subjects WHERE name = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, subjectName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int getClassId(String className) {
        return switch (className) {
            case "1TIA" -> 1;
            case "1TIB" -> 2;
            default -> -1;
        };
    }

    // -------------------- Method for load Students to studentComboBox --------------------
    private void loadStudentsToComboBox(String className) {
        studentsList.clear();

        int classId = switch (className) {
            case "1TIA" -> 1;
            case "1TIB" -> 2;
            default -> -1;
        };

        String sql = "SELECT first_name, last_name FROM users WHERE role = 'STUDENT' AND class_id = ? ORDER BY last_name ASC";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String studentName = rs.getString("first_name") + " " + rs.getString("last_name");
                studentsList.add(studentName);
            }

            studentComboBox.setItems(FXCollections.observableArrayList(studentsList));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // -------------------- Method for load Subjects to subjectComboBox --------------------
    public void loadSubjectsToComboBox() {
        subjectsList.clear();

        String sql = "SELECT name FROM subjects ORDER BY name ASC";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                subjectsList.add(rs.getString("name"));
            }

            subjectComboBox.setItems(FXCollections.observableArrayList(subjectsList));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
