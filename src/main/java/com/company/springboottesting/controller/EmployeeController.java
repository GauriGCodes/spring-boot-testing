package com.company.springboottesting.controller;

import com.company.springboottesting.model.Employee;
import com.company.springboottesting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employees/v1/")
public class EmployeeController {
    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService){
        this.employeeService=employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeService.saveEmployee(employee);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Employee getEmployeeById(@PathVariable(name = "id") Long id){
        return employeeService.findEmployeeById(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Employee updateEmployee(@RequestBody Employee employee){
        return employeeService.updateEmployee(employee);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable(name = "id") Long id){
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>("Employee with ID: "+id+" deleted",HttpStatus.OK);
    }
}
