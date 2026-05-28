package com.ajp.course.service;

import com.ajp.course.dto.AnalyticsSummaryDTO;
import com.ajp.course.entity.CourseEnrollment;

import java.util.List;

public interface CourseEnrollmentService {

    CourseEnrollment save(CourseEnrollment enrollment);

    List<CourseEnrollment> findAll();

    CourseEnrollment findById(Long id);

    void deleteById(Long id);

    List<CourseEnrollment> search(String department, Integer year,
                                  String courseCategory, String platformName,
                                  String courseStatus, String certificateStatus);

    AnalyticsSummaryDTO getAnalytics();

    // Dropdown helpers
    List<String> getDistinctDepartments();
    List<Integer> getDistinctYears();
    List<String> getDistinctCategories();
    List<String> getDistinctPlatforms();

    // Uniqueness validation helpers
    boolean isDuplicateEnrollment(String rollNumber, String courseName);
    boolean isDuplicateEnrollmentForUpdate(String rollNumber, String courseName, Long id);
}
