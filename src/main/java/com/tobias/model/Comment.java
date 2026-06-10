package com.tobias.model;

import java.time.LocalDateTime;

public class Comment {
    private int id;
    private int warningId;
    private int userId;
    private String content;
    private LocalDateTime dateComment;
    
    private String authorName;
    private String authorPhoto;

    public Comment() {}

    public int getId() { 
        return id;
    }

    public void setId(int id) { 
        this.id = id; 
    }

    public int getWarningId() {
        return warningId;
    }

    public void setWarningId(int warningId) {
        this.warningId = warningId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDateComment() {
        return dateComment;
    }

    public void setDateComment(LocalDateTime dateComment) {
        this.dateComment = dateComment;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorPhoto() {
        return authorPhoto;
    }

    public void setAuthorPhoto(String authorPhoto) {
        this.authorPhoto = authorPhoto;
    }

    
}
