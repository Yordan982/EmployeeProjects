package sirma.academy.employeeprojects.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sirma.academy.employeeprojects.models.Project;
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}