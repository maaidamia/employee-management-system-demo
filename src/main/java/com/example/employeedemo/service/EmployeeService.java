package com.example.employeedemo.service;

import com.example.employeedemo.model.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    void saveEmployee(Employee employee);
    Employee getEmployeeById(long id);
    void deleteEmployeeById(long id);
    Page<Employee> pagingAndSorting(int pageNo, int pageSize, String[] sortFields, String[] sortDirections);
}
