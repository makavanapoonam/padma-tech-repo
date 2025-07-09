package com.padma.interview.service;

import java.util.List;

import com.padma.interview.dto.EmployeeDTO;

public interface EmployeeService {
    
    List<EmployeeDTO> getAllEmployees();
    
    EmployeeDTO getEmployeeById(String id);
    
    List<EmployeeDTO> getEmployeesByDepartmentId(String departmentId);
    
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    
    EmployeeDTO updateEmployee(EmployeeDTO employeeDTO);
    
    void deleteEmployee(String id);
} 