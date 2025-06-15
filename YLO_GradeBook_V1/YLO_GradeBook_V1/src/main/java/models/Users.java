package models;

public class Users {
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private int classId;
    private Classes userClass;

    public Users(int id, String username, String password, String firstName, String lastName, String role, int classId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.classId = classId;
    }

    public Users(int id, String username, String password, String firstName, String lastName, String role, Classes userClass) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.classId = (userClass != null) ? userClass.getId() : -1;
        this.userClass = userClass;
    }


    public Classes getUserClass() {
        return userClass;
    }

    public void setUserClass(Classes userClass) {
        this.userClass = userClass;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRole() {
        return role;
    }

    public int getClassId() {
        return classId;
    }
}