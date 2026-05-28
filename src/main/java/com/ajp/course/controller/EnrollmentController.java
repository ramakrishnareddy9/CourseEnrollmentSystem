package com.ajp.course.controller;

import com.ajp.course.entity.CourseEnrollment;
import com.ajp.course.service.CourseEnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final CourseEnrollmentService service;

    @Autowired
    public EnrollmentController(CourseEnrollmentService service) {
        this.service = service;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LIST (with optional search/filter)
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping
    public String list(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String courseCategory,
            @RequestParam(required = false) String platformName,
            @RequestParam(required = false) String courseStatus,
            @RequestParam(required = false) String certificateStatus,
            Model model) {

        boolean hasFilter = (department != null && !department.isEmpty())
                || year != null
                || (courseCategory != null && !courseCategory.isEmpty())
                || (platformName != null && !platformName.isEmpty())
                || (courseStatus != null && !courseStatus.isEmpty())
                || (certificateStatus != null && !certificateStatus.isEmpty());

        List<CourseEnrollment> enrollments = hasFilter
                ? service.search(department, year, courseCategory, platformName, courseStatus, certificateStatus)
                : service.findAll();

        model.addAttribute("enrollments", enrollments);
        model.addAttribute("departments", service.getDistinctDepartments());
        model.addAttribute("years", service.getDistinctYears());
        model.addAttribute("categories", service.getDistinctCategories());
        model.addAttribute("platforms", service.getDistinctPlatforms());

        // Retain selected filter values
        model.addAttribute("selectedDept", department);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedCategory", courseCategory);
        model.addAttribute("selectedPlatform", platformName);
        model.addAttribute("selectedStatus", courseStatus);
        model.addAttribute("selectedCertStatus", certificateStatus);
        model.addAttribute("totalCount", enrollments.size());

        return "enrollment/list";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // VIEW SINGLE RECORD
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("enrollment", service.findById(id));
        return "enrollment/view";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SHOW ADD FORM
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("enrollment", new CourseEnrollment());
        model.addAttribute("formTitle", "Add New Enrollment");
        model.addAttribute("isEdit", false);
        return "enrollment/form";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SHOW EDIT FORM
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("enrollment", service.findById(id));
        model.addAttribute("formTitle", "Edit Enrollment");
        model.addAttribute("isEdit", true);
        return "enrollment/form";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SAVE (Create + Update)
    // ─────────────────────────────────────────────────────────────────────────
    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("enrollment") CourseEnrollment enrollment,
            BindingResult result,
            @RequestParam(value = "isEdit", defaultValue = "false") boolean isEdit,
            RedirectAttributes redirectAttrs,
            Model model) {

        // Cross-field validations
        if (enrollment.getCompletedModules() != null && enrollment.getTotalModules() != null
                && enrollment.getCompletedModules() > enrollment.getTotalModules()) {
            result.rejectValue("completedModules", "error.completedModules",
                    "Completed modules cannot exceed total modules");
        }

        if (enrollment.getCompletionDate() != null && enrollment.getEnrollmentDate() != null
                && enrollment.getCompletionDate().isBefore(enrollment.getEnrollmentDate())) {
            result.rejectValue("completionDate", "error.completionDate",
                    "Completion date cannot be earlier than enrollment date");
        }

        // Uniqueness check
        boolean duplicate = isEdit
                ? service.isDuplicateEnrollmentForUpdate(enrollment.getRollNumber(), enrollment.getCourseName(), enrollment.getEnrollmentId())
                : service.isDuplicateEnrollment(enrollment.getRollNumber(), enrollment.getCourseName());

        if (duplicate) {
            result.rejectValue("rollNumber", "error.rollNumber",
                    "A student with this Roll Number is already enrolled in this course");
        }

        if (result.hasErrors()) {
            model.addAttribute("formTitle", isEdit ? "Edit Enrollment" : "Add New Enrollment");
            model.addAttribute("isEdit", isEdit);
            return "enrollment/form";
        }

        service.save(enrollment);
        redirectAttrs.addFlashAttribute("successMessage",
                isEdit ? "Enrollment updated successfully!" : "Enrollment added successfully!");
        return "redirect:/enrollments";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        service.deleteById(id);
        redirectAttrs.addFlashAttribute("successMessage", "Enrollment deleted successfully!");
        return "redirect:/enrollments";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HOME redirect
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/")
    public String home() {
        return "redirect:/enrollments";
    }
}
