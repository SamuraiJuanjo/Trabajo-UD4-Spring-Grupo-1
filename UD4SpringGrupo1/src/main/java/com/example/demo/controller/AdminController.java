package com.example.demo.controller;

import java.util.List;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.ProFamily;
import com.example.demo.model.StudentModel;
import com.example.demo.repository.ProFamilyRepository;
import com.example.demo.service.StudentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private static final String ADMIN_VIEW = "/admin/adminScreen";
	private static final String EDIT_STUDENT_VIEW = "/admin/editStudent";
	
	@Autowired
	@Qualifier("studentService")
	 private StudentService studentService;
	
	@Autowired
	@Qualifier("proFamilyRepository")
	 private ProFamilyRepository proFamilyRepository;
	
	@GetMapping("/editStudent/{studentId}")
	public String editStudent(@PathVariable("studentId") int studentId, Model model) {
		List<ProFamily> proFamilyList = proFamilyRepository.findAll();
		model.addAttribute("profesionalFamilies", proFamilyList); 
		StudentModel student = studentService.getStudentById(studentId);
		model.addAttribute("student", student); 
	    return EDIT_STUDENT_VIEW;
	}
	
	@PostMapping("/editStudent")
    public String saveEditedStudent(@ModelAttribute StudentModel studentModel) {
        studentService.updateStudent(studentModel);
        return "redirect:/adminScreen";
    }
	
	@PostMapping("/enabled/{studentId}")
	public String enable(@PathVariable("studentId") int studentId, Model model) {
		studentService.enableStudent(studentId);
	    return "redirect:/adminScreen";
	}
	
	@PostMapping("/deleteStudent/{studentId}")
	public String delete(@PathVariable("studentId") int studentId, Model model) {
		studentService.deleteStudent(studentId);
	    
	    return "redirect:/adminScreen";
	}
	
	@GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Model model) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        model.addAttribute("logoutMessage", "You have successfully logged out");
        return "redirect:/home?logout";
    }

}
