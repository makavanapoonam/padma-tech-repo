package com.padma.interview.controller;

import com.padma.interview.dto.DepartmentDTO;
import com.padma.interview.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "*")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;


    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable String id) {
        DepartmentDTO department = departmentService.getDepartmentById(id);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO createdDepartment = departmentService.createDepartment(departmentDTO);
        return new ResponseEntity<>(createdDepartment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable String id, @RequestBody DepartmentDTO departmentDTO) {
        departmentDTO.setId(id);
        DepartmentDTO updatedDepartment = departmentService.updateDepartment(departmentDTO);
        return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable String id) {
        departmentService.deleteDepartment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{departmentId}/employees/{employeeId}")
    public ResponseEntity<DepartmentDTO> assignEmployeeToDepartment(
            @PathVariable String departmentId,
            @PathVariable String employeeId) {
        DepartmentDTO updatedDepartment = departmentService.assignEmployeeToDepartment(departmentId, employeeId);
        return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
    }

    @DeleteMapping("/{departmentId}/employees/{employeeId}")
    public ResponseEntity<DepartmentDTO> removeEmployeeFromDepartment(
            @PathVariable String departmentId,
            @PathVariable String employeeId) {
        DepartmentDTO updatedDepartment = departmentService.removeEmployeeFromDepartment(departmentId, employeeId);
        return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> generateDepartmentReport() {
        byte[] reportBytes = departmentService.generateDepartmentReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "department_report.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        headers.setContentLength(reportBytes.length);

        return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
    }
} 