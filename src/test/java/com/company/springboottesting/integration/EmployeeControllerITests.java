package com.company.springboottesting.integration;

import com.company.springboottesting.exception.EmployeeNotFound;
import com.company.springboottesting.model.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class EmployeeControllerITests {

    @Autowired
    private TestRestTemplate testRestTemplate;


    @Test
    public void getAllEmployeesList(){
        Employee[] employees = testRestTemplate
                .getForObject("http://localhost:8080/api/employees/v1/", Employee[].class);
        //Print Employees
        for(Employee employee:employees){
            System.out.println(employee.toString());
        }
        Assertions.assertEquals(employees.length,4);
    }

    @Test
    public void saveEmployee(){
        Employee employee = Employee.builder().firstName("Reva").lastName("Gandhi").email("revaGandhi2@gmail.com").build();
        Employee savedEmployee = testRestTemplate.postForObject("http://localhost:8080/api/employees/v1/",employee,Employee.class);
        Assertions.assertEquals(savedEmployee.getFirstName(),employee.getFirstName());
        Assertions.assertEquals(savedEmployee.getLastName(),employee.getLastName());
        Assertions.assertEquals(savedEmployee.getEmail(),employee.getEmail());
    }

    @Test
    public void getEmployeeById(){
        Long employeeID = 1L;
        //Positive Scenario

        Employee employee = testRestTemplate.getForObject("http://localhost:8080/api/employees/v1/"+employeeID,Employee.class);
        Assertions.assertEquals(employee.getFirstName(),"Gauri");
        Assertions.assertEquals(employee.getLastName(),"Gupta");
        Assertions.assertEquals(employee.getEmail(),"gouri8gupta@gmail.com");

        //Negative Scenario
        employeeID=99L;
        ResponseEntity<EmployeeNotFound> response = testRestTemplate.getForEntity("http://localhost:8080/api/employees/v1/"+employeeID, EmployeeNotFound.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Employee with ID:- "+employeeID+" not found ", response.getBody().getMessage());

    }

    @Test
    public void deleteEmployeeById(){
        Long employeeID = 5L;
        testRestTemplate.delete("http://localhost:8080/api/employees/v1/" + employeeID);
    }

    @Test
    public void updateEmployee(){
        Long employeeID = 1L;
        Employee employee = Employee.builder().id(employeeID).firstName("Gauri").lastName("Gupta").email("gouri8gupta@gmail.com").build();
        testRestTemplate.put("http://localhost:8080/api/employees/v1/",employee,Employee.class);
        Employee getEmployee = testRestTemplate.getForObject("http://localhost:8080/api/employees/v1/"+employeeID,Employee.class);
        Assertions.assertEquals(getEmployee.getEmail(),"gouri8gupta@gmail.com");
    }


}
