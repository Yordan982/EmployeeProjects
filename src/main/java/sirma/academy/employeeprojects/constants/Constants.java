package sirma.academy.employeeprojects.constants;

public class Constants {
    public static final String CSV_FILE_PATH = "src/main/resources/static/employees.csv";
    public static final String INVALID_EMPLOYEE_ID = "An employee with id %d does not exist.";
    public static final String CANNOT_DELETE_EMPLOYEE_ID = "Cannot delete. Employee id %d is used in the 'employee_projects' table.";
    public static final String INVALID_PROJECT_ID = "A project with id %d does not exist.";
    public static final String CANNOT_DELETE_PROJECT_ID = "Cannot delete. Project id %d is used in the 'employee_projects' table.";
    public static final String SUCCESSFULLY_REGISTERED_EMPLOYEE = "The employee is now registered.";
    public static final String SUCCESSFULLY_REGISTERED_PROJECT = "The project is now added.";
    public static final String SUCCESSFULLY_UPDATED_EMPLOYEE = "Successfully updated the last name to '%s' for id: %d.";
    public static final String SUCCESSFULLY_UPDATED_PROJECT = "Successfully updated the project name to '%s' for id: %d.";
    public static final String SUCCESSFULLY_DELETED_EMPLOYEE = "The employee is successfully deleted.";
    public static final String SUCCESSFULLY_DELETED_PROJECT = "The project is successfully deleted.";
    public static final String SUCCESSFULLY_ADDED_FROM_FILE = "Successfully added/updated all valid employees.";
    public static final String INVALID_DATA_PROVIDED = "Invalid data provided.";
    public static final String UNABLE_TO_READ_LINE = "Unable to read the line";

    public static final String UNABLE_TO_PARSE_DATE = "Unable to parse date: ";

    public static final String DATE_PATTERN_ERROR = "Error parsing date with pattern: %s. Moving to the next pattern.%n";
}