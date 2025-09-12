package com.scheduleplanner.timeblock.repository;

import com.scheduleplanner.timeblock.model.TimeBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeBlockRepository extends JpaRepository<TimeBlock, Integer> {
    
    /**
     * Find all time blocks for a specific student
     */
    List<TimeBlock> findByStudentIdOrderByDayAscStartTimeAsc(Integer studentId);
    
    /**
     * Find time blocks for a student on a specific day
     */
    List<TimeBlock> findByStudentIdAndDayOrderByStartTimeAsc(Integer studentId, String day);
    
    /**
     * Find time blocks by type for a student
     */
    List<TimeBlock> findByStudentIdAndType(Integer studentId, String type);
    
    /**
     * Check for conflicting time blocks for a student
     */
    @Query("SELECT tb FROM TimeBlock tb WHERE tb.studentId = :studentId " +
           "AND tb.day = :day " +
           "AND ((tb.startTime <= :startTime AND tb.endTime > :startTime) " +
           "OR (tb.startTime < :endTime AND tb.endTime >= :endTime) " +
           "OR (tb.startTime >= :startTime AND tb.endTime <= :endTime)) " +
           "AND (:excludeId IS NULL OR tb.id != :excludeId)")
    List<TimeBlock> findConflictingTimeBlocks(@Param("studentId") Integer studentId,
                                            @Param("day") String day,
                                            @Param("startTime") String startTime,
                                            @Param("endTime") String endTime,
                                            @Param("excludeId") Integer excludeId);
    
    /**
     * Find time blocks within a time range for a student
     */
    @Query("SELECT tb FROM TimeBlock tb WHERE tb.studentId = :studentId " +
           "AND tb.startTime >= :startTime AND tb.endTime <= :endTime " +
           "ORDER BY tb.day, tb.startTime")
    List<TimeBlock> findTimeBlocksInRange(@Param("studentId") Integer studentId,
                                        @Param("startTime") String startTime,
                                        @Param("endTime") String endTime);
    
    /**
     * Count time blocks for a student
     */
    long countByStudentId(Integer studentId);
    
    /**
     * Delete all time blocks for a student
     */
    void deleteByStudentId(Integer studentId);
} 