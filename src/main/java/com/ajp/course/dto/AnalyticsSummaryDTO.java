package com.ajp.course.dto;

import java.util.List;
import java.util.Map;

public class AnalyticsSummaryDTO {

    // Summary counts
    private long totalEnrollments;
    private long completedCourses;
    private long inProgressCourses;
    private long notStartedCourses;
    private long certificateEligible;
    private long notEligible;

    // Computed metrics
    private double avgProgressPercentage;
    private double avgFinalScore;
    private double highestFinalScore;
    private double lowestFinalScoreAmongCompleted;
    private String platformWithMostEligible;
    private double percentageCompleted;

    // Distribution maps
    private Map<String, Long> departmentWiseCount;
    private Map<Integer, Long> yearWiseCount;
    private Map<String, Long> categoryWiseCount;
    private Map<String, Long> platformWiseCount;

    // Tables
    private List<DeptPerformanceDTO> deptPerformanceList;
    private List<TopStudentDTO> top5Students;

    public AnalyticsSummaryDTO() {}

    public AnalyticsSummaryDTO(long totalEnrollments, long completedCourses, long inProgressCourses,
                              long notStartedCourses, long certificateEligible, long notEligible,
                              double avgProgressPercentage, double avgFinalScore, double highestFinalScore,
                              double lowestFinalScoreAmongCompleted, String platformWithMostEligible,
                              double percentageCompleted, Map<String, Long> departmentWiseCount,
                              Map<Integer, Long> yearWiseCount, Map<String, Long> categoryWiseCount,
                              Map<String, Long> platformWiseCount, List<DeptPerformanceDTO> deptPerformanceList,
                              List<TopStudentDTO> top5Students) {
        this.totalEnrollments = totalEnrollments;
        this.completedCourses = completedCourses;
        this.inProgressCourses = inProgressCourses;
        this.notStartedCourses = notStartedCourses;
        this.certificateEligible = certificateEligible;
        this.notEligible = notEligible;
        this.avgProgressPercentage = avgProgressPercentage;
        this.avgFinalScore = avgFinalScore;
        this.highestFinalScore = highestFinalScore;
        this.lowestFinalScoreAmongCompleted = lowestFinalScoreAmongCompleted;
        this.platformWithMostEligible = platformWithMostEligible;
        this.percentageCompleted = percentageCompleted;
        this.departmentWiseCount = departmentWiseCount;
        this.yearWiseCount = yearWiseCount;
        this.categoryWiseCount = categoryWiseCount;
        this.platformWiseCount = platformWiseCount;
        this.deptPerformanceList = deptPerformanceList;
        this.top5Students = top5Students;
    }

    public long getTotalEnrollments() { return totalEnrollments; }
    public void setTotalEnrollments(long totalEnrollments) { this.totalEnrollments = totalEnrollments; }

    public long getCompletedCourses() { return completedCourses; }
    public void setCompletedCourses(long completedCourses) { this.completedCourses = completedCourses; }

    public long getInProgressCourses() { return inProgressCourses; }
    public void setInProgressCourses(long inProgressCourses) { this.inProgressCourses = inProgressCourses; }

    public long getNotStartedCourses() { return notStartedCourses; }
    public void setNotStartedCourses(long notStartedCourses) { this.notStartedCourses = notStartedCourses; }

    public long getCertificateEligible() { return certificateEligible; }
    public void setCertificateEligible(long certificateEligible) { this.certificateEligible = certificateEligible; }

    public long getNotEligible() { return notEligible; }
    public void setNotEligible(long notEligible) { this.notEligible = notEligible; }

    public double getAvgProgressPercentage() { return avgProgressPercentage; }
    public void setAvgProgressPercentage(double avgProgressPercentage) { this.avgProgressPercentage = avgProgressPercentage; }

    public double getAvgFinalScore() { return avgFinalScore; }
    public void setAvgFinalScore(double avgFinalScore) { this.avgFinalScore = avgFinalScore; }

    public double getHighestFinalScore() { return highestFinalScore; }
    public void setHighestFinalScore(double highestFinalScore) { this.highestFinalScore = highestFinalScore; }

    public double getLowestFinalScoreAmongCompleted() { return lowestFinalScoreAmongCompleted; }
    public void setLowestFinalScoreAmongCompleted(double lowestFinalScoreAmongCompleted) { this.lowestFinalScoreAmongCompleted = lowestFinalScoreAmongCompleted; }

    public String getPlatformWithMostEligible() { return platformWithMostEligible; }
    public void setPlatformWithMostEligible(String platformWithMostEligible) { this.platformWithMostEligible = platformWithMostEligible; }

    public double getPercentageCompleted() { return percentageCompleted; }
    public void setPercentageCompleted(double percentageCompleted) { this.percentageCompleted = percentageCompleted; }

    public Map<String, Long> getDepartmentWiseCount() { return departmentWiseCount; }
    public void setDepartmentWiseCount(Map<String, Long> departmentWiseCount) { this.departmentWiseCount = departmentWiseCount; }

    public Map<Integer, Long> getYearWiseCount() { return yearWiseCount; }
    public void setYearWiseCount(Map<Integer, Long> yearWiseCount) { this.yearWiseCount = yearWiseCount; }

    public Map<String, Long> getCategoryWiseCount() { return categoryWiseCount; }
    public void setCategoryWiseCount(Map<String, Long> categoryWiseCount) { this.categoryWiseCount = categoryWiseCount; }

    public Map<String, Long> getPlatformWiseCount() { return platformWiseCount; }
    public void setPlatformWiseCount(Map<String, Long> platformWiseCount) { this.platformWiseCount = platformWiseCount; }

    public List<DeptPerformanceDTO> getDeptPerformanceList() { return deptPerformanceList; }
    public void setDeptPerformanceList(List<DeptPerformanceDTO> deptPerformanceList) { this.deptPerformanceList = deptPerformanceList; }

    public List<TopStudentDTO> getTop5Students() { return top5Students; }
    public void setTop5Students(List<TopStudentDTO> top5Students) { this.top5Students = top5Students; }
}
