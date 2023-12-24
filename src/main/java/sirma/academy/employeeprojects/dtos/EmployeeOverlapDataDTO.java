package sirma.academy.employeeprojects.dtos;

public class EmployeeOverlapDataDTO {

    private Long firstEmployeeId;
    private Long secondEmployeeId;
    private Long projectId;
    private Long daysWorkedTogether;

    public EmployeeOverlapDataDTO(Long firstEmployeeId, Long secondEmployeeId, Long projectId, Long daysWorkedTogether) {

        this.firstEmployeeId = firstEmployeeId;
        this.secondEmployeeId = secondEmployeeId;
        this.projectId = projectId;
        this.daysWorkedTogether = daysWorkedTogether;
    }

    public Long getFirstEmployeeId() {

        return firstEmployeeId;
    }

    public void setFirstEmployeeId(Long firstEmployeeId) {

        this.firstEmployeeId = firstEmployeeId;
    }

    public Long getSecondEmployeeId() {

        return secondEmployeeId;
    }

    public void setSecondEmployeeId(Long secondEmployeeId) {

        this.secondEmployeeId = secondEmployeeId;
    }

    public Long getProjectId() {

        return projectId;
    }

    public void setProjectId(Long projectId) {

        this.projectId = projectId;
    }

    public Long getDaysWorkedTogether() {

        return daysWorkedTogether;
    }

    public void setDaysWorkedTogether(Long daysWorkedTogether) {

        this.daysWorkedTogether = daysWorkedTogether;
    }
}
