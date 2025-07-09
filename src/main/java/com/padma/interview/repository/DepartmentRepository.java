package com.padma.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.padma.interview.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
} 