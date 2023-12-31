package sirma.academy.employeeprojects.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sirma.academy.employeeprojects.models.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}