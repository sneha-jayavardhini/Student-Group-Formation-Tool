package com.asdc.group6.ToolAdmin;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewCoursesController {

	@GetMapping("/viewCoursesView")
	public String getAdminView() {
		return "ViewCourses.html";
	}

}