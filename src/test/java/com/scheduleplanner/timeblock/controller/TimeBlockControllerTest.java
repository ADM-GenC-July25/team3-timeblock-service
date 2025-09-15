package com.scheduleplanner.timeblock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduleplanner.timeblock.dto.TimeBlockDTO;
import com.scheduleplanner.timeblock.service.TimeBlockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TimeBlockController.class)
@ActiveProfiles("test")
class TimeBlockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimeBlockService timeBlockService;

    @Autowired
    private ObjectMapper objectMapper;

    private TimeBlockDTO sampleTimeBlock;
    private List<TimeBlockDTO> sampleTimeBlocks;

    @BeforeEach
    void setUp() {
        sampleTimeBlock = new TimeBlockDTO(
            1,
            "Math Class",
            "Monday",
            "09:00",
            "10:30",
            "Academic",
            "Advanced Mathematics",
            "#FF5722",
            123,
            15
        );

        TimeBlockDTO timeBlock2 = new TimeBlockDTO(
            2,
            "Study Time",
            "Monday",
            "14:00",
            "16:00",
            "Study",
            "Homework and review",
            "#2196F3",
            123,
            15
        );

        sampleTimeBlocks = Arrays.asList(sampleTimeBlock, timeBlock2);
    }

    @Test
    void testGetTimeBlocksByStudentId_Success() throws Exception {
        // Arrange
        Integer studentId = 123;
        when(timeBlockService.getTimeBlocksByStudentId(studentId)).thenReturn(sampleTimeBlocks);

        // Act & Assert
        mockMvc.perform(get("/api/timeblocks/student/{studentId}", studentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Math Class"))
                .andExpect(jsonPath("$[0].studentId").value(123))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Study Time"));

        verify(timeBlockService, times(1)).getTimeBlocksByStudentId(studentId);
    }

    @Test
    void testGetTimeBlocksByStudentId_InternalServerError() throws Exception {
        // Arrange
        Integer studentId = 123;
        when(timeBlockService.getTimeBlocksByStudentId(studentId))
                .thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        mockMvc.perform(get("/api/timeblocks/student/{studentId}", studentId))
                .andExpect(status().isInternalServerError());

        verify(timeBlockService, times(1)).getTimeBlocksByStudentId(studentId);
    }

    @Test
    void testGetTimeBlocksByStudentIdAndDay_Success() throws Exception {
        // Arrange
        Integer studentId = 123;
        String day = "Monday";
        when(timeBlockService.getTimeBlocksByStudentIdAndDay(studentId, day)).thenReturn(sampleTimeBlocks);

        // Act & Assert
        mockMvc.perform(get("/api/timeblocks/student/{studentId}/day/{day}", studentId, day))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].day").value("Monday"))
                .andExpect(jsonPath("$[1].day").value("Monday"));

        verify(timeBlockService, times(1)).getTimeBlocksByStudentIdAndDay(studentId, day);
    }

    @Test
    void testGetTimeBlockById_Success() throws Exception {
        // Arrange
        Integer timeBlockId = 1;
        when(timeBlockService.getTimeBlockById(timeBlockId)).thenReturn(Optional.of(sampleTimeBlock));

        // Act & Assert
        mockMvc.perform(get("/api/timeblocks/{id}", timeBlockId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Math Class"))
                .andExpect(jsonPath("$.day").value("Monday"));

        verify(timeBlockService, times(1)).getTimeBlockById(timeBlockId);
    }

    @Test
    void testGetTimeBlockById_NotFound() throws Exception {
        // Arrange
        Integer timeBlockId = 999;
        when(timeBlockService.getTimeBlockById(timeBlockId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/timeblocks/{id}", timeBlockId))
                .andExpect(status().isNotFound());

        verify(timeBlockService, times(1)).getTimeBlockById(timeBlockId);
    }

    @Test
    void testCreateTimeBlock_Success() throws Exception {
        // Arrange
        TimeBlockDTO newTimeBlock = new TimeBlockDTO(
            null, // ID should be null for creation
            "New Class",
            "Tuesday",
            "10:00",
            "11:30",
            "Academic",
            "Science class",
            "#4CAF50",
            123,
            15
        );

        TimeBlockDTO createdTimeBlock = new TimeBlockDTO(
            3,
            "New Class",
            "Tuesday",
            "10:00",
            "11:30",
            "Academic",
            "Science class",
            "#4CAF50",
            123,
            15
        );

        when(timeBlockService.createTimeBlock(any(TimeBlockDTO.class))).thenReturn(createdTimeBlock);

        // Act & Assert
        mockMvc.perform(post("/api/timeblocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTimeBlock)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("New Class"))
                .andExpect(jsonPath("$.day").value("Tuesday"));

        verify(timeBlockService, times(1)).createTimeBlock(any(TimeBlockDTO.class));
    }

    @Test
    void testCreateTimeBlock_ValidationError() throws Exception {
        // Arrange - TimeBlock with missing required fields
        TimeBlockDTO invalidTimeBlock = new TimeBlockDTO();
        invalidTimeBlock.setTitle(""); // Empty title should fail validation
        invalidTimeBlock.setDay("");
        invalidTimeBlock.setStartTime("");
        invalidTimeBlock.setEndTime("");
        invalidTimeBlock.setType("");

        // Act & Assert
        mockMvc.perform(post("/api/timeblocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTimeBlock)))
                .andExpect(status().isBadRequest());

        verify(timeBlockService, never()).createTimeBlock(any(TimeBlockDTO.class));
    }

    @Test
    void testCreateTimeBlock_Conflict() throws Exception {
        // Arrange
        TimeBlockDTO conflictingTimeBlock = new TimeBlockDTO(
            null,
            "Conflicting Class",
            "Monday",
            "09:30",
            "10:30",
            "Academic",
            "This conflicts with existing time block",
            "#FF9800",
            123,
            15
        );

        when(timeBlockService.createTimeBlock(any(TimeBlockDTO.class)))
                .thenThrow(new IllegalArgumentException("Time block conflicts with existing schedule"));

        // Act & Assert
        mockMvc.perform(post("/api/timeblocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conflictingTimeBlock)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Time block conflicts with existing schedule"));

        verify(timeBlockService, times(1)).createTimeBlock(any(TimeBlockDTO.class));
    }

    @Test
    void testUpdateTimeBlock_Success() throws Exception {
        // Arrange
        Integer timeBlockId = 1;
        TimeBlockDTO updateData = new TimeBlockDTO(
            1,
            "Updated Math Class",
            "Monday",
            "09:00",
            "10:30",
            "Academic",
            "Updated description",
            "#FF5722",
            123,
            15
        );

        when(timeBlockService.updateTimeBlock(eq(timeBlockId), any(TimeBlockDTO.class)))
                .thenReturn(Optional.of(updateData));

        // Act & Assert
        mockMvc.perform(put("/api/timeblocks/{id}", timeBlockId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Updated Math Class"))
                .andExpect(jsonPath("$.description").value("Updated description"));

        verify(timeBlockService, times(1)).updateTimeBlock(eq(timeBlockId), any(TimeBlockDTO.class));
    }

    @Test
    void testDeleteTimeBlock_Success() throws Exception {
        // Arrange
        Integer timeBlockId = 1;
        when(timeBlockService.deleteTimeBlock(timeBlockId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/timeblocks/{id}", timeBlockId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Time block deleted successfully"));

        verify(timeBlockService, times(1)).deleteTimeBlock(timeBlockId);
    }

    @Test
    void testDeleteTimeBlock_NotFound() throws Exception {
        // Arrange
        Integer timeBlockId = 999;
        when(timeBlockService.deleteTimeBlock(timeBlockId)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/timeblocks/{id}", timeBlockId))
                .andExpect(status().isNotFound());

        verify(timeBlockService, times(1)).deleteTimeBlock(timeBlockId);
    }

    @Test
    void testGetTimeBlocksByType_Success() throws Exception {
        // Arrange
        Integer studentId = 123;
        String type = "Academic";
        List<TimeBlockDTO> academicTimeBlocks = Arrays.asList(sampleTimeBlock);

        when(timeBlockService.getTimeBlocksByType(studentId, type)).thenReturn(academicTimeBlocks);

        // Act & Assert
        mockMvc.perform(get("/api/timeblocks/student/{studentId}/type/{type}", studentId, type))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].type").value("Academic"));

        verify(timeBlockService, times(1)).getTimeBlocksByType(studentId, type);
    }

    @Test
    void testHealthCheck() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/timeblocks/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("timeblock-service"));
    }

    @Test
    void testCorsConfiguration() throws Exception {
        // Test that CORS is properly configured
        mockMvc.perform(options("/api/timeblocks/health")
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk());
    }
}
