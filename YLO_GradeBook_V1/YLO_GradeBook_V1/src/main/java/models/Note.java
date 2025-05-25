package models;
import java.time.LocalDateTime;

public class Note {
    private int id;
    private int userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public Note(int id, int userId, String title, String content, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }

}
