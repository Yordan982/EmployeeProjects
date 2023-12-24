package sirma.academy.employeeprojects.controllers;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sirma.academy.employeeprojects.dtos.UpdateProjectDTO;
import sirma.academy.employeeprojects.models.Project;
import sirma.academy.employeeprojects.services.ProjectService;

import static sirma.academy.employeeprojects.constants.Constants.*;

@Controller
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/create")
    @Validated
    public ResponseEntity<?> create(@Valid @RequestBody Project project) {
        try {
            this.projectService.save(project);
            return ResponseEntity.ok(SUCCESSFULLY_REGISTERED_PROJECT);
        } catch (HttpMessageNotReadableException exception) {
            return ResponseEntity.badRequest().body(INVALID_DATA_PROVIDED);
        }
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) {
        try {
            return ResponseEntity.ok((this.projectService.findById(id).toString()));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(String.format(INVALID_PROJECT_ID, id));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UpdateProjectDTO updateProjectDTO) {
        try {
            this.projectService.update(id, updateProjectDTO);
            return ResponseEntity.ok(String.format(SUCCESSFULLY_UPDATED_PROJECT, updateProjectDTO.getProjectName(), id));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(String.format(INVALID_PROJECT_ID, id));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            this.projectService.delete(id);
            return ResponseEntity.ok(SUCCESSFULLY_DELETED_PROJECT);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(String.format(INVALID_PROJECT_ID, id));
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            return ResponseEntity.badRequest().body(String.format(CANNOT_DELETE_PROJECT_ID, id));
        }
    }
}