package sirma.academy.employeeprojects.services;

import org.springframework.stereotype.Service;
import sirma.academy.employeeprojects.dtos.UpdateEmployeeDTO;
import sirma.academy.employeeprojects.models.Employee;
import sirma.academy.employeeprojects.repositories.EmployeeRepository;

import static sirma.academy.employeeprojects.constants.Constants.INVALID_EMPLOYEE_ID;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void save(Employee employee) {
        employeeRepository.save(employee);
    }

    public void delete(Long id) {
        boolean exists = employeeRepository.existsById(id);
        if (exists) {
            employeeRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException(String.format(INVALID_EMPLOYEE_ID, id));
        }
    }

    public Employee findById(Long id) {
        return this.employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(String.format(INVALID_EMPLOYEE_ID, id)));
    }

    public void update(Long id, UpdateEmployeeDTO updateEmployeeDTO) {
        Employee employee = findById(id);
        employee.setLastName(updateEmployeeDTO.getLastName());
        this.save(employee);
    }
}