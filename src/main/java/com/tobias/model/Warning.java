package com.tobias.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Warning {
    private int id;
    private int disciplineId;
    private int userId;
    private String content;
    private LocalDateTime publishedAt;
    
    // Dados extras para o ecrã (Frontend)
    private String authorName;
    private String authorPhoto;
    private List<Comment> comments = new ArrayList<>();

    public Warning() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getDisciplineId() { return disciplineId; }
    public void setDisciplineId(int disciplineId) { this.disciplineId = disciplineId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getAuthorPhoto() { return authorPhoto; }
    public void setAuthorPhoto(String authorPhoto) { this.authorPhoto = authorPhoto; }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
}
