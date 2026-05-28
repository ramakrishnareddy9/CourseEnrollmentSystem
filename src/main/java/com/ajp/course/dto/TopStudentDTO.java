package com.ajp.course.dto;

public class TopStudentDTO {
    private int rank;
    private String studentName;
    private String rollNumber;
    private String department;
    private String courseName;
    private double finalScore;
    private double progressPercentage;
    private double quizScore;
    private String courseStatus;
    private String certificateStatus;
    private String remarks;

    public TopStudentDTO() {}

    public TopStudentDTO(int rank, String studentName, String rollNumber, String department,
                        String courseName, double finalScore, double progressPercentage,
                        double quizScore, String courseStatus, String certificateStatus, String remarks) {
        this.rank = rank;
        this.studentName = studentName;
        this.rollNumber = rollNumber;
        this.department = department;
        this.courseName = courseName;
        this.finalScore = finalScore;
        this.progressPercentage = progressPercentage;
        this.quizScore = quizScore;
        this.courseStatus = courseStatus;
        this.certificateStatus = certificateStatus;
        this.remarks = remarks;
    }

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public double getFinalScore() { return finalScore; }
    public void setFinalScore(double finalScore) { this.finalScore = finalScore; }

    public double getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(double progressPercentage) { this.progressPercentage = progressPercentage; }

    public double getQuizScore() { return quizScore; }
    public void setQuizScore(double quizScore) { this.quizScore = quizScore; }

    public String getCourseStatus() { return courseStatus; }
    public void setCourseStatus(String courseStatus) { this.courseStatus = courseStatus; }

    public String getCertificateStatus() { return certificateStatus; }
    public void setCertificateStatus(String certificateStatus) { this.certificateStatus = certificateStatus; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
