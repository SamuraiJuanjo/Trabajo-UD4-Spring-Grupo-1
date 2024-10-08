package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Business;
import com.example.demo.entity.ProFamily;
import com.example.demo.entity.Student;
import com.example.demo.model.StudentModel;
import com.example.demo.service.ProFamilyService;
import com.example.demo.service.StudentService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private static final String ADMIN_VIEW = "admin/adminScreen";
	private static final String ADMIN_STUDENTS = "admin/studentStats";
	private static final String EDIT_STUDENT_VIEW = "admin/editStudent";
	
	@Autowired
	@Qualifier("studentService")
	 private StudentService studentService;
	
	@Autowired
	@Qualifier("proFamilyService")
	 private ProFamilyService proFamilyService;
	
	@GetMapping("/adminScreen")
	public String adminScreen(Model model,
			@RequestParam(name="opcion", required=false, defaultValue="null") String opcion,
			@RequestParam(name="filterBy", required=false, defaultValue="null") String filterBy) {
	    List<Student> students = studentService.getAdminScreenFilterBy(opcion, filterBy);
	    model.addAttribute("students", students);
	    
	    List<ProFamily> proFamilies = proFamilyService.getAllNotEmpty();
	    model.addAttribute("proFamilies", proFamilies);

	    return ADMIN_VIEW;
	}
	
	@GetMapping("/studentStats")
	public String adminStudentsList(Model model, 
			@RequestParam(name="opcion", required=false, defaultValue="null") String opcion,
			@RequestParam(name="filterBy", required=false, defaultValue="null") String filterBy) {
	    List<Student> students = studentService.getAdminScreenFilterBy(opcion, filterBy);
	    model.addAttribute("students", students);
	    
	    List<ProFamily> proFamilies = proFamilyService.getAllNotEmpty();
	    model.addAttribute("proFamilies", proFamilies);
	 	    
	    Map<Integer, Double> averageValorations = studentService.getAverageValoration(students);
	    model.addAttribute("averageValorations", averageValorations);
	    	    
	    Map<Integer, Integer> numberOfFinishedServices = studentService.getNumberOfFinishedServices(students);
	    model.addAttribute("numberOfFinishedServices", numberOfFinishedServices);

	    return ADMIN_STUDENTS;
	}

	
	@GetMapping("/editStudent/{studentId}")
	public String editStudent(@PathVariable("studentId") int studentId, Model model) {
		List<ProFamily> proFamilyList = proFamilyService.getAll();
		model.addAttribute("profesionalFamilies", proFamilyList); 
		StudentModel student = studentService.getStudentById(studentId);
		model.addAttribute("student", student); 
	    return EDIT_STUDENT_VIEW;
	}
	
	@PostMapping("/editStudent")
    public String saveEditedStudent(@ModelAttribute StudentModel studentModel) {
        studentService.updateStudent(studentModel);
        return "redirect:/admin/adminScreen";
    }
	
	@PostMapping("/enabled/{studentId}")
	public String enable(@PathVariable("studentId") int studentId, Model model, @RequestParam(name="filterBy", required=false, defaultValue="null") String filterBy) {
		
		studentService.enableStudent(studentId);
		    
	    return "redirect:/admin/adminScreen?filterBy="+filterBy;
	}
	
	@PostMapping("/deleteStudent/{studentId}")
	public String delete(@PathVariable("studentId") int studentId, Model model) {
		studentService.deleteStudent(studentId);
	    
	    return "redirect:/admin/adminScreen";
	}

}
