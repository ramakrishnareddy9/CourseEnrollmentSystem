package com.ajp.course.repository;

import com.ajp.course.entity.CourseEnrollment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {

    // ── Uniqueness check (rollNumber + courseName, excluding current id) ──
    boolean existsByRollNumberAndCourseName(String rollNumber, String courseName);

    @Query("SELECT COUNT(e) > 0 FROM CourseEnrollment e " +
           "WHERE e.rollNumber = :rollNumber AND e.courseName = :courseName AND e.enrollmentId <> :id")
    boolean existsByRollNumberAndCourseNameAndIdNot(
            @Param("rollNumber") String rollNumber,
            @Param("courseName") String courseName,
            @Param("id") Long id);

    // ── Search / Filter ──
    @Query("SELECT e FROM CourseEnrollment e WHERE " +
           "(:department IS NULL OR :department = '' OR e.department = :department) AND " +
           "(:year IS NULL OR e.year = :year) AND " +
           "(:courseCategory IS NULL OR :courseCategory = '' OR e.courseCategory = :courseCategory) AND " +
           "(:platformName IS NULL OR :platformName = '' OR e.platformName = :platformName) AND " +
           "(:courseStatus IS NULL OR :courseStatus = '' OR e.courseStatus = :courseStatus) AND " +
           "(:certificateStatus IS NULL OR :certificateStatus = '' OR e.certificateStatus = :certificateStatus)")
    List<CourseEnrollment> searchEnrollments(
            @Param("department") String department,
            @Param("year") Integer year,
            @Param("courseCategory") String courseCategory,
            @Param("platformName") String platformName,
            @Param("courseStatus") String courseStatus,
            @Param("certificateStatus") String certificateStatus);

    // ── Analytics: Status counts ──
    long countByCourseStatus(String courseStatus);
    long countByCertificateStatus(String certificateStatus);

    // ── Analytics: Avg, Max, Min ──
    @Query("SELECT COALESCE(AVG(e.progressPercentage), 0) FROM CourseEnrollment e")
    Double findAvgProgressPercentage();

    @Query("SELECT COALESCE(AVG(e.finalScore), 0) FROM CourseEnrollment e")
    Double findAvgFinalScore();

    @Query("SELECT COALESCE(MAX(e.finalScore), 0) FROM CourseEnrollment e")
    Double findMaxFinalScore();

    @Query("SELECT COALESCE(MIN(e.finalScore), 0) FROM CourseEnrollment e WHERE e.courseStatus = 'Completed'")
    Double findMinFinalScoreAmongCompleted();

    // ── Analytics: Distribution counts ──
    @Query("SELECT e.department, COUNT(e) FROM CourseEnrollment e GROUP BY e.department ORDER BY e.department")
    List<Object[]> countByDepartment();

    @Query("SELECT e.year, COUNT(e) FROM CourseEnrollment e GROUP BY e.year ORDER BY e.year")
    List<Object[]> countByYear();

    @Query("SELECT e.courseCategory, COUNT(e) FROM CourseEnrollment e GROUP BY e.courseCategory ORDER BY e.courseCategory")
    List<Object[]> countByCourseCategory();

    @Query("SELECT e.platformName, COUNT(e) FROM CourseEnrollment e GROUP BY e.platformName ORDER BY e.platformName")
    List<Object[]> countByPlatformName();

    // ── Analytics: Platform with most eligible ──
    // Uses Pageable(0,1) instead of LIMIT which is invalid in JPQL
    @Query("SELECT e.platformName FROM CourseEnrollment e WHERE e.certificateStatus = 'Eligible for Certificate' " +
           "GROUP BY e.platformName ORDER BY COUNT(e) DESC")
    List<String> findPlatformWithMostEligible(Pageable pageable);

    // ── Analytics: Department-wise performance ──
    @Query("SELECT e.department, " +
           "COUNT(e), " +
           "SUM(CASE WHEN e.courseStatus = 'Completed' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN e.courseStatus = 'In Progress' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN e.certificateStatus = 'Eligible for Certificate' THEN 1 ELSE 0 END), " +
           "COALESCE(AVG(e.progressPercentage), 0), " +
           "COALESCE(AVG(e.finalScore), 0) " +
           "FROM CourseEnrollment e GROUP BY e.department ORDER BY e.department")
    List<Object[]> getDeptPerformance();

    // ── Analytics: Top students (finalScore DESC, progressPercentage DESC, quizScore DESC) ──
    @Query("SELECT e FROM CourseEnrollment e " +
           "ORDER BY e.finalScore DESC, e.progressPercentage DESC, e.quizScore DESC")
    List<CourseEnrollment> findTopStudents(Pageable pageable);

    // ── Distinct filter values for dropdowns ──
    @Query("SELECT DISTINCT e.department FROM CourseEnrollment e ORDER BY e.department")
    List<String> findDistinctDepartments();

    @Query("SELECT DISTINCT e.year FROM CourseEnrollment e ORDER BY e.year")
    List<Integer> findDistinctYears();

    @Query("SELECT DISTINCT e.courseCategory FROM CourseEnrollment e ORDER BY e.courseCategory")
    List<String> findDistinctCategories();

    @Query("SELECT DISTINCT e.platformName FROM CourseEnrollment e ORDER BY e.platformName")
    List<String> findDistinctPlatforms();
}
