package models;

public class Classes {
    private int id;
    private String name;

    public Classes(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Classes(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

