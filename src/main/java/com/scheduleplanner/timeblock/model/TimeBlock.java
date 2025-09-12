package com.scheduleplanner.timeblock.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "time_blocks")
public class TimeBlock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "time_block_id")
    private Integer id;
    
    @NotBlank(message = "Title is required")
    @Column(nullable = false, length = 255)
    private String title;
    
    @NotBlank(message = "Day is required")
    @Column(name = "day", nullable = false, length = 20)
    private String day;
    
    @NotBlank(message = "Start time is required")
    @Column(name = "start_time", nullable = false, length = 20)
    private String startTime;
    
    @NotBlank(message = "End time is required")
    @Column(name = "end_time", nullable = false, length = 20)
    private String endTime;
    
    @NotBlank(message = "Type is required")
    @Column(name = "type", nullable = false, length = 50)
    private String type;
    
    @Column(length = 500)
    private String description;
    
    @Column(length = 20)
    private String color;
    
    @NotNull(message = "Student ID is required")
    @Column(name = "student_id", nullable = false)
    private Integer studentId;
    
    @Column
    private Integer weeks;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public TimeBlock() {}
    
    public TimeBlock(String title, String day, String startTime, String endTime, 
                    String type, String description, String color, Integer studentId, Integer weeks) {
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
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "TimeBlock{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", day='" + day + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", type='" + type + '\'' +
                ", studentId=" + studentId +
                ", weeks=" + weeks +
                '}';
    }
} 