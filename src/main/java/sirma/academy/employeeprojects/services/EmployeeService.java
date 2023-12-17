package sirma.academy.employeeprojects.services;

import org.springframework.stereotype.Service;
import sirma.academy.employeeprojects.models.Employee;
import sirma.academy.employeeprojects.repositories.EmployeeRepository;

import static sirma.academy.employeeprojects.constants.Constants.INVALID_EMPLOYEE_ID;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee add(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void delete(Long id) {
        boolean exists = employeeRepository.existsById(id);
        if (exists) {
            employeeRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException(INVALID_EMPLOYEE_ID);
        }
    }
}