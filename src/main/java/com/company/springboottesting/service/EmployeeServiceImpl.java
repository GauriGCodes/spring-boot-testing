package com.company.springboottesting.service;

import com.company.springboottesting.exception.EmployeeNotFound;
import com.company.springboottesting.model.Employee;
import com.company.springboottesting.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        // Checks if Email exists on database
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (optionalEmployee.isEmpty()) {
            return employeeRepository.save(employee);
        } else {
            throw new EmployeeNotFound("Employee with email: " + employee.getEmail() + " already exists.. ");
        }
    }

    @Override
    public Employee updateEmployee(Employee employee){
        Employee employee1 = employeeRepository.findById(employee.getId()).orElseThrow(()->new EmployeeNotFound("Employee with ID:- "+employee.getId()+" not found "));

        employee1.setEmail(employee.getEmail());
        employee1.setFirstName(employee.getFirstName());
        employee1.setLastName(employee.getLastName());

        return employeeRepository.save(employee1);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(()-> new EmployeeNotFound("Employee with ID:- "+id+" not found "));
    }

    @Override
    public void deleteEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isPresent()) employeeRepository.delete(employee.get());
        else{
            throw new EmployeeNotFound("Employee with ID:- "+id+" not found ");
        }
    }

}
