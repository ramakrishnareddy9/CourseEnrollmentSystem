package com.ajp.course.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "course_enrollments")
public class CourseEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    @NotBlank(message = "Student name must not be empty")
    @Column(nullable = false)
    private String studentName;

    @NotBlank(message = "Roll number must not be empty")
    @Column(nullable = false)
    private String rollNumber;

    @NotBlank(message = "Department must not be empty")
    private String department;

    @NotNull(message = "Year must not be empty")
    @Min(value = 1, message = "Year must be between 1 and 4")
    @Max(value = 4, message = "Year must be between 1 and 4")
    private Integer year;

    @NotBlank(message = "Course name must not be empty")
    private String courseName;

    @NotBlank(message = "Course category must not be empty")
    private String courseCategory;

    @NotBlank(message = "Platform name must not be empty")
    private String platformName;

    @NotNull(message = "Enrollment date must not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate enrollmentDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate completionDate;

    @NotNull(message = "Total modules must not be empty")
    @Min(value = 1, message = "Total modules must be greater than 0")
    private Integer totalModules;

    @NotNull(message = "Completed modules must not be empty")
    @Min(value = 0, message = "Completed modules cannot be negative")
    private Integer completedModules;

    @NotNull(message = "Assignment score must not be empty")
    @DecimalMin(value = "0.0", message = "Assignment score must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Assignment score must be between 0 and 100")
    private Double assignmentScore;

    @NotNull(message = "Quiz score must not be empty")
    @DecimalMin(value = "0.0", message = "Quiz score must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Quiz score must be between 0 and 100")
    private Double quizScore;

    // ---- Computed / derived fields (persisted) ----
    private Double progressPercentage;
    private Double finalScore;

    private String courseStatus;       // Not Started | In Progress | Completed
    private String certificateStatus;  // Eligible for Certificate | Not Eligible
    private String remarks;

    // -----------------------------------------------
    // Business logic: called before every save
    // -----------------------------------------------
    @PrePersist
    @PreUpdate
    public void computeDerivedFields() {
        // Progress
        if (totalModules != null && totalModules > 0 && completedModules != null) {
            this.progressPercentage = (completedModules * 100.0) / totalModules;
        } else {
            this.progressPercentage = 0.0;
        }

        // Final score
        if (assignmentScore != null && quizScore != null) {
            this.finalScore = (assignmentScore * 0.6) + (quizScore * 0.4);
            // Round to 2 decimal places
            this.finalScore = Math.round(this.finalScore * 100.0) / 100.0;
        } else {
            this.finalScore = 0.0;
        }

        // Course status
        if (completedModules == null || completedModules == 0) {
            this.courseStatus = "Not Started";
        } else if (progressPercentage >= 100.0) {
            this.courseStatus = "Completed";
        } else {
            this.courseStatus = "In Progress";
        }

        // Certificate status
        if ("Completed".equals(this.courseStatus) && this.finalScore >= 60.0) {
            this.certificateStatus = "Eligible for Certificate";
        } else {
            this.certificateStatus = "Not Eligible";
        }

        // Remarks
        if (this.finalScore >= 85.0 && progressPercentage >= 100.0) {
            this.remarks = "Top Performer";
        } else if (this.remarks == null || this.remarks.equals("Top Performer")) {
            // only clear auto-set remark; preserve manual remarks
            this.remarks = "";
        }
    }

    // -----------------------------------------------
    // Getters and Setters
    // -----------------------------------------------
    public Long getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(Long enrollmentId) { this.enrollmentId = enrollmentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCourseCategory() { return courseCategory; }
    public void setCourseCategory(String courseCategory) { this.courseCategory = courseCategory; }

    public String getPlatformName() { return platformName; }
    public void setPlatformName(String platformName) { this.platformName = platformName; }

    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public LocalDate getCompletionDate() { return completionDate; }
    public void setCompletionDate(LocalDate completionDate) { this.completionDate = completionDate; }

    public Integer getTotalModules() { return totalModules; }
    public void setTotalModules(Integer totalModules) { this.totalModules = totalModules; }

    public Integer getCompletedModules() { return completedModules; }
    public void setCompletedModules(Integer completedModules) { this.completedModules = completedModules; }

    public Double getAssignmentScore() { return assignmentScore; }
    public void setAssignmentScore(Double assignmentScore) { this.assignmentScore = assignmentScore; }

    public Double getQuizScore() { return quizScore; }
    public void setQuizScore(Double quizScore) { this.quizScore = quizScore; }

    public Double getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Double progressPercentage) { this.progressPercentage = progressPercentage; }

    public Double getFinalScore() { return finalScore; }
    public void setFinalScore(Double finalScore) { this.finalScore = finalScore; }

    public String getCourseStatus() { return courseStatus; }
    public void setCourseStatus(String courseStatus) { this.courseStatus = courseStatus; }

    public String getCertificateStatus() { return certificateStatus; }
    public void setCertificateStatus(String certificateStatus) { this.certificateStatus = certificateStatus; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
