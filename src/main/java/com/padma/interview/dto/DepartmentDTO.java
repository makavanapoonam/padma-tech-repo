package com.padma.interview.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

public class DepartmentDTO {

    private String id;
    
    @NotBlank(message = "Name cannot be blank")
    private String name;
    
    @NotBlank(message = "Location cannot be blank")
    private String location;
    
    private List<EmployeeDTO> employees = new ArrayList<>();
    
    // Default constructor
    public DepartmentDTO() {
    }
    
    // Parameterized constructor
    public DepartmentDTO(String id, String name, String location, List<EmployeeDTO> employees) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.employees = employees;
    }
    
    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<EmployeeDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeDTO> employees) {
        this.employees = employees;
    }
} 