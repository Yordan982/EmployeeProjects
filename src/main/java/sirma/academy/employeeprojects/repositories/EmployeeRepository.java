package sirma.academy.employeeprojects.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sirma.academy.employeeprojects.models.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(value = "SELECT * FROM projects.employees " +
            "WHERE project_id IN " +
            "(SELECT project_id FROM employees " +
            "GROUP BY project_id " +
            "HAVING COUNT(project_id) > 1) " +
            "ORDER BY project_id", nativeQuery = true)
    List<Employee> findEmployeesWithDuplicateProjects();
}