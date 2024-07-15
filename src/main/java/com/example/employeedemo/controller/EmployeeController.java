package com.example.employeedemo.controller;

import com.example.employeedemo.service.EmployeeService;
import com.example.employeedemo.model.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // display list of employees
    @GetMapping("/")
    public String viewHomePage(Model model) {
        return pageAndSort(1, 5, new String[]{"firstName"}, new String[]{"asc"}, model);
    }

    @GetMapping("/employees/new")
    public String showNewEmployeeForm(Model model) {
        //create model attribute to bind form data
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "new_employee";
    }

    @PostMapping("/employees/save")
    public String saveEmployeeDetails(@ModelAttribute("employee") Employee employee) {
        //save employee to database
        employeeService.saveEmployee(employee);
        return "redirect:/";
    }

    @GetMapping("/employees/update/{id}")
    public String showFormForUpdate(@PathVariable(value="id") long id, Model model){
        //get employee from the service
        Employee employee = employeeService.getEmployeeById(id);

        //set employee as a model attribute to pre-populate the form
        model.addAttribute("employee", employee);
        return "update_employee";
    }

    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable(value="id") long id){
        //call delete employee method
        this.employeeService.deleteEmployeeById(id);
        return "redirect:/";
    }

    @GetMapping("/employees")
    public String pageAndSort(@RequestParam(value="page", defaultValue = "1") int pageNo,
                                 @RequestParam(value="size", defaultValue = "5") int pageSize,
                                 @RequestParam(value="sort", defaultValue = "firstName") String[] sortFields,
                                 @RequestParam(value="order", defaultValue = "asc") String[] sortDirections,
                                 Model model) {
        Page<Employee> page = employeeService.pagingAndSorting(pageNo, pageSize, sortFields, sortDirections);
        List<Employee> listOfEmployees = page.getContent();

        int start = (pageNo - 1) * pageSize + 1;
        int end = Math.min(pageNo * pageSize, (int) page.getTotalElements());

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listOfEmployees", listOfEmployees);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("pageSizeParam", pageSize); // to keep track of current page size
        model.addAttribute("sortFields", sortFields);
        model.addAttribute("sortDirections", sortDirections);

        return "index";
    }
}
