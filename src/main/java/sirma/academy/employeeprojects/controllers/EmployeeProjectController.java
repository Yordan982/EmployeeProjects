package sirma.academy.employeeprojects.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sirma.academy.employeeprojects.services.EmployeeProjectService;

import static sirma.academy.employeeprojects.constants.Constants.*;

@RestController
@RequestMapping("/employee_project")
public class EmployeeProjectController {
    private final EmployeeProjectService employeeProjectService;

    public EmployeeProjectController(EmployeeProjectService employeeProjectService) {
        this.employeeProjectService = employeeProjectService;
    }

    @PostMapping("/read")
    public ResponseEntity<?> readFromFile() {
        return ResponseEntity.ok(this.employeeProjectService.addFromFile(CSV_FILE_PATH));
    }

    @GetMapping("/pairs")
    public ResponseEntity<?> pairs() {
        String employeesWithDuplicateProjects = employeeProjectService.longestOverlappingEmployees();
        if (employeesWithDuplicateProjects.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(employeesWithDuplicateProjects, HttpStatus.OK);
        }
    }
}