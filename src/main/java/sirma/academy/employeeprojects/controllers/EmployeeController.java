package sirma.academy.employeeprojects.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sirma.academy.employeeprojects.readers.CSVCustomReader;
import sirma.academy.employeeprojects.repositories.EmployeeRepository;
import sirma.academy.employeeprojects.services.EmployeeService;
import sirma.academy.employeeprojects.models.Employee;

import java.util.List;

import static sirma.academy.employeeprojects.constants.Constants.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CSVCustomReader csvCustomReader;
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/duplicates")
    public ResponseEntity<List<Employee>> duplicates() {
        List<Employee> employeesWithDuplicateProjects = employeeRepository.findEmployeesWithDuplicateProjects();

        if (employeesWithDuplicateProjects.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(employeesWithDuplicateProjects, HttpStatus.OK);
        }
    }

    //TODO: Do not add new record if it already exists with the same data; Fix error message for incorrect data entered (when parsing)
    @PostMapping("/add")
    @Validated
    public ResponseEntity<?> add(@Valid @RequestBody Employee employee) {
        try {
            employeeService.add(employee);
            return ResponseEntity.ok(SUCCESSFULLY_REGISTERED);
        } catch (HttpMessageNotReadableException exception) {
            return ResponseEntity.badRequest().body(INVALID_DATA_PROVIDED);
        }
    }

    //TODO: Return an error when an employee with the id does not exist (entered with a String)
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.delete(id);
            return ResponseEntity.ok(SUCCESSFULLY_DELETED);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(INVALID_EMPLOYEE_ID);
        }
    }

    //TODO: Rewrite the if statements
    @GetMapping("/read")
    public ResponseEntity<?> readFromFile() {
        int insertedEmployees = 0;
        List<Employee> employeesList = csvCustomReader.parseCSV(CSV_FILE_PATH);
        for (Employee currentEmployee : employeesList) {
            employeeService.add(currentEmployee);
            insertedEmployees++;
        }
        if (insertedEmployees > 0) {
            return ResponseEntity.ok(String.format(SUCCESSFULLY_ADDED_FROM_FILE, insertedEmployees));
        } else {
            return ResponseEntity.badRequest().body(INVALID_DATA_PROVIDED);
        }
    }
}