package com.padma.interview.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.padma.interview.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    
    List<Employee> findByDepartmentId(String departmentId);
} 