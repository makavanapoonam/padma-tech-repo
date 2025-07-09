package com.padma.interview.service.implementation;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.padma.interview.dto.DepartmentDTO;
import com.padma.interview.dto.EmployeeDTO;
import com.padma.interview.model.Department;
import com.padma.interview.model.Employee;
import com.padma.interview.repository.DepartmentRepository;
import com.padma.interview.repository.EmployeeRepository;
import com.padma.interview.service.DepartmentService;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentById(String id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + id));
        
        return convertToDTO(department);
    }

    @Override
    @Transactional
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        Department department = convertToEntity(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);
        return convertToDTO(savedDepartment);
    }

    @Override
    @Transactional
    public DepartmentDTO updateDepartment(DepartmentDTO departmentDTO) {
        // Check if department exists
        departmentRepository.findById(departmentDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + departmentDTO.getId()));
        
        Department department = convertToEntity(departmentDTO);
        Department updatedDepartment = departmentRepository.save(department);
        return convertToDTO(updatedDepartment);
    }

    @Override
    @Transactional
    public void deleteDepartment(String id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + id));
        
        departmentRepository.delete(department);
    }
    
    @Override
    @Transactional
    public DepartmentDTO assignEmployeeToDepartment(String departmentId, String employeeId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + departmentId));
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + employeeId));
        
        employee.setDepartment(department);
        employeeRepository.save(employee);
        
        // Refresh department to get updated employees
        department = departmentRepository.findById(departmentId).get();
        
        return convertToDTO(department);
    }
    
    @Override
    @Transactional
    public DepartmentDTO removeEmployeeFromDepartment(String departmentId, String employeeId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + departmentId));
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + employeeId));
        
        // Check if employee belongs to this department
        if (employee.getDepartment() == null || !employee.getDepartment().getId().equals(departmentId)) {
            throw new IllegalStateException("Employee with ID " + employeeId + " is not assigned to department with ID " + departmentId);
        }
        
        employee.setDepartment(null);
        employeeRepository.save(employee);
        
        // Refresh department to get updated employees
        department = departmentRepository.findById(departmentId).get();
        
        return convertToDTO(department);
    }
    
    @Override
    public byte[] generateDepartmentReport() {
        try {
            // Create a simple PDF document directly without a template
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            // Get all departments
            List<Department> departments = departmentRepository.findAll();
            List<Map<String, Object>> dataList = new ArrayList<>();
            
            // Create data for the report
            for (Department dept : departments) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", dept.getId());
                data.put("name", dept.getName());
                data.put("location", dept.getLocation());
                data.put("employeeCount", dept.getEmployees().size());
                dataList.add(data);
            }
            
            // Create a data source for the report
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);
            
            // Load the report template
            JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("/reports/department_report.jrxml"));
            
            // Compile the report
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            // Fill the report with data
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("title", "Department Report");
            parameters.put("generatedDate", new java.util.Date());
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            
            // Export to PDF
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating department report: " + e.getMessage(), e);
        }
    }
    
    // Convert Entity to DTO
    private DepartmentDTO convertToDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setLocation(department.getLocation());
        
        // Convert employees to DTOs
        List<EmployeeDTO> employeeDTOs = department.getEmployees().stream()
                .map(this::convertEmployeeToDTO)
                .collect(Collectors.toList());
        
        dto.setEmployees(employeeDTOs);
        return dto;
    }
    
    // Convert DTO to Entity
    private Department convertToEntity(DepartmentDTO dto) {
        Department department = new Department();
        department.setId(dto.getId());
        department.setName(dto.getName());
        department.setLocation(dto.getLocation());
        
        // We don't set employees here to avoid circular dependencies
        // Employee relationships are managed in the EmployeeService
        
        return department;
    }
    
    // Convert Employee Entity to DTO
    private EmployeeDTO convertEmployeeToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setPosition(employee.getPosition());
        dto.setSalary(employee.getSalary());
        return dto;
    }
} 