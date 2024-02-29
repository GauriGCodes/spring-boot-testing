package com.company.springboottesting.service;

import com.company.springboottesting.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    Employee updateEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Employee findEmployeeById(Long id);
    void deleteEmployee(Long id);
}
