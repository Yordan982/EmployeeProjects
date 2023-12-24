package sirma.academy.employeeprojects.dtos;

public class UpdateEmployeeDTO {

    private String lastName;

    public String getLastName() {
        return lastName;
    }

    public UpdateEmployeeDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
}