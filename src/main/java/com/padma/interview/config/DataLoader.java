package com.padma.interview.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.padma.interview.model.Department;
import com.padma.interview.model.Employee;
import com.padma.interview.repository.DepartmentRepository;
import com.padma.interview.repository.EmployeeRepository;

@Component
public class DataLoader {

    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @PostConstruct
    @Transactional
    public void loadData() {
        // Create departments
        Department hrDepartment = new Department();
        hrDepartment.setId("dept01");
        hrDepartment.setName("Human Resources");
        hrDepartment.setLocation("Building A");
        
        Department engineeringDepartment = new Department();
        engineeringDepartment.setId("dept02");
        engineeringDepartment.setName("Engineering");
        engineeringDepartment.setLocation("Building B");
        
        // Create employees
        Employee alice = new Employee();
        alice.setId("emp001");
        alice.setName("Alice Smith");
        alice.setEmail("alice.smith@example.com");
        alice.setPosition("Recruiter");
        alice.setSalary(60000.0);
        
        Employee charlie = new Employee();
        charlie.setId("emp003");
        charlie.setName("Charlie Brown");
        charlie.setEmail("charlie.brown@example.com");
        charlie.setPosition("HR Assistant");
        charlie.setSalary(40000.0);
        
        Employee bob = new Employee();
        bob.setId("emp002");
        bob.setName("Bob Johnson");
        bob.setEmail("bob.johnson@example.com");
        bob.setPosition("Software Engineer");
        bob.setSalary(80000.0);
        
        Employee diana = new Employee();
        diana.setId("emp004");
        diana.setName("Diana Prince");
        diana.setEmail("diana.prince@example.com");
        diana.setPosition("System Architect");
        diana.setSalary(90000.0);
        
        // Add employees to departments
        hrDepartment.addEmployee(alice);
        hrDepartment.addEmployee(charlie);
        engineeringDepartment.addEmployee(bob);
        engineeringDepartment.addEmployee(diana);
        
        // Save departments (cascade will save employees)
        departmentRepository.save(hrDepartment);
        departmentRepository.save(engineeringDepartment);
    }
} 