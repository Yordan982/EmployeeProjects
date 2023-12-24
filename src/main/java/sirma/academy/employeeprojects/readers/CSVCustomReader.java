package sirma.academy.employeeprojects.readers;

import org.springframework.stereotype.Component;
import sirma.academy.employeeprojects.dtos.EmployeeProjectDTO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static sirma.academy.employeeprojects.constants.Constants.*;


@Component
public class CSVCustomReader {

    public List<EmployeeProjectDTO> parseCSV(String filePath) {
        List<EmployeeProjectDTO> employeesFromFile = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] employeeData = line.split(",");
                EmployeeProjectDTO currentEmployee = new EmployeeProjectDTO();
                currentEmployee.setEmployeeId(Long.valueOf(employeeData[0]));

                if (!"NULL".equals(employeeData[1])) {
                    try {
                        currentEmployee.setDateFrom(CustomDateParser.parseDate(employeeData[1]));
                    } catch (ParseException e) {
                        System.out.println(UNABLE_TO_READ_LINE);
                    }
                }

                int projectIdIndex = employeeData.length - 1;
                currentEmployee.setProjectId(Long.valueOf(employeeData[projectIdIndex]));

                if (employeeData.length == 4 && !"NULL".equals(employeeData[2])) {
                    try {
                        currentEmployee.setDateTo(CustomDateParser.parseDate(employeeData[2]));
                    } catch (ParseException e) {
                        System.out.println(UNABLE_TO_READ_LINE);
                    }
                }

                employeesFromFile.add(currentEmployee);
            }
        } catch (IOException exception) {
            System.out.println(UNABLE_TO_READ_LINE);
        }
        return employeesFromFile;
    }
}