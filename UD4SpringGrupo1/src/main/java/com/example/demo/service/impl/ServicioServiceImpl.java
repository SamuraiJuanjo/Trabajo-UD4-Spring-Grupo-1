package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Business;
import com.example.demo.entity.ProFamily;
import com.example.demo.entity.Report;
import com.example.demo.entity.Servicio;
import com.example.demo.entity.Student;
import com.example.demo.model.ServicioModel;
import com.example.demo.repository.ReportRepository;
import com.example.demo.repository.ServicioRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.ProFamilyService;
import com.example.demo.service.ServicioService;

@Configuration
@Service("servicioService")
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;
    
    @Autowired
    private ReportRepository reportRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private ProFamilyService proFamilyService;

    public ServicioServiceImpl(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }
    
    public Servicio model2entity(ServicioModel servicioModel) {
		ModelMapper mapper = new ModelMapper();
		return mapper.map(servicioModel, Servicio.class);
	}

	public ServicioModel entity2model(Servicio servicio) {
		ModelMapper mapper = new ModelMapper();
		return mapper.map(servicio, ServicioModel.class);
	}
	
	@Override
	public Servicio addServicio(ServicioModel servicioModel) {
		Servicio servicio = model2entity(servicioModel);
		servicio.setTitle(servicioModel.getTitle());
		servicio.setDescription(servicioModel.getDescription());
		servicio.setHappeningDate(servicioModel.getHappeningDate());
		servicio.setRegisterDate(servicioModel.getRegisterDate());
		servicio.setId(servicioModel.getId());
		servicio.setBusinessId(servicioModel.getBusinessId());
		servicio.setProfesionalFamilyId(servicioModel.getProfesionalFamilyId());
		servicio.setStudentId(servicioModel.getStudentId());
		servicio.setValoration(0);
		servicio.setComment(null);
		servicio.setDeleted(0);
		servicio.setFinished(0);
		return servicioRepository.save(servicio);	
	}

	@Override
	public int deleteServicio(int id) {
		Servicio servicio = servicioRepository.findById(id);

        if (servicio.getDeleted() == 0) {
        	servicio.setDeleted(1);
        	servicioRepository.save(servicio);
            return 1;
        } else
            return 0;
	}

	@Override
	public Servicio updateServicio(ServicioModel servicioModel) {
	    Servicio servicio = servicioRepository.findById(servicioModel.getId());
	    servicio.setTitle(servicioModel.getTitle());
	    servicio.setDescription(servicioModel.getDescription());
	    servicio.setHappeningDate(servicioModel.getHappeningDate());
	    servicio.setRegisterDate(servicioModel.getRegisterDate());
	    servicio.setId(servicioModel.getId());
	    servicio.setBusinessId(servicioModel.getBusinessId());
	    servicio.setProfesionalFamilyId(servicioModel.getProfesionalFamilyId());
	    servicio.setStudentId(servicioModel.getStudentId());
	    
	    servicio.setDeleted(0);
	    return servicioRepository.save(servicio);
	}
	
	@Override
	public Report createReportByServicioId(int servicioId, String reportText, int serviceTime, int studentId) {
	    Servicio servicio = servicioRepository.findById(servicioId);

	    if (servicio != null) {
	        Report report = new Report();
	        report.setServiceTime(serviceTime);
	        report.setReport(reportText);
	        Student student = studentRepository.findById(studentId);

	        if (student != null) {
	            report.setStudentId(student);
	        }

	        report.setServicioId(servicio);
	        servicio.setFinished(1);
	        updateServicio(entity2model(servicio));
	        return reportRepository.save(report);
	    }
	    return null; 
	}



	@Override
	public Servicio rateServicio(int servicioId, float valoracion) {
	    Servicio servicio = servicioRepository.findById(servicioId);
	    servicio.setValoration(valoracion);
	    return servicioRepository.save(servicio);
	}
	
	@Override
	public Servicio finishServicio(int servicioId) {
	    Servicio servicio = servicioRepository.findById(servicioId);
	    servicio.setFinished(1);
	    return servicioRepository.save(servicio);
	}
	
	@Override
	public Servicio commentServicio(int servicioId, String comment) {
	    Servicio servicio = servicioRepository.findById(servicioId);
	    servicio.setComment(comment);
	    return servicioRepository.save(servicio);
	}
	
	@Override
	public List<ServicioModel> getFinishedServicios() {
	    List<Servicio> finishedServicios = servicioRepository.findByFinished(1);
	    List<ServicioModel> servicioModels = new ArrayList<>();

	    for (Servicio servicio : finishedServicios) {
	        ServicioModel servicioModel = entity2model(servicio);
	        servicioModels.add(servicioModel);
	    }

	    return servicioModels;
	}
	
	@Override
	public List<ServicioModel> getUnassignedServicios() {
	    List<Servicio> unassignedServicios = servicioRepository.findByStudentIdIsNull();
	    List<ServicioModel> servicioModels = new ArrayList<>();

	    for (Servicio servicio : unassignedServicios) {
	        ServicioModel servicioModel = entity2model(servicio);
	        servicioModels.add(servicioModel);
	    }

	    return servicioModels;
	}
	
	@Override
	public List<ServicioModel> getAssignedButUncompletedServices() {
	    List<Servicio> assignedButUncompletedServices = servicioRepository.findByStudentIdIsNotNullAndFinishedIsNot(1);
	    List<ServicioModel> servicioModels = new ArrayList<>();

	    for (Servicio servicio : assignedButUncompletedServices) {
	        ServicioModel servicioModel = entity2model(servicio);
	        servicioModels.add(servicioModel);
	    }

	    return servicioModels;
	}


	@Override
	public List<ServicioModel> findServiciosByProFamily(String familyName) {
	    List<ServicioModel> servicioModels = new ArrayList<>();

	    for (Servicio servicio : servicioRepository.findAll()) {
	        if (servicio.getProfesionalFamilyId() != null && servicio.getProfesionalFamilyId().getName().equals(familyName)) {
	            servicioModels.add(entity2model(servicio));
	        }
	    }

	    return servicioModels;
	}

	@Override
	public List<ServicioModel> getAllServicios() {
	    List<Servicio> servicios = servicioRepository.findAll();
	    List<ServicioModel> servicioModels = new ArrayList<>();

	    for (Servicio servicio : servicios) {
	        ServicioModel servicioModel = entity2model(servicio);
	        servicioModels.add(servicioModel);
	    }

	    return servicioModels;
	}

	@Override
	public List<ServicioModel> getServicesByBusinessId(Business business) {
		List<Servicio>modelServices=servicioRepository.findAll();
		List<ServicioModel>services=new ArrayList<>();
		for(Servicio servicio: modelServices) {
			if(servicio.getBusinessId()!=null && servicio.getBusinessId().getId() == business.getId()) {
			ServicioModel servicioModel = entity2model(servicio);
			services.add(servicioModel);
			}
		}
		
		return services;
		
	}

	@Override
    public List<Report> getReportsForServicesByBusinessId(Business business) {
        List<ServicioModel> services = getServicesByBusinessId(business);
        List<Report> reports = new ArrayList<>();

        for (ServicioModel servicioModel : services) {
            List<Report> reportsForService = reportRepository.findAllByServicioId(model2entity(servicioModel));
            reports.addAll(reportsForService);
        }

        return reports;
    }

	@Override
	public Servicio getServicioById(int serviceId) {
        return servicioRepository.findById(serviceId);
    }

	public List<ProFamily> getProfessionalFamiliesByBusinessId(List<Servicio>listServices){
		List<ProFamily>reports=new ArrayList<>();
		for(Servicio servicio: listServices) {
			reports.add(servicio.getProfesionalFamilyId());
		}
		Collections.sort(reports, Comparator.comparing(ProFamily::getName));
		return reports;
	}

	@Override
	public List<ServicioModel> getFinishedServiciosByProFamily(String familyName) {
	    List<ServicioModel> servicioModels = new ArrayList<>();

	    List<Servicio> finishedServicios = servicioRepository.findByFinished(1);

	    for (Servicio servicio : finishedServicios) {
	        if (servicio.getProfesionalFamilyId() != null && servicio.getProfesionalFamilyId().getName().equals(familyName)) {
	            servicioModels.add(entity2model(servicio));
	        }
	    }

	    return servicioModels;
	}
	
	@Override
	public List<ServicioModel> getUnassignedServiciosByProFamily(String familyName) {
	    List<ServicioModel> servicioModels = new ArrayList<>();

	    List<Servicio> finishedServicios = servicioRepository.findByStudentIdIsNull();

	    for (Servicio servicio : finishedServicios) {
	        if (servicio.getProfesionalFamilyId() != null && servicio.getProfesionalFamilyId().getName().equals(familyName)) {
	            servicioModels.add(entity2model(servicio));
	        }
	    }

	    return servicioModels;
	}
	
	@Override
	public List<ServicioModel> getAssignedButUncompletedServiciosByProFamily(String familyName) {
	    List<ServicioModel> servicioModels = new ArrayList<>();

	    List<Servicio> finishedServicios = servicioRepository.findByStudentIdIsNotNullAndFinishedIsNot(1);

	    for (Servicio servicio : finishedServicios) {
	        if (servicio.getProfesionalFamilyId() != null && servicio.getProfesionalFamilyId().getName().equals(familyName)) {
	            servicioModels.add(entity2model(servicio));
	        }
	    }

	    return servicioModels;
	}

	@Override
	public Servicio assignStudent(int servicioId, int studentId) {
		Servicio servicio = servicioRepository.findById(servicioId);
		servicio.setStudentId(studentRepository.findById(studentId));
		 return servicioRepository.save(servicio);
	}

	@Override
	public List<ServicioModel> findByValorationIsNotNullAndBusinessIdAndProfesionalFamilyId(Business business,
			ProFamily profamily) {
		List<ServicioModel>listModel=new ArrayList<>();
		List<Servicio>entityList=servicioRepository.findByValorationIsNotNullAndBusinessIdAndProfesionalFamilyId(business, profamily);
		for(Servicio servicio: entityList) {
			listModel.add(entity2model(servicio));
		}
		return listModel;
	}

	@Override
	public List<ServicioModel> findByFinishedAndStudentId(int id, Student student) {
		List<ServicioModel>modelList=new ArrayList<>();
		List<Servicio>entityList=servicioRepository.findByFinishedAndStudentId(1, student);
		System.out.println("Que imprime esto: "+entityList);
		for(Servicio servicio: entityList) {
			modelList.add(entity2model(servicio));
		}
		return modelList;
	}

	@Override
	public List<ServicioModel> getFilteredServices(String opcion, String filterBy,Date startDate, Date endDate) {
		List<ServicioModel> listServicios = new ArrayList<>();

	    if (!filterBy.equals("null")) {
	        if (filterBy.equals("finishedServices")) {
	            if (Integer.parseInt(opcion) != 0) {
	                ProFamily profam = proFamilyService.findById(Integer.parseInt(opcion));
	                String proFamName = profam.getName();
	                listServicios = getFinishedServiciosByProFamily(proFamName);
	            } else {
	                listServicios = getFinishedServicios();
	            }
	        } else if (filterBy.equals("asignados_no_realizados")) {
	            if (Integer.parseInt(opcion) != 0) {
	                ProFamily profam = proFamilyService.findById(Integer.parseInt(opcion));
	                String proFamName = profam.getName();
	                listServicios = getAssignedButUncompletedServiciosByProFamily(proFamName);
	            } else {
	                listServicios = getAssignedButUncompletedServices();
	            }
	        } else if (filterBy.equals("no_asignados")) {
	            if (Integer.parseInt(opcion) != 0) {
	                ProFamily profam = proFamilyService.findById(Integer.parseInt(opcion));
	                String proFamName = profam.getName();
	                listServicios = getUnassignedServiciosByProFamily(proFamName);
	            } else {
	                listServicios = getUnassignedServicios();
	            }
	        }else if(filterBy.equals("all")) {
	        	listServicios = getAllServicios();
	        }
	    } else if (Integer.parseInt(opcion) != 0) {
	        ProFamily profam = proFamilyService.findById(Integer.parseInt(opcion));
	        String proFamName = profam.getName();
	        listServicios = findServiciosByProFamily(proFamName);
	    } else {
	        listServicios = getAllServicios();
	    }
	    
	    if (startDate != null && endDate != null) {
	        listServicios = listServicios.stream()
	                .filter(servicio -> servicio.getRegisterDate().after(startDate) && servicio.getRegisterDate().before(endDate))
	                .collect(Collectors.toList());
	    }

	    return listServicios;
	}

	public List<ServicioModel> getServicesByTwoDates(Date registerDateBegin, Date registerDateEnd) {
		List<Servicio>listService=servicioRepository.findByHappeningDateBetween(registerDateBegin, registerDateEnd);
		List<ServicioModel>listServiceModel=new ArrayList<>();
		for(Servicio servicio:listService) {
			listServiceModel.add(entity2model(servicio));
		}
		return listServiceModel;
	}

}