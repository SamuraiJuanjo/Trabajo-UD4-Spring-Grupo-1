package com.example.demo.controller;

import java.sql.Date;
import java.time.LocalDate;
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
import com.example.demo.entity.Servicio;
import com.example.demo.model.ServicioModel;
import com.example.demo.repository.ProFamilyRepository;
import com.example.demo.repository.ServicioRepository;
import com.example.demo.service.ProFamilyService;
import com.example.demo.service.ServicioService;

@Controller
@RequestMapping("/servicio")
public class ServicioController {

	private static final String ADD_SERVICIO_VIEW = "business/addServicio";
	private static final String EDIT_SERVICIO_VIEW = "business/editServicio";
	
	@Autowired
	@Qualifier("servicioService")
	 private ServicioService servicioService;
	
	@Autowired
	@Qualifier("proFamilyService")
    private ProFamilyService proFamilyService;
	
	@Autowired
	@Qualifier("proFamilyRepository")
	 private ProFamilyRepository proFamilyRepository;
	
	@Autowired
	@Qualifier("servicioRepository")
	 private ServicioRepository servicioRepository;
	
	@GetMapping("/addServicio")
	public String addServicio(Model model) {
		
//		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        int userId = obtenerIdUsuarioPorNombre(username);
//        List<Servicio> servicios = servicioService.obtenerServiciosPorUsuario(userId);
	    List<ProFamily> profesionalFamilies = proFamilyRepository.findAll();
	    model.addAttribute("servicioModel", new ServicioModel());
	    model.addAttribute("profesionalFamilies", profesionalFamilies);
	    // Puedes configurar la fecha actual aquí antes de mostrar el formulario
//	    LocalDate currentDate = LocalDate.now();
//	    model.addAttribute("currentDate", currentDate);
	    return ADD_SERVICIO_VIEW;
	}

	@PostMapping("/addServicio")
	public String addServicioPost(@ModelAttribute ServicioModel servicioModel, Model model) {
	    // Puedes configurar la fecha actual antes de guardarla en la base de datos
//	    servicioModel.setRegisterDate(new Date(System.currentTimeMillis()));
//	    servicioService.addServicio(servicioModel);
		 model.addAttribute("servicioModel", new ServicioModel());
		 servicioService.addServicio(servicioModel);
	    return "redirect:/business/home"; // Redirige a donde sea necesario después de guardar
	}



	
	@GetMapping("/editServicio/{servicioId}")
	public String editStudent(@PathVariable("servicioId") int servicioId, Model model) {
		List<ProFamily> proFamilyList = proFamilyRepository.findAll();
		model.addAttribute("profesionalFamilies", proFamilyList); 
		Servicio servicio = servicioRepository.findById(servicioId);
		model.addAttribute("servicio", servicio); 
	    return EDIT_SERVICIO_VIEW;
	}
	
	@PostMapping("/editServicio")
    public String saveEditedServicio(@ModelAttribute ServicioModel servicioModel) {
		servicioService.updateServicio(servicioModel);
        return "redirect:/business/home";
    }
	
	@PostMapping("/deleteServicio/{servicioId}")
	public String delete(@PathVariable("servicioId") int servicioId, Model model) {
		servicioService.deleteServicio(servicioId);
	    return "redirect:/business/home";
	}
	
}
