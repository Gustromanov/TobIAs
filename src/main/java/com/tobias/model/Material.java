package com.tobias.model;

import java.time.LocalDateTime;

public class Material {

    private int id;
    private int disciplineId;
    private String title;
    private String content;
    private String filePath;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private LocalDateTime uploadedAt;

    public Material() {
    }

    public Material(int id, int disciplineId, String title, String content, String filePath,
            String originalFileName, String contentType, Long fileSize, LocalDateTime uploadedAt) {
        this.id = id;
        this.disciplineId = disciplineId;
        this.title = title;
        this.content = content;
        this.filePath = filePath;
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.uploadedAt = uploadedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(int disciplineId) {
        this.disciplineId = disciplineId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public boolean hasFile() {
        return filePath != null && !filePath.isBlank();
    }
}
