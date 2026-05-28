package com.ajp.course.dto;

public class DeptPerformanceDTO {
    private String department;
    private long totalEnrollments;
    private long completedCourses;
    private long inProgressCourses;
    private long certificateEligible;
    private double avgProgressPercentage;
    private double avgFinalScore;
    private double completionPercentage; // (completed / total) * 100

    public DeptPerformanceDTO() {}

    public DeptPerformanceDTO(String department, long totalEnrollments, long completedCourses,
                             long inProgressCourses, long certificateEligible,
                             double avgProgressPercentage, double avgFinalScore, double completionPercentage) {
        this.department = department;
        this.totalEnrollments = totalEnrollments;
        this.completedCourses = completedCourses;
        this.inProgressCourses = inProgressCourses;
        this.certificateEligible = certificateEligible;
        this.avgProgressPercentage = avgProgressPercentage;
        this.avgFinalScore = avgFinalScore;
        this.completionPercentage = completionPercentage;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public long getTotalEnrollments() { return totalEnrollments; }
    public void setTotalEnrollments(long totalEnrollments) { this.totalEnrollments = totalEnrollments; }

    public long getCompletedCourses() { return completedCourses; }
    public void setCompletedCourses(long completedCourses) { this.completedCourses = completedCourses; }

    public long getInProgressCourses() { return inProgressCourses; }
    public void setInProgressCourses(long inProgressCourses) { this.inProgressCourses = inProgressCourses; }

    public long getCertificateEligible() { return certificateEligible; }
    public void setCertificateEligible(long certificateEligible) { this.certificateEligible = certificateEligible; }

    public double getAvgProgressPercentage() { return avgProgressPercentage; }
    public void setAvgProgressPercentage(double avgProgressPercentage) { this.avgProgressPercentage = avgProgressPercentage; }

    public double getAvgFinalScore() { return avgFinalScore; }
    public void setAvgFinalScore(double avgFinalScore) { this.avgFinalScore = avgFinalScore; }

    public double getCompletionPercentage() { return completionPercentage; }
    public void setCompletionPercentage(double completionPercentage) { this.completionPercentage = completionPercentage; }
}
