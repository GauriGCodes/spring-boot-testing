package com.company.springboottesting.integration;


import com.company.springboottesting.exception.EmployeeNotFound;
import com.company.springboottesting.model.Employee;
import com.company.springboottesting.repository.EmployeeRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class EmployeeControllerTestContainerTests {
    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest");

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeAll
    static void beforeAll(){
        mySQLContainer.start();
    }

    @AfterAll
    static void afterAll(){
        mySQLContainer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

     @BeforeEach
     void setup(){
            employeeRepository.deleteAll();
       }

    @Test
    public void getAllEmployeesList(){
        Employee employee = Employee.builder().firstName("Tarang").lastName("Gupta").email("taranggupta4@gmail.com").build();
        Employee employee1 = Employee.builder().firstName("Gauri").lastName("Gupta").email("gouri88gupta@gmail.com").build();
        Employee employee2 = Employee.builder().firstName("Sanjay").lastName("Gupta").email("sanjaygupta9@gmail.com").build();
        employeeRepository.saveAll(List.of(employee,employee1,employee2));

        Employee[] employees = testRestTemplate
                .getForObject("http://localhost:8080/api/employees/v1/", Employee[].class);

        Assertions.assertEquals(employees.length,3);
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
        //Positive Scenario
        Employee employee1 = Employee.builder().firstName("Gauri").lastName("Gupta").email("gouri8gupta@gmail.com").build();
        employeeRepository.save(employee1);

        Employee employee = testRestTemplate.getForObject("http://localhost:8080/api/employees/v1/"+employee1.getId(),Employee.class);
        Assertions.assertEquals(employee.getFirstName(),"Gauri");
        Assertions.assertEquals(employee.getLastName(),"Gupta");
        Assertions.assertEquals(employee.getEmail(),"gouri8gupta@gmail.com");

        //Negative Scenario
        Long employeeID=99L;
        ResponseEntity<EmployeeNotFound> response = testRestTemplate.getForEntity("http://localhost:8080/api/employees/v1/"+employeeID, EmployeeNotFound.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Employee with ID:- "+employeeID+" not found ", response.getBody().getMessage());

    }

    @Test
    public void deleteEmployeeById(){
        Employee employee1 = Employee.builder().firstName("Gauri").lastName("Gupta").email("gouri8gupta@gmail.com").build();
        employeeRepository.save(employee1);

        testRestTemplate.delete("http://localhost:8080/api/employees/v1/" + employee1.getId());

        ResponseEntity<EmployeeNotFound> response = testRestTemplate.getForEntity("http://localhost:8080/api/employees/v1/"+employee1.getId(), EmployeeNotFound.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Employee with ID:- "+employee1.getId()+" not found ", response.getBody().getMessage());
    }

    @Test
    public void updateEmployee(){
        Employee employee1 = Employee.builder().firstName("Gauri").lastName("Gupta").email("gouri88gupta@gmail.com").build();
        employeeRepository.save(employee1);

        Employee employee = Employee.builder().id(employee1.getId()).firstName("Gauri").lastName("Gupta").email("gouri8gupta@gmail.com").build();
        testRestTemplate.put("http://localhost:8080/api/employees/v1/",employee,Employee.class);

        Employee getEmployee = testRestTemplate.getForObject("http://localhost:8080/api/employees/v1/"+employee1.getId(),Employee.class);
        Assertions.assertEquals(getEmployee.getEmail(),"gouri8gupta@gmail.com");
    }

}
