package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Business;
import com.example.demo.entity.ProFamily;
import com.example.demo.entity.Servicio;
import com.example.demo.entity.Student;
import com.example.demo.model.BusinessModel;
import com.example.demo.model.StudentModel;
import com.example.demo.repository.BusinessRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.BusinessService;
import com.example.demo.service.FilesStorageService;
import com.example.demo.service.ServicioService;
import com.example.demo.service.StudentService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@RequestMapping("business")
public class BusinessController {

	private static final String BUSINESS_VIEW = "/admin/business";
	private static final String EDIT_BUSINESS_VIEW = "admin/editBusiness";
	private static final String ADD_BUSINESS_VIEW = "admin/addBusiness";
	private static final String BUSINESS_HOME_VIEW = "/business/businessHome";
	
	@Autowired
	@Qualifier("businessService")
	 private BusinessService businessService;
	
	@Autowired
	@Qualifier("businessRepository")
	 private BusinessRepository businessRepository;
	
	@Autowired
	@Qualifier("servicioService")
	 private ServicioService servicioService;
	
	@Autowired
	@Qualifier("studentRepository")
	private StudentRepository studentRepository;
	
	@Autowired
	@Qualifier("studentService")
	private StudentService studentService;
	
	@Autowired
	@Qualifier("filesStorageService")
	 private FilesStorageService storageService;
	
	@GetMapping("/list")
	public String business(Model model) {
		List<Business> businessList = businessRepository.findAll();
		model.addAttribute("business1", businessList); 
	    return BUSINESS_VIEW;
	}
	
	@GetMapping("/addBusiness")
	public String addBusiness(Model model) {
	    model.addAttribute("businessModel", new BusinessModel());
	    return ADD_BUSINESS_VIEW;
	}
	
//	@PostMapping("/addBusiness")
//	public String saveAddBusiness(@ModelAttribute BusinessModel businessModel, RedirectAttributes flash) {
//		businessService.addBusiness(businessModel);
//	    flash.addFlashAttribute("success", "Business registered succesfully!");
//	    return "redirect:/business/list";
//	}
	
	@PostMapping("/addBusiness")
	public String addEmpresa(@ModelAttribute("empresa") BusinessModel business,
			@RequestParam("logoImagen") MultipartFile file, @RequestParam("logo") String logoName,
			RedirectAttributes flash) {

		if (business != null) {
			if (business.getName().isEmpty() || business.getAddress().isEmpty() || business.getEmail().isEmpty() || business.getPhone().isEmpty()) {
				flash.addFlashAttribute("error", "Some fields are empty");
				return "redirect:/business/addBusiness";
			} else {
				
				String projectDir = System.getProperty("user.dir");

				
				String uploadDir = projectDir + "/src/main/resources/static/imgs/business/";

				try {
					
					File uploadDirFile = new File(uploadDir);
					if (!uploadDirFile.exists()) {
						uploadDirFile.mkdirs();
					}

					file.transferTo(new File(uploadDir + logoName));
					
				} catch (IOException e) {
					e.printStackTrace();
				}

//				Student student = studentRepository.findByUsername(empresa.getEmail());
//				StudentModel studentBusiness = studentService.entity2model(student);
//				studentBusiness.setEnabled(1);
//				studentBusiness.setRole("ROL_EMPRESA");
//				studentService.updateStudent(studentBusiness);

				business.setLogo(logoName);
				
				businessService.addBusiness(business);
				flash.addFlashAttribute("success", "Business created succesfully");
			}
		}
		return "redirect:/business/list";
	}
	
	@GetMapping("/editBusiness/{businessID}")
	public String editBusiness(@PathVariable("businessID")int businessId,Model model) {
		Business business=businessService.getBusinessById(businessId);
		model.addAttribute("businessModel", business); 
	    return EDIT_BUSINESS_VIEW;
	}
	

	@PostMapping("/editBusiness")
	public String saveEditedBusiness(@ModelAttribute BusinessModel businessModel, RedirectAttributes flash, @RequestParam("logoImagen")MultipartFile file
			,@RequestParam("logo") String logoName) {
	    if (businessModel.getId() > 0) {
	        Business business = businessRepository.findById(businessModel.getId()).orElse(null);

	        if (business != null) {
	            business.setName(businessModel.getName());
	            business.setAddress(businessModel.getAddress());
	            business.setEmail(businessModel.getEmail());
	            business.setPhone(businessModel.getPhone());
	            String projectDir = System.getProperty("user.dir");

				
				String uploadDir = projectDir + "/src/main/resources/static/imgs/business/";

				try {
					
					File uploadDirFile = new File(uploadDir);
					if (!uploadDirFile.exists()) {
						uploadDirFile.mkdirs();
					}

					file.transferTo(new File(uploadDir + logoName));
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				business.setLogo(logoName);

	            Business updatedBusiness = businessService.updateBusiness(business);

	            if (updatedBusiness != null) {
	                flash.addFlashAttribute("success", "Business updated successfully!");
	                System.out.println("Success: Business updated. ID: " + updatedBusiness.getId() + ", Name: " + updatedBusiness.getName());
	            } else {
	                flash.addFlashAttribute("error", "Failed to update Business.");
	                System.out.println("Failed: Unable to update Business.");
	            }
	        } else {
	            flash.addFlashAttribute("error", "Business not found.");
	            System.out.println("Failed: Business not found.");
	        }
	    } else {
	        flash.addFlashAttribute("error", "Invalid Business ID.");
	        System.out.println("Failed: Invalid Business ID.");
	    }

	    return "redirect:/business/list";
	}

	@PostMapping("/deleteBusiness/{businessId}")
	public String delete(@PathVariable("businessId") int businessId, Model model) {
		businessService.deleteBusiness(businessId);
	    
	    return "redirect:/business/list";
	}
	
	
	@GetMapping("/home")
	public String Business(Model model) {
		List<Servicio> servicios = servicioService.getAllServicios();
        model.addAttribute("servicio", servicios);
	    return BUSINESS_HOME_VIEW;
	}

}
