package com.padma.interview.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.padma.interview.dto.EmployeeDTO;
import com.padma.interview.model.Department;
import com.padma.interview.model.Employee;
import com.padma.interview.repository.DepartmentRepository;
import com.padma.interview.repository.EmployeeRepository;
import com.padma.interview.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id));
        
        return convertToDTO(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByDepartmentId(String departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + departmentId));
        
        return department.getEmployees().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = convertToEntity(employeeDTO);
        
        // If department ID is provided, set the department
        if (employeeDTO.getDepartmentId() != null && !employeeDTO.getDepartmentId().isEmpty()) {
            Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + employeeDTO.getDepartmentId()));
            employee.setDepartment(department);
        }
        
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDTO(savedEmployee);
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO) {
        // Check if employee exists
        employeeRepository.findById(employeeDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + employeeDTO.getId()));
        
        Employee employee = convertToEntity(employeeDTO);
        
        // If department ID is provided, set the department
        if (employeeDTO.getDepartmentId() != null && !employeeDTO.getDepartmentId().isEmpty()) {
            Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + employeeDTO.getDepartmentId()));
            employee.setDepartment(department);
        } else {
            employee.setDepartment(null);
        }
        
        Employee updatedEmployee = employeeRepository.save(employee);
        return convertToDTO(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployee(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id));
        
        employeeRepository.delete(employee);
    }
    
    // Convert Entity to DTO
    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setPosition(employee.getPosition());
        dto.setSalary(employee.getSalary());
        
        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
            dto.setDepartmentName(employee.getDepartment().getName());
        }
        
        return dto;
    }
    
    // Convert DTO to Entity
    private Employee convertToEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setPosition(dto.getPosition());
        employee.setSalary(dto.getSalary());
        
        // Department is set separately to avoid circular dependencies
        
        return employee;
    }
} 