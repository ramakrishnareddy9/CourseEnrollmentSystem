package com.ajp.course.service;

import com.ajp.course.dto.AnalyticsSummaryDTO;
import com.ajp.course.dto.DeptPerformanceDTO;
import com.ajp.course.dto.TopStudentDTO;
import com.ajp.course.entity.CourseEnrollment;
import com.ajp.course.repository.CourseEnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseEnrollmentServiceImpl implements CourseEnrollmentService {

    private final CourseEnrollmentRepository repo;

    @Autowired
    public CourseEnrollmentServiceImpl(CourseEnrollmentRepository repo) {
        this.repo = repo;
    }

    @Override
    public CourseEnrollment save(CourseEnrollment enrollment) {
        // @PrePersist/@PreUpdate on entity handles all computations
        return repo.save(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseEnrollment> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public CourseEnrollment findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Enrollment not found with id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseEnrollment> search(String department, Integer year,
                                          String courseCategory, String platformName,
                                          String courseStatus, String certificateStatus) {
        return repo.searchEnrollments(department, year, courseCategory, platformName, courseStatus, certificateStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public AnalyticsSummaryDTO getAnalytics() {
        AnalyticsSummaryDTO dto = new AnalyticsSummaryDTO();

        // ── Basic counts ──────────────────────────────────────────
        long total = repo.count();
        long completed = repo.countByCourseStatus("Completed");
        long inProgress = repo.countByCourseStatus("In Progress");
        long notStarted = repo.countByCourseStatus("Not Started");
        long eligible = repo.countByCertificateStatus("Eligible for Certificate");
        long notEligible = repo.countByCertificateStatus("Not Eligible");

        dto.setTotalEnrollments(total);
        dto.setCompletedCourses(completed);
        dto.setInProgressCourses(inProgress);
        dto.setNotStartedCourses(notStarted);
        dto.setCertificateEligible(eligible);
        dto.setNotEligible(notEligible);

        // ── Computed metrics ──────────────────────────────────────
        Double avgProgress = repo.findAvgProgressPercentage();
        Double avgFinal = repo.findAvgFinalScore();
        Double maxFinal = repo.findMaxFinalScore();
        Double minFinal = repo.findMinFinalScoreAmongCompleted();

        dto.setAvgProgressPercentage(round2(avgProgress != null ? avgProgress : 0.0));
        dto.setAvgFinalScore(round2(avgFinal != null ? avgFinal : 0.0));
        dto.setHighestFinalScore(round2(maxFinal != null ? maxFinal : 0.0));
        dto.setLowestFinalScoreAmongCompleted(round2(minFinal != null ? minFinal : 0.0));
        dto.setPercentageCompleted(total > 0 ? round2((completed * 100.0) / total) : 0.0);

        // Platform with most eligible — use Pageable(0,1) for JPQL top-1
        List<String> topPlatforms = repo.findPlatformWithMostEligible(PageRequest.of(0, 1));
        dto.setPlatformWithMostEligible(!topPlatforms.isEmpty() ? topPlatforms.get(0) : "N/A");

        // ── Distribution maps ──────────────────────────────────────
        dto.setDepartmentWiseCount(toStringLongMap(repo.countByDepartment()));
        dto.setYearWiseCount(toIntLongMap(repo.countByYear()));
        dto.setCategoryWiseCount(toStringLongMap(repo.countByCourseCategory()));
        dto.setPlatformWiseCount(toStringLongMap(repo.countByPlatformName()));

        // ── Department-wise performance table ─────────────────────
        List<Object[]> deptRows = repo.getDeptPerformance();
        List<DeptPerformanceDTO> deptList = new ArrayList<>();
        for (Object[] row : deptRows) {
            String dept = (String) row[0];
            long totalE = ((Number) row[1]).longValue();
            long comp   = ((Number) row[2]).longValue();
            long inProg = ((Number) row[3]).longValue();
            long cert   = ((Number) row[4]).longValue();
            double avgProg = ((Number) row[5]).doubleValue();
            double avgFin  = ((Number) row[6]).doubleValue();
            double compPct = totalE > 0 ? round2((comp * 100.0) / totalE) : 0.0;

            deptList.add(new DeptPerformanceDTO(
                    dept, totalE, comp, inProg, cert,
                    round2(avgProg), round2(avgFin), compPct));
        }
        dto.setDeptPerformanceList(deptList);

        // ── Top 5 students ────────────────────────────────────────
        List<CourseEnrollment> top5 = repo.findTopStudents(PageRequest.of(0, 5));
        List<TopStudentDTO> top5DTOs = new ArrayList<>();
        for (int i = 0; i < top5.size(); i++) {
            CourseEnrollment e = top5.get(i);
            top5DTOs.add(new TopStudentDTO(
                    i + 1,
                    e.getStudentName(),
                    e.getRollNumber(),
                    e.getDepartment(),
                    e.getCourseName(),
                    round2(e.getFinalScore() != null ? e.getFinalScore() : 0),
                    round2(e.getProgressPercentage() != null ? e.getProgressPercentage() : 0),
                    e.getQuizScore() != null ? e.getQuizScore() : 0,
                    e.getCourseStatus(),
                    e.getCertificateStatus(),
                    e.getRemarks()
            ));
        }
        dto.setTop5Students(top5DTOs);

        return dto;
    }

    // ── Dropdown helpers ──────────────────────────────────────────
    @Override public List<String> getDistinctDepartments() { return repo.findDistinctDepartments(); }
    @Override public List<Integer> getDistinctYears()      { return repo.findDistinctYears(); }
    @Override public List<String> getDistinctCategories()  { return repo.findDistinctCategories(); }
    @Override public List<String> getDistinctPlatforms()   { return repo.findDistinctPlatforms(); }

    // ── Uniqueness checks ─────────────────────────────────────────
    @Override public boolean isDuplicateEnrollment(String rollNumber, String courseName) {
        return repo.existsByRollNumberAndCourseName(rollNumber, courseName);
    }
    @Override public boolean isDuplicateEnrollmentForUpdate(String rollNumber, String courseName, Long id) {
        return repo.existsByRollNumberAndCourseNameAndIdNot(rollNumber, courseName, id);
    }

    // ── Utility ───────────────────────────────────────────────────
    private double round2(double val) {
        return Math.round(val * 100.0) / 100.0;
    }

    private Map<String, Long> toStringLongMap(List<Object[]> rows) {
        Map<String, Long> map = new LinkedHashMap<>();
        for (Object[] row : rows) {
            map.put((String) row[0], ((Number) row[1]).longValue());
        }
        return map;
    }

    private Map<Integer, Long> toIntLongMap(List<Object[]> rows) {
        Map<Integer, Long> map = new LinkedHashMap<>();
        for (Object[] row : rows) {
            map.put(((Number) row[0]).intValue(), ((Number) row[1]).longValue());
        }
        return map;
    }
}
