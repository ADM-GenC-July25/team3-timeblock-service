package com.scheduleplanner.timeblock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TimeBlockDTO {
    
    private Integer id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Day is required")
    private String day;
    
    @NotBlank(message = "Start time is required")
    private String startTime;
    
    @NotBlank(message = "End time is required")
    private String endTime;
    
    @NotBlank(message = "Type is required")
    private String type;
    
    private String description;
    
    private String color;
    
    @NotNull(message = "Student ID is required")
    private Integer studentId;
    
    private Integer weeks;
    
    // Constructors
    public TimeBlockDTO() {}
    
    public TimeBlockDTO(Integer id, String title, String day, String startTime, String endTime, 
                       String type, String description, String color, Integer studentId, Integer weeks) {
        this.id = id;
        this.title = title;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.description = description;
        this.color = color;
        this.studentId = studentId;
        this.weeks = weeks;
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDay() {
        return day;
    }
    
    public void setDay(String day) {
        this.day = day;
    }
    
    public String getStartTime() {
        return startTime;
    }
    
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
    public String getEndTime() {
        return endTime;
    }
    
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public Integer getStudentId() {
        return studentId;
    }
    
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
    
    public Integer getWeeks() {
        return weeks;
    }
    
    public void setWeeks(Integer weeks) {
        this.weeks = weeks;
    }
} 