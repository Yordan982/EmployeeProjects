package sirma.academy.employeeprojects.readers;

import org.springframework.stereotype.Component;
import sirma.academy.employeeprojects.models.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@Component
public class CSVCustomReader {

    public List<Employee> parseCSV(String filePath) {
        List<Employee> employeesFromFile = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] employeeData = line.split(",");
                Employee currentEmployee = new Employee();
                currentEmployee.setEmployeeId(Long.valueOf(employeeData[0]));

                if (!"NULL".equals(employeeData[1])) {
                    try {
                        currentEmployee.setDateFrom(CustomDateParser.parseDate(employeeData[1]));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                int indexOfProjectId = employeeData.length - 1;
                currentEmployee.setProjectId(Long.valueOf(employeeData[indexOfProjectId]));

                if (employeeData.length == 4 && !"NULL".equals(employeeData[2])) {
                    try {
                        currentEmployee.setDateTo(CustomDateParser.parseDate(employeeData[2]));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                employeesFromFile.add(currentEmployee);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return employeesFromFile;
    }
}