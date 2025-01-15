package com.example.budgetmanager.dto;

public class FileModel {
    private String fileName;
    private String fileContent;

    public FileModel(String fileName, String fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

    // Getters și Setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }
}
