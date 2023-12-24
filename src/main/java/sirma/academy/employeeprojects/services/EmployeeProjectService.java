package sirma.academy.employeeprojects.services;

import org.springframework.stereotype.Service;
import sirma.academy.employeeprojects.dtos.EmployeeOverlapDataDTO;
import sirma.academy.employeeprojects.dtos.EmployeeProjectDTO;
import sirma.academy.employeeprojects.models.Employee;
import sirma.academy.employeeprojects.models.EmployeeProject;
import sirma.academy.employeeprojects.models.Project;
import sirma.academy.employeeprojects.readers.CSVCustomReader;
import sirma.academy.employeeprojects.repositories.EmployeeProjectRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static sirma.academy.employeeprojects.constants.Constants.*;

@Service
public class EmployeeProjectService {

    private final EmployeeProjectRepository employeeProjectRepository;
    private final CSVCustomReader csvCustomReader;
    private final EmployeeService employeeService;
    private final ProjectService projectService;

    public EmployeeProjectService(EmployeeProjectRepository employeeProjectRepository, CSVCustomReader csvCustomReader, EmployeeService employeeService, ProjectService projectService) {
        this.employeeProjectRepository = employeeProjectRepository;
        this.csvCustomReader = csvCustomReader;
        this.employeeService = employeeService;
        this.projectService = projectService;
    }


    public String addFromFile(String filePath) {
        List<EmployeeProjectDTO> employeesList = csvCustomReader.parseCSV(filePath);
        for (EmployeeProjectDTO employeeProjectDTO : employeesList) {
            try {
                EmployeeProject employeeProject = new EmployeeProject();
                employeeProject.setDateFrom(employeeProjectDTO.getDateFrom());
                employeeProject.setDateTo(employeeProjectDTO.getDateTo());
                Employee employee = employeeService.findById(employeeProjectDTO.getEmployeeId());
                Project project = projectService.findById(employeeProjectDTO.getProjectId());
                employeeProject.setEmployee(employee);
                employeeProject.setProject(project);
                this.add(employeeProject);
            } catch (Exception exception) {
                return INVALID_DATA_PROVIDED;
            }
        }
        return SUCCESSFULLY_ADDED_FROM_FILE;
    }

    public void add(EmployeeProject employeeProject) {
        employeeProjectRepository.save(employeeProject);
    }

    public String longestOverlappingEmployees() {

        List<EmployeeProject> employeeProjects = this.employeeProjectRepository.findAll();
        Map<Long, List<EmployeeProject>> projectData = getSortedEmployeeProjects(employeeProjects);
        Map<String, EmployeeOverlapDataDTO> employeeOverlapDataDTOs = new HashMap<>();

        for (Map.Entry<Long, List<EmployeeProject>> entry : projectData.entrySet()) {
            List<EmployeeProject> employeeProjectList = entry.getValue();
            int firstEmployeeIndex = 0;
            int secondEmployeeIndex = firstEmployeeIndex + 1;
            int maxIndex = employeeProjectList.size() - 1;
            while (secondEmployeeIndex <= maxIndex) {
                EmployeeProject firstEmployee = employeeProjectList.get(firstEmployeeIndex);
                EmployeeProject secondEmployee = employeeProjectList.get(secondEmployeeIndex);
                LocalDate firstEmployeeDateTo = (firstEmployee.getDateTo() == null) ? LocalDate.now() : firstEmployee.getDateTo();

                if (firstEmployeeDateTo.compareTo(secondEmployee.getDateFrom()) >= 0) {
                    String key = constructKey(firstEmployee.getEmployee().getId(), secondEmployee.getEmployee().getId(),
                            firstEmployee.getProject().getId());
                    LocalDate maxStart = Collections.max(Arrays.asList(firstEmployee.getDateFrom(), secondEmployee.getDateFrom()));
                    LocalDate minEnd = Collections.min(Arrays.asList(firstEmployeeDateTo, secondEmployee.getDateTo()));
                    long overlappingDays =
                            Duration.between(maxStart.atStartOfDay(), minEnd.atStartOfDay()).toDays() + 1;
                    if (employeeOverlapDataDTOs.containsKey(key)) {
                        EmployeeOverlapDataDTO employeeOverlapDataDTO = employeeOverlapDataDTOs.get(key);
                        employeeOverlapDataDTO.setDaysWorkedTogether(employeeOverlapDataDTO.getDaysWorkedTogether() + overlappingDays);
                        employeeOverlapDataDTOs.put(key, employeeOverlapDataDTO);
                    } else {
                        employeeOverlapDataDTOs.put(key, new EmployeeOverlapDataDTO(
                                firstEmployee.getEmployee().getId(),
                                secondEmployee.getEmployee().getId(),
                                firstEmployee.getProject().getId(),
                                overlappingDays
                        ));
                    }
                    secondEmployeeIndex++;
                } else {
                    firstEmployeeIndex++;
                    secondEmployeeIndex = firstEmployeeIndex + 1;
                }
            }
        }

        return returnMostOverlappingHours(employeeOverlapDataDTOs);
    }

    private Map<Long, List<EmployeeProject>> getSortedEmployeeProjects(List<EmployeeProject> employeeProjects) {
        return employeeProjects.stream().sorted(Comparator.comparing(EmployeeProject::getDateFrom)
                        .thenComparing((x, y) -> {
                            LocalDate firstDateTo = (x.getDateTo() == null) ? LocalDate.now() : x.getDateTo();
                            LocalDate secondDateTo = (y.getDateTo() == null) ? LocalDate.now() : y.getDateTo();
                            return firstDateTo.compareTo(secondDateTo);
                        }).thenComparing(x -> x.getEmployee().getId()))
                .collect(Collectors.groupingBy(ep -> ep.getProject().getId()));
    }

    private String constructKey(Long firstEmployeeId, Long secondEmployeeId, Long projectId) {
        if (firstEmployeeId > secondEmployeeId) {
            long tempValue = firstEmployeeId;
            firstEmployeeId = secondEmployeeId;
            secondEmployeeId = tempValue;
        }
        return String.format("%d,%d,%d", firstEmployeeId, secondEmployeeId, projectId);
    }

    private String returnMostOverlappingHours(Map<String, EmployeeOverlapDataDTO> employeeOverlapDataDTOs) {

        StringBuilder sb = new StringBuilder();
        Map<String, Long> employeeCombinationWithHours =
                employeeOverlapDataDTOs.values().stream().collect(Collectors.groupingBy(
                        x -> x.getFirstEmployeeId() + "," + x.getSecondEmployeeId(),
                        Collectors.summingLong(EmployeeOverlapDataDTO::getDaysWorkedTogether)
                ));

        Map.Entry<String, Long> pairWithMostHours = Collections.max(employeeCombinationWithHours.entrySet(),
                Map.Entry.comparingByValue());
        String[] employeeIds = pairWithMostHours.getKey().split(",");
        sb.append(String.format("%s, %s, %s%n", employeeIds[0], employeeIds[1], pairWithMostHours.getValue()));
        employeeOverlapDataDTOs.keySet().stream().filter(x -> x.startsWith(pairWithMostHours.getKey())).forEach(x -> {
                    EmployeeOverlapDataDTO employeeOverlapDataDTO = employeeOverlapDataDTOs.get(x);
                    String projectId = employeeOverlapDataDTO.getProjectId().toString();
                    String hoursWorkedTogether = employeeOverlapDataDTO.getDaysWorkedTogether().toString();
                    sb.append(String.format("%s, %s%n", projectId, hoursWorkedTogether));
                }
        );
        return sb.toString();
    }
}