package com.company.springboottesting.repository;

import com.company.springboottesting.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp(){
        this.employee = Employee.builder().firstName("Tarang").lastName("Gupta").email("taranggupta4@gmail.com").build();
    }


    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){
        Employee savedEmployee = employeeRepository.save(employee);
        assertThat(savedEmployee).isNotNull();
       // Assertions.assertThat(savedEmployee.getId()).isEqualTo(1L);
        List<Employee> getEmployees = employeeRepository.findAll();
        assertThat(getEmployees.size()).isEqualTo(1);
    }

    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList(){
        // Save Some dummy data
        Employee employee1 = Employee.builder().firstName("Gauri").lastName("Gupta").email("gouri88gupta@gmail.com").build();
        Employee employee2 = Employee.builder().firstName("Sanjay").lastName("Gupta").email("sanjaygupta9@gmail.com").build();
        employeeRepository.save(employee);
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        List<Employee> getEmployees = employeeRepository.findAll();
        assertThat(getEmployees.get(0).getEmail()).isEqualTo("taranggupta4@gmail.com");
       // Assertions.assertThat(getEmployees.get(1).getEmail()).isEqualTo("taranggupta4@gmail.com");
        assertThat(getEmployees.get(2).getFirstName()).isEqualTo("Sanjay");
    }

    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject(){
        Employee savedEmployee = employeeRepository.save(employee);

        Employee getEmployee = employeeRepository.findById(savedEmployee.getId()).orElseThrow(()->new RuntimeException("Employee with ID: "+employee.getId()+" not found"));
        assertThat(getEmployee).isNotNull();
        assertThat(getEmployee.getFirstName()).isEqualTo("Tarang");
    }

    @Test
    public void givenEmployeeObject_whenFindByEmail_thenReturnEmployeeObject(){
        employeeRepository.save(employee);

        String email = "taranggupta4@gmail.com";
        Employee getEmployee = employeeRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("Employee with email: "+email+" not found"));
        assertThat(getEmployee).isNotNull();
        assertThat(getEmployee.getFirstName()).isEqualTo("Tarang");
    }

    @Test
    public void givenEmployeeObject_whenUpdateEmployeeObject_thenReturnEmployeeObject(){
        Employee savedEmployee = employeeRepository.save(employee);

        savedEmployee.setEmail("JohnDoe5@gmail.com");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        // Updated the Email
        assertThat(updatedEmployee.getEmail()).isEqualTo("JohnDoe5@gmail.com");
    }

    @Test
    public void givenEmployeeObject_whenDeleteEmployeeObject_thenRemoveEmployee(){
        employeeRepository.save(employee);
        employeeRepository.delete(employee);

        //Find By ID should be null now
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());
        assertThat(employeeOptional).isEmpty();
    }

    @Test
    public void givenFirstNameSecondName_whenFindByFirstNameSecondName_thenReturnEmployee(){
        employeeRepository.save(employee);
        Employee employee1 = employeeRepository.findByFirstNameAndSecondName("Tarang","Gupta");
        assertThat(employee1).isNotNull();

        Employee employee2 = employeeRepository.findByFirstNameAndSecondName("Johns","Doe");
        assertThat(employee2).isNull();
    }

    @Test
    public void givenFirstNameSecondNameNamedParameter_whenFindByFirstNameSecondName_thenReturnEmployee(){
        employeeRepository.save(employee);
        Employee employee1 = employeeRepository.findByFirstNameAndSecondNameNamedParameters("Tarang","Gupta");
        assertThat(employee1).isNotNull();

        Employee employee2 = employeeRepository.findByFirstNameAndSecondNameNamedParameters("Johns","Doe");
        assertThat(employee2).isNull();
    }

    @Test
    public void givenFirstNameSecondNameIndexedParameterNativeSQL_whenFindByFirstNameSecondName_thenReturnEmployee(){
        employeeRepository.save(employee);
        Employee employee1 = employeeRepository.findByNativeSQl("Tarang","Gupta");
        assertThat(employee1).isNotNull();

        Employee employee2 = employeeRepository.findByNativeSQl("Johns","Doe");
        assertThat(employee2).isNull();
    }

    @Test
    public void givenFirstNameSecondNameNativeParameterNativeSQL_whenFindByFirstNameSecondName_thenReturnEmployee(){
        employeeRepository.save(employee);
        Employee employee1 = employeeRepository.findByNativeSQlNamedParameter("Tarang","Gupta");
        assertThat(employee1).isNotNull();

        Employee employee2 = employeeRepository.findByNativeSQlNamedParameter("Johns","Doe");
        assertThat(employee2).isNull();
    }

}
