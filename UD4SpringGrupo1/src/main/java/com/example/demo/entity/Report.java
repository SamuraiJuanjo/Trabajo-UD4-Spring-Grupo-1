package com.example.demo.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Date fullDate;
    private int serviceTime;
    private String report;

    @ManyToOne
    @JoinColumn(name = "studentId")
    private int studentId;

    @OneToOne
    @JoinColumn(name = "servicioId")
    private int servicioId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getFullDate() {
		return fullDate;
	}

	public void setFullDate(Date fullDate) {
		this.fullDate = fullDate;
	}

	public int getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(int serviceTime) {
		this.serviceTime = serviceTime;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getServicioId() {
		return servicioId;
	}

	public void setServicioId(int servicioId) {
		this.servicioId = servicioId;
	}

	public Report(int id, Date fullDate, int serviceTime, String report, int studentId, int servicioId) {
		super();
		this.id = id;
		this.fullDate = fullDate;
		this.serviceTime = serviceTime;
		this.report = report;
		this.studentId = studentId;
		this.servicioId = servicioId;
	}

	public Report() {
		super();
	}
    
}
