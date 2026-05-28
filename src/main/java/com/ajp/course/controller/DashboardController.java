package com.ajp.course.controller;

import com.ajp.course.service.CourseEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final CourseEnrollmentService service;

    @Autowired
    public DashboardController(CourseEnrollmentService service) {
        this.service = service;
    }

    @GetMapping
    public String analytics(Model model) {
        model.addAttribute("analytics", service.getAnalytics());
        return "dashboard/analytics";
    }
}
