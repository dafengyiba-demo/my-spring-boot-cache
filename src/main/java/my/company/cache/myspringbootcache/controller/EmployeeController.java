package my.company.cache.myspringbootcache.controller;

import my.company.cache.myspringbootcache.bean.Employee;
import my.company.cache.myspringbootcache.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;


    @GetMapping("/emp/{id}")
    public Employee queryEmp(@PathVariable Integer id){
        return employeeService.getEmp(id);
    }

    @GetMapping("/emp")
    public Employee updateEmp(Employee employee){
        return employeeService.updateEmp(employee);
    }

    @GetMapping("/delEmp/{id}")
    public String deleteEmp(@PathVariable Integer id){
         employeeService.deleteEmp(id);
         return "success";
    }

    @GetMapping("/emp/lastName/{lastName}")
    public Employee queryEmpByLastName(@PathVariable String lastName){
        return employeeService.getEmpByLastName(lastName);
    }

}
