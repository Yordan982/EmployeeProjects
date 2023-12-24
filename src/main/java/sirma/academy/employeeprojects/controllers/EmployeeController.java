package sirma.academy.employeeprojects.controllers;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sirma.academy.employeeprojects.dtos.UpdateEmployeeDTO;
import sirma.academy.employeeprojects.models.Employee;
import sirma.academy.employeeprojects.services.EmployeeService;

import static sirma.academy.employeeprojects.constants.Constants.*;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/create")
    @Validated
    public ResponseEntity<?> create(@Valid @RequestBody Employee employee) {
        try {
            this.employeeService.save(employee);
            return ResponseEntity.ok(SUCCESSFULLY_REGISTERED_EMPLOYEE);
        } catch (HttpMessageNotReadableException exception) {
            return ResponseEntity.badRequest().body(INVALID_DATA_PROVIDED);
        }
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) {
        try {
            return ResponseEntity.ok((this.employeeService.findById(id).toString()));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(String.format(INVALID_EMPLOYEE_ID, id));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UpdateEmployeeDTO updateEmployeeDTO) {
        try {
            this.employeeService.update(id, updateEmployeeDTO);
            return ResponseEntity.ok(String.format(SUCCESSFULLY_UPDATED_EMPLOYEE, updateEmployeeDTO.getLastName(), id));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(String.format(INVALID_EMPLOYEE_ID, id));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            this.employeeService.delete(id);
            return ResponseEntity.ok(SUCCESSFULLY_DELETED_EMPLOYEE);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(String.format(INVALID_EMPLOYEE_ID, id));
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            return ResponseEntity.badRequest().body(String.format(CANNOT_DELETE_EMPLOYEE_ID, id));
        }
    }
}