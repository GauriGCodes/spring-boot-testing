package com.company.springboottesting.Controller;

import com.company.springboottesting.exception.EmployeeNotFound;
import com.company.springboottesting.model.Employee;
import com.company.springboottesting.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;


@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    public void setUp(){
        this.employee = Employee.builder().firstName("John").lastName("Doe").email("johndoe@gmail.com").build();
    }

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        Mockito.when(employeeService.saveEmployee(Mockito.any(Employee.class))).thenReturn(employee);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/employees/v1/")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(employee))).andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",CoreMatchers.is(employee.getLastName()))).andExpect(MockMvcResultMatchers.jsonPath("$.email",CoreMatchers.is(employee.getEmail())));
    }

    @Test
    public void givenEmployeesList_whenFindAll_thenReturnEmployeesList() throws Exception {
        List<Employee> employeeList = new ArrayList<>();

        Employee employee1 = Employee.builder().firstName("Reva").lastName("Gandhi").email("revagandhi4@gmail.com").build();
        Employee employee2 = Employee.builder().firstName("Shanaya").lastName("Shri").email("sshri@gmail.com").build();
        Employee employee3 = Employee.builder().firstName("Nishtha").lastName("Tara").email("ntara@gmail.com").build();

        employeeList = List.of(employee,employee1,employee2,employee3);

        Mockito.when(employeeService.getAllEmployees()).thenReturn(employeeList);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/v1/")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.size()",CoreMatchers.is(employeeList.size())));
    }

    @Test
    public void givenEmployeeId_whenFindById_thenReturnEmployeeObject() throws Exception {
        //Positive Scenario
        Long employeeId = 1L;
        Mockito.when(employeeService.findEmployeeById(employeeId)).thenReturn(employee);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/v1/{id}",employeeId)).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName()))).andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @Test
    public void givenEmployeeId_whenFindById_thenReturnNotFound() throws Exception {
        //Negative Scenario
        Long employeeId = 1L;
        Mockito.when(employeeService.findEmployeeById(employeeId)).thenThrow(new EmployeeNotFound("Employee with employeeID: "+employeeId+" not found"));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/v1/{id}",employeeId)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void givenEmployeeObject_whenUpdate_thenReturnUpdatedEmployee() throws Exception {
        //Positive Scenario
        Employee updatedEmployee = Employee.builder().firstName("Nishtha").lastName("Tara").email("ntara@gmail.com").build();
        Mockito.when(employeeService.updateEmployee(Mockito.any(Employee.class))).thenReturn(updatedEmployee);


        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/v1/")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(employee))).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(updatedEmployee.getFirstName())))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",CoreMatchers.is(updatedEmployee.getLastName()))).andExpect(MockMvcResultMatchers.jsonPath("$.email",CoreMatchers.is(updatedEmployee.getEmail())));
    }

    @Test
    public void givenEmployeeObject_whenUpdate_thenReturnNotFound() throws Exception {
        //Positive Scenario
        Employee updatedEmployee = Employee.builder().firstName("Nishtha").lastName("Tara").email("ntara@gmail.com").build();
        Mockito.when(employeeService.updateEmployee(Mockito.any(Employee.class))).thenThrow(new EmployeeNotFound("Employee with id "+ updatedEmployee.getId()+" not found"));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/v1/")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(employee))).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void givenEmployeeObject_whenDeleteEmployeeObject_thenRemoveEmployee() throws Exception {
        Long employeeID = 1L;
        Mockito.doNothing().when(employeeService).deleteEmployee(employeeID);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/v1/{id}",employeeID)).andExpect(MockMvcResultMatchers.status().isOk());
    }



}
