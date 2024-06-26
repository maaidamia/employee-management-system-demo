package com.example.employeedemo.controller;

import com.example.employeedemo.service.EmployeeService;
import com.example.employeedemo.model.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // display list of employees
    @GetMapping("/")
    public String viewHomePage(Model model){
        return findPagination(1, model);
    }

    @GetMapping("/showNewEmployeeForm")
    public String showNewEmployeeForm(Model model) {
        //create model attribute to bind form data
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "new_employee";
    }

    @PostMapping("/saveEmployeeDetails")
    public String saveEmployeeDetails(@ModelAttribute("employee") Employee employee) {
        //save employee to database
        employeeService.saveEmployee(employee);
        return "redirect:/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value="id") long id, Model model){
        //get employee from the service
        Employee employee = employeeService.getEmployeeById(id);

        //set employee as a model attribute to pre-populate the form
        model.addAttribute("employee", employee);
        return "update_employee";
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable(value="id") long id){
        //call delete employee method
        this.employeeService.deleteEmployeeById(id);
        return "redirect:/";
    }

    @GetMapping("/employees")
    public String findPagination(@RequestParam(value="page", defaultValue = "1") int pageNo, Model model){
        int pageSize = 5;

        Page<Employee> page = employeeService.findPaginated(pageNo, pageSize);
        List<Employee> listOfEmployees = page.getContent();

        int start = (pageNo - 1) * pageSize + 1;
        int end = Math.min(pageNo * pageSize, (int) page.getTotalElements());

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listOfEmployees", listOfEmployees);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "index";
    }
}
