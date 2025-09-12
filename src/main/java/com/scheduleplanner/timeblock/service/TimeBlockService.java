package com.scheduleplanner.timeblock.service;

import com.scheduleplanner.timeblock.dto.TimeBlockDTO;
import com.scheduleplanner.timeblock.model.TimeBlock;
import com.scheduleplanner.timeblock.repository.TimeBlockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TimeBlockService {
    
    private static final Logger logger = LoggerFactory.getLogger(TimeBlockService.class);
    
    @Autowired
    private TimeBlockRepository timeBlockRepository;
    
    /**
     * Get all time blocks for a student
     */
    public List<TimeBlockDTO> getTimeBlocksByStudentId(Integer studentId) {
        logger.info("Fetching time blocks for student: {}", studentId);
        
        List<TimeBlock> timeBlocks = timeBlockRepository.findByStudentIdOrderByDayAscStartTimeAsc(studentId);
        return timeBlocks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get time blocks for a student on a specific day
     */
    public List<TimeBlockDTO> getTimeBlocksByStudentIdAndDay(Integer studentId, String day) {
        logger.info("Fetching time blocks for student: {} on day: {}", studentId, day);
        
        List<TimeBlock> timeBlocks = timeBlockRepository.findByStudentIdAndDayOrderByStartTimeAsc(studentId, day);
        return timeBlocks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get a specific time block by ID
     */
    public Optional<TimeBlockDTO> getTimeBlockById(Integer id) {
        logger.info("Fetching time block with ID: {}", id);
        
        Optional<TimeBlock> timeBlock = timeBlockRepository.findById(id);
        return timeBlock.map(this::convertToDTO);
    }
    
    /**
     * Create a new time block
     */
    public TimeBlockDTO createTimeBlock(TimeBlockDTO timeBlockDTO) {
        logger.info("Creating new time block: {}", timeBlockDTO.getTitle());
        
        // Validate for conflicts
        validateTimeBlockConflicts(timeBlockDTO, null);
        
        TimeBlock timeBlock = convertToEntity(timeBlockDTO);
        TimeBlock savedTimeBlock = timeBlockRepository.save(timeBlock);
        
        logger.info("Created time block with ID: {}", savedTimeBlock.getId());
        return convertToDTO(savedTimeBlock);
    }
    
    /**
     * Update an existing time block
     */
    public Optional<TimeBlockDTO> updateTimeBlock(Integer id, TimeBlockDTO timeBlockDTO) {
        logger.info("Updating time block with ID: {}", id);
        
        Optional<TimeBlock> existingTimeBlock = timeBlockRepository.findById(id);
        if (existingTimeBlock.isEmpty()) {
            logger.warn("Time block with ID {} not found for update", id);
            return Optional.empty();
        }
        
        // Validate for conflicts (exclude current time block)
        validateTimeBlockConflicts(timeBlockDTO, id);
        
        TimeBlock timeBlock = existingTimeBlock.get();
        updateEntityFromDTO(timeBlock, timeBlockDTO);
        
        TimeBlock savedTimeBlock = timeBlockRepository.save(timeBlock);
        logger.info("Updated time block with ID: {}", savedTimeBlock.getId());
        
        return Optional.of(convertToDTO(savedTimeBlock));
    }
    
    /**
     * Delete a time block
     */
    public boolean deleteTimeBlock(Integer id) {
        logger.info("Deleting time block with ID: {}", id);
        
        if (timeBlockRepository.existsById(id)) {
            timeBlockRepository.deleteById(id);
            logger.info("Deleted time block with ID: {}", id);
            return true;
        } else {
            logger.warn("Time block with ID {} not found for deletion", id);
            return false;
        }
    }
    
    /**
     * Check for conflicts when creating/updating time blocks
     */
    public List<TimeBlock> checkConflicts(Integer studentId, String day, String startTime, String endTime, Integer excludeId) {
        logger.info("Checking conflicts for student: {} on day: {} from {} to {}", studentId, day, startTime, endTime);
        
        return timeBlockRepository.findConflictingTimeBlocks(studentId, day, startTime, endTime, excludeId);
    }
    
    /**
     * Get time blocks by type
     */
    public List<TimeBlockDTO> getTimeBlocksByType(Integer studentId, String type) {
        logger.info("Fetching time blocks for student: {} of type: {}", studentId, type);
        
        List<TimeBlock> timeBlocks = timeBlockRepository.findByStudentIdAndType(studentId, type);
        return timeBlocks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Delete all time blocks for a student
     */
    public void deleteAllTimeBlocksForStudent(Integer studentId) {
        logger.info("Deleting all time blocks for student: {}", studentId);
        timeBlockRepository.deleteByStudentId(studentId);
    }
    
    // Private helper methods
    
    private void validateTimeBlockConflicts(TimeBlockDTO timeBlockDTO, Integer excludeId) {
        List<TimeBlock> conflicts = checkConflicts(
            timeBlockDTO.getStudentId(),
            timeBlockDTO.getDay(),
            timeBlockDTO.getStartTime(),
            timeBlockDTO.getEndTime(),
            excludeId
        );
        
        if (!conflicts.isEmpty()) {
            String conflictNames = conflicts.stream()
                    .map(TimeBlock::getTitle)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Time conflict detected with: " + conflictNames);
        }
    }
    
    private TimeBlockDTO convertToDTO(TimeBlock timeBlock) {
        return new TimeBlockDTO(
            timeBlock.getId(),
            timeBlock.getTitle(),
            timeBlock.getDay(),
            timeBlock.getStartTime(),
            timeBlock.getEndTime(),
            timeBlock.getType(),
            timeBlock.getDescription(),
            timeBlock.getColor(),
            timeBlock.getStudentId(),
            timeBlock.getWeeks()
        );
    }
    
    private TimeBlock convertToEntity(TimeBlockDTO dto) {
        TimeBlock timeBlock = new TimeBlock();
        updateEntityFromDTO(timeBlock, dto);
        return timeBlock;
    }
    
    private void updateEntityFromDTO(TimeBlock timeBlock, TimeBlockDTO dto) {
        timeBlock.setTitle(dto.getTitle());
        timeBlock.setDay(dto.getDay());
        timeBlock.setStartTime(dto.getStartTime());
        timeBlock.setEndTime(dto.getEndTime());
        timeBlock.setType(dto.getType());
        timeBlock.setDescription(dto.getDescription());
        timeBlock.setColor(dto.getColor());
        timeBlock.setStudentId(dto.getStudentId());
        timeBlock.setWeeks(dto.getWeeks() != null ? dto.getWeeks() : 15); // Default to 15 weeks
    }
} 