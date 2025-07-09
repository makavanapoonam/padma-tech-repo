package com.padma.interview.service;

import java.util.List;

import com.padma.interview.dto.DepartmentDTO;

public interface DepartmentService {
    
    List<DepartmentDTO> getAllDepartments();
    
    DepartmentDTO getDepartmentById(String id);
    
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);
    
    DepartmentDTO updateDepartment(DepartmentDTO departmentDTO);
    
    void deleteDepartment(String id);
    
    byte[] generateDepartmentReport();
    
    DepartmentDTO assignEmployeeToDepartment(String departmentId, String employeeId);
    
    DepartmentDTO removeEmployeeFromDepartment(String departmentId, String employeeId);
} 