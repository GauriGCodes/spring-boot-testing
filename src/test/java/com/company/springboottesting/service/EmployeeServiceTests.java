package com.company.springboottesting.service;

import com.company.springboottesting.exception.EmployeeNotFound;
import com.company.springboottesting.model.Employee;
import com.company.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    // Mockito Set-up
    @BeforeEach
    public void setup(){
        this.employee = Employee.builder().firstName("Jack").lastName("Harlow").email("jackHarlow@gmail.com").build();
    }

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
        Mockito.when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.empty());
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);
        assertThat(employeeService.saveEmployee(employee)).isNotNull();

        //Test the negative scenario
        Mockito.when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));
        assertThatThrownBy(()->employeeService.saveEmployee(employee)).hasMessage("Employee with email: " + employee.getEmail() + " already exists.. ");
    }


    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList(){
        Employee employee1 = Employee.builder().firstName("Rekha").lastName("Johns").email("rjohns@gmail.com").build();
        Employee employee2 = Employee.builder().firstName("Tara").lastName("Vijay").email("tvijay@gmail.com").build();

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        employeeList.add(employee1);
        employeeList.add(employee2);

        Mockito.when(employeeRepository.findAll()).thenReturn(employeeList);
        assertThat(employeeService.getAllEmployees().size()).isEqualTo(employeeList.size());
        assertThat(employeeService.getAllEmployees().get(2).getFirstName()).isEqualTo("Tara");

        //Invalid Test Case
        //Mockito.when(employeeRepository.findAll()).thenReturn(null);
       // assertThat(employeeService.getAllEmployees().size()).isEqualTo(employeeList.size());
    }

    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject(){
        Mockito.when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        assertThat(employeeService.findEmployeeById(employee.getId())).isNotNull();

        //Negative Scenario
        Mockito.when(employeeRepository.findById(19L)).thenThrow(new EmployeeNotFound("Employee with ID: "+19+" Not Found.."));
        org.junit.jupiter.api.Assertions.assertThrows(EmployeeNotFound.class,() -> employeeService.findEmployeeById(19L));
    }

    @Test
    public void givenEmployeeObject_whenUpdateEmployeeObject_thenReturnEmployeeObject(){
        employee.setId(3L);
        Employee updatedEmployee = Employee.builder().firstName("Jack").lastName("Harlow").email("jackHarlow7@gmail.com").build();
        updatedEmployee.setId(employee.getId());

       // Mockito.when(employeeRepository.findById(updatedEmployee.getId())).thenReturn(Optional.of(updatedEmployee));
       // Mockito.when(employeeRepository.save(updatedEmployee)).thenReturn(updatedEmployee);

       // assertThat(employeeService.updateEmployee(updatedEmployee).getEmail()).isEqualTo("jackHarlow7@gmail.com");

        // Negative Test Case Scenario
        Mockito.when(employeeRepository.findById(updatedEmployee.getId())).thenThrow(new EmployeeNotFound("Employee with ID: "+updatedEmployee.getId()+" Not Found.."));
        org.junit.jupiter.api.Assertions.assertThrows(EmployeeNotFound.class,() -> employeeService.updateEmployee(updatedEmployee));
        Mockito.verify(employeeRepository,Mockito.never()).save(updatedEmployee);
    }

    @Test
    public void givenEmployeeObject_whenDeleteEmployeeObject_thenRemoveEmployee(){
        // Positive Scenario (Object Found in the Repo)
        Mockito.when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        employeeService.deleteEmployee(employee.getId());
        Mockito.verify(employeeRepository,Mockito.times(1)).delete(employee);

    }

}
