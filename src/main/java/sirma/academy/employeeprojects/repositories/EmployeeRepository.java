package sirma.academy.employeeprojects.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sirma.academy.employeeprojects.models.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(value = "SELECT e1.*" +
            "FROM projects.employees e1 " +
            "JOIN projects.employees e2 ON e1.project_id = e2.project_id " +
            "WHERE e1.employee_id != e2.employee_id " +
            "AND e1.project_id IN (" +
            "SELECT project_id " +
            "FROM projects.employees " +
            "GROUP BY project_id " +
            "HAVING COUNT(project_id) > 1) " +
            "AND (e1.date_from < e2.date_to OR e2.date_to IS NULL) " +
            "AND (e1.date_to > e2.date_from OR e1.date_to IS NULL) " +
            "ORDER BY e1.project_id;", nativeQuery = true)
    List<Employee> employeesWithOverlappingProjectDates();
}