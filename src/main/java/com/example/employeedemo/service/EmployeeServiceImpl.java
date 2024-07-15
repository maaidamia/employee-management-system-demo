package com.example.employeedemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.employeedemo.model.Employee;
import com.example.employeedemo.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    @Override
    public void saveEmployee(Employee employee) {
        this.employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(long id) {
        Optional<Employee> optional = employeeRepository.findById(id);
        Employee employee = null;
        if(optional.isPresent()){
            employee = optional.get();
        }else{
            throw new RuntimeException("Employee not found for id:" +id);
        }
        return employee;
    }

    @Override
    public void deleteEmployeeById(long id) {
        this.employeeRepository.deleteById(id);
    }

    @Override
    public Page<Employee> pagingAndSorting(int pageNo, int pageSize, String[] sortFields, String[] sortDirections) {
        List<Sort.Order> orders = new ArrayList<>();

        if (sortFields.length != sortDirections.length) {
            throw new IllegalArgumentException("Sort fields and directions must have the same length");
        }

        for (int i = 0; i < sortFields.length; i++) {
            orders.add(new Sort.Order(
                    sortDirections[i].equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.Direction.ASC : Sort.Direction.DESC,
                    sortFields[i]
            ));
        }

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(orders));
        return this.employeeRepository.findAll(pageable);
    }
}
