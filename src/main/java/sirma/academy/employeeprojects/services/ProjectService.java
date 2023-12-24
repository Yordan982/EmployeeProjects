package sirma.academy.employeeprojects.services;

import org.springframework.stereotype.Service;
import sirma.academy.employeeprojects.dtos.UpdateProjectDTO;
import sirma.academy.employeeprojects.models.Project;
import sirma.academy.employeeprojects.repositories.ProjectRepository;

import static sirma.academy.employeeprojects.constants.Constants.INVALID_PROJECT_ID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;


    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void save(Project project) {
        this.projectRepository.save(project);
    }

    public void delete(Long id) {
        boolean exists = projectRepository.existsById(id);
        if (exists) {
            projectRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException(String.format(INVALID_PROJECT_ID, id));
        }
    }

    public Project findById(Long id) {
        return this.projectRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(String.format(INVALID_PROJECT_ID, id)));
    }

    public void update(Long id, UpdateProjectDTO updateProjectDTO) {
        Project project = findById(id);
        project.setProjectName(updateProjectDTO.getProjectName());
        this.save(project);
    }
}