package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.ProFamily;
import com.example.demo.repository.ProFamilyRepository;
import com.example.demo.service.ProFamilyService;

@Controller
@RequestMapping("proFamily")
public class ProFamilyController {

	private static final String PRO_FAMILY_VIEW = "/admin/proFamily";
	private static final String EDIT_PRO_FAMILY_VIEW = "/admin/editProFamily";
	
	@Autowired
	@Qualifier("proFamilyService")
	 private ProFamilyService proFamilyService;
	
	@Autowired
	@Qualifier("proFamilyRepository")
	 private ProFamilyRepository proFamilyRepository;
	
	@GetMapping("/list")
	public String proFamily(Model model) {
		List<ProFamily> proFamilyList = proFamilyRepository.findAll();
		model.addAttribute("profesionalFamilies", proFamilyList); 
	    return PRO_FAMILY_VIEW;
	}
	
	@GetMapping("/editProFamily/{proFamilyId}")
	public String editProFamily(@PathVariable("proFamilyId") int proFamilyId, Model model) {
	    ProFamily proFamily = proFamilyRepository.findById(proFamilyId).orElse(null);
	    model.addAttribute("proFamily", proFamily);
	    return EDIT_PRO_FAMILY_VIEW;
	}

	
	@PostMapping("/editProFamily/{proFamilyId}")
	public String saveEditedProFamily(@PathVariable("proFamilyId") int proFamilyId, @ModelAttribute ProFamily proFamily) {
	    proFamily.setId(proFamilyId);
	    proFamilyService.updateProFamily(proFamily);
	    return "redirect:/proFamily/list";
	}

	@PostMapping("/deleteProFamily/{proFamilyId}")
	public String delete(@PathVariable("proFamilyId") int proFamilyId, Model model) {
		proFamilyService.deleteProFamily(proFamilyId);
	    
	    return "redirect:/proFamily/list";
	}

}