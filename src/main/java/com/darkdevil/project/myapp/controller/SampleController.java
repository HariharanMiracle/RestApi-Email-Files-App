package com.darkdevil.project.myapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sample")
public class SampleController {

	@GetMapping("method1")
	public String method1() {
		return "Method 1";
	}
	
}
