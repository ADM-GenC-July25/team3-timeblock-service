package com.scheduleplanner.timeblock.controller;

import com.scheduleplanner.timeblock.dto.TimeBlockDTO;
import com.scheduleplanner.timeblock.service.TimeBlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/timeblocks")
@CrossOrigin(origins = {
    "http://localhost:3000", 
    "http://localhost:5173", 
    "https://2362857-team3-bucket.s3-website-us-west-2.amazonaws.com",
    "https://2362857-team3-bucket-main.s3-website-us-west-2.amazonaws.com"
})
@Tag(name = "TimeBlock", description = "API for managing personal time blocks")
@Validated
public class TimeBlockController {
    
    private static final Logger logger = LoggerFactory.getLogger(TimeBlockController.class);
    
    @Autowired
    private TimeBlockService timeBlockService;
    
    /**
     * Get all time blocks for a specific student
     */
    @Operation(summary = "Get time blocks for student", description = "Retrieves all time blocks for a specific student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved time blocks"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TimeBlockDTO>> getTimeBlocksByStudentId(
            @Parameter(description = "Student ID", required = true) @PathVariable Integer studentId) {
        try {
            logger.info("Fetching time blocks for student: {}", studentId);
            List<TimeBlockDTO> timeBlocks = timeBlockService.getTimeBlocksByStudentId(studentId);
            return ResponseEntity.ok(timeBlocks);
        } catch (Exception e) {
            logger.error("Error fetching time blocks for student {}: {}", studentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get time blocks for a student on a specific day
     */
    @Operation(summary = "Get time blocks by day", description = "Retrieves time blocks for a student on a specific day")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved time blocks"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/student/{studentId}/day/{day}")
    public ResponseEntity<List<TimeBlockDTO>> getTimeBlocksByStudentIdAndDay(
            @Parameter(description = "Student ID", required = true) @PathVariable Integer studentId,
            @Parameter(description = "Day of week", required = true) @PathVariable String day) {
        try {
            logger.info("Fetching time blocks for student: {} on day: {}", studentId, day);
            List<TimeBlockDTO> timeBlocks = timeBlockService.getTimeBlocksByStudentIdAndDay(studentId, day);
            return ResponseEntity.ok(timeBlocks);
        } catch (Exception e) {
            logger.error("Error fetching time blocks for student {} on day {}: {}", studentId, day, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get a specific time block by ID
     */
    @Operation(summary = "Get time block by ID", description = "Retrieves a specific time block by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved time block"),
        @ApiResponse(responseCode = "404", description = "Time block not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TimeBlockDTO> getTimeBlockById(
            @Parameter(description = "Time Block ID", required = true) @PathVariable Integer id) {
        try {
            logger.info("Fetching time block with ID: {}", id);
            Optional<TimeBlockDTO> timeBlock = timeBlockService.getTimeBlockById(id);
            return timeBlock.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error fetching time block with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Create a new time block
     */
    @Operation(summary = "Create time block", description = "Creates a new time block for a student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Time block created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or conflict detected"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<?> createTimeBlock(@Valid @RequestBody TimeBlockDTO timeBlockDTO) {
        try {
            logger.info("Creating new time block: {}", timeBlockDTO.getTitle());
            TimeBlockDTO createdTimeBlock = timeBlockService.createTimeBlock(timeBlockDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTimeBlock);
        } catch (IllegalArgumentException e) {
            logger.warn("Time block creation failed due to conflict: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            logger.error("Error creating time block: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error creating time block: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Update an existing time block
     */
    @Operation(summary = "Update time block", description = "Updates an existing time block")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Time block updated successfully"),
        @ApiResponse(responseCode = "404", description = "Time block not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or conflict detected"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTimeBlock(
            @Parameter(description = "Time Block ID", required = true) @PathVariable Integer id,
            @Valid @RequestBody TimeBlockDTO timeBlockDTO) {
        try {
            logger.info("Updating time block with ID: {}", id);
            Optional<TimeBlockDTO> updatedTimeBlock = timeBlockService.updateTimeBlock(id, timeBlockDTO);
            return updatedTimeBlock.map(ResponseEntity::ok)
                                  .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            logger.warn("Time block update failed due to conflict: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            logger.error("Error updating time block with ID {}: {}", id, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error updating time block: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Delete a time block
     */
    @Operation(summary = "Delete time block", description = "Deletes a time block by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Time block deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Time block not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTimeBlock(
            @Parameter(description = "Time Block ID", required = true) @PathVariable Integer id) {
        try {
            logger.info("Deleting time block with ID: {}", id);
            boolean deleted = timeBlockService.deleteTimeBlock(id);
            if (deleted) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Time block deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Time block not found");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error deleting time block with ID {}: {}", id, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error deleting time block: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get time blocks by type for a student
     */
    @Operation(summary = "Get time blocks by type", description = "Retrieves time blocks of a specific type for a student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved time blocks"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/student/{studentId}/type/{type}")
    public ResponseEntity<List<TimeBlockDTO>> getTimeBlocksByType(
            @Parameter(description = "Student ID", required = true) @PathVariable Integer studentId,
            @Parameter(description = "Time block type", required = true) @PathVariable String type) {
        try {
            logger.info("Fetching time blocks for student: {} of type: {}", studentId, type);
            List<TimeBlockDTO> timeBlocks = timeBlockService.getTimeBlocksByType(studentId, type);
            return ResponseEntity.ok(timeBlocks);
        } catch (Exception e) {
            logger.error("Error fetching time blocks for student {} of type {}: {}", studentId, type, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "timeblock-service");
        return ResponseEntity.ok(status);
    }
} 