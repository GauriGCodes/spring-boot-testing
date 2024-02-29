package com.company.springboottesting.repository;

import com.company.springboottesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    //Index Parameters
    @Query("select e from Employee e where e.firstName=?1 and e.lastName=?2")
    Employee findByFirstNameAndSecondName(String firstName, String lastName);

    //Named Parameters
    @Query("select e from Employee e where e.firstName=:firstName and e.lastName=:lastName")
    Employee findByFirstNameAndSecondNameNamedParameters(@Param("firstName") String firstName, @Param("lastName") String lastName);

    //Native Index Query
    @Query(value = "select * from employees where first_name=?1 and last_name=?2",nativeQuery = true)
    Employee findByNativeSQl(String firstName, String lastName);

    //Native Query Index Parameters
    @Query(value = "select * from employees where first_name=:firstName and last_name=:lastName",nativeQuery = true)
    Employee findByNativeSQlNamedParameter(@Param("firstName") String firstName, @Param("lastName")String lastName);
}
