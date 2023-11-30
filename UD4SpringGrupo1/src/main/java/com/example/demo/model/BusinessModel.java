package com.example.demo.model;

import java.util.List;

import com.example.demo.entity.Servicio;

import lombok.Data;

@Data
public class BusinessModel {
	
	private int id;
	private String name, address,phone, email, logo;
	private List<Servicio> servicioList;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public List<Servicio> getServicioList() {
		return servicioList;
	}

	public void setServicioList(List<Servicio> servicioList) {
		this.servicioList = servicioList;
	}

	public BusinessModel(int id, String name, String address, String phone, String email, String logo,
			List<Servicio> servicioList) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.email = email;
		this.logo = logo;
		this.servicioList = servicioList;
	}

	public BusinessModel() {
		super();
	}
	
}