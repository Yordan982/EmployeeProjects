# Employee Projects App
The application manages the employees and the projects that they have worked on. It accepts a CSV file with the employees' data and returns the pair of two employees that worked on common projects for the longest period of days and exactly how many days they have spent on each project. It also supports CRUD operations.

## Example of the input data from the CSV file:

**Columns:** employeeId, dateFrom, dateTo, projectId
```
2,2023/23/12,NULL,10
5,12-09-2022,2023-06-01,4
7,2022-10-16,2023-10-15,23
8,2023-02-10,2023-02-17,23
...
```
**Note:** The data from the example above is partial.
## Example of the output data:
```
7, 8, 13
5, 5
23, 8
```
**Explained:** The combination of employee 7 and employee 8 have worked the longest on common projects **(13 days in total)**. On the second line it shows that they have worked on project 5 for 5 days and on the third line it shows that they have worked on project 23 for 8 days (5 + 8 = 13 days). **13 days is the longest period of common projects among all employee pairs.**

## Constraints:
* DateTo can be NULL, equivalent to today.
* The program calculates the days, for which the employee pair has worked together on common projects the most.
* The input data must be loaded to the program from a CSV file.
* More than one date format has to be supported.
* Using external libraries for the CSV parsing is forbidden.

Bonus:
* Implemented persistence of the data.
* CRUD operations for the Employees.

## Usage
### Starting the program
* From the **application.properties** file from the IDE, change the database username and password, then Run the project. By default program is setup to work with MySQL.
* Postman or another API program is required to try the full functionality.

**Important:** When the program is started, it will execute two SQL queries to fill up the database for the **Employee** and **Projects** tables automatically.

### Uploading the records from the CSV file
The data must come from the **employees.csv** file. The file directory can be changed from the constant **CSV_FILE_PATH** inside of the **Constants** class.

#### To fill up the data from the CSV file:
* Submit a **POST** query from Postman to **http://localhost:8080/employee_project/read** and the program will save all valid records from the file.

**Note:** The program tries 4 different date formats and will add those records to the database that match one of the 4 patterns.

#### Supported date patterns:
* yyyy-MM-dd
* yyyy/MM/dd
* dd-MM-yyyy
* MM-dd-yyyy

Additional formats can be added in the **DATE_FORMATS** List from **CustomDateParser** class.

### Returning the pair with the most spent days on common projects
1) Submit a **GET** query from Postman pointing to **http://localhost:8080/employee_project/pairs**
* The response will return 2 or more lines.

On the first line it shows employeeId1, employeeId2 and spent days on common projects.

On each next line it breaks those days to each project and prints the projectId and the days spent on the project together.

### CRUD Operations
#### Employee Class
1. **Create**

Submit a **POST** query to **localhost:8080/employee/create** from Postman with the details of the user.

Example of valid JSON data:
```
{
    "firstName": "Petar",
    "lastName": "Aleksiev"
}
```
2. **Read**

Submit a **GET** query to **http://localhost:8080/employee/read/2** from Postman, where 2 is the employeeId. If the employee exists, his/her details will be returned. Example response:
```
Ivan Ivanov, id: 2
```

3. **Update**

Submit a **PUT** query to **localhost:8080/employee/update/2** from Postman with the last name of the user, where 2 is the employeeId. The DTO is set up to update the lastName only.

Example of valid JSON data:
```
{
    "lastName": "Todorov"
}
```
Example response after the update:
```
Successfully updated the last name to 'Todorov' for id: 2.
```

4. **Delete**

Submit a **DELETE** query to **http://localhost:8080/employee/delete/2** from Postman, where **2** is the employeeId.

If the employee exists and is not linked to any projects from the **employee_project** table, the employee will be deleted.

#### Project Class
1. **Create**

Submit a **POST** query to **localhost:8080/project/create** from Postman with the project name.

Example of valid JSON data:
```
{
    "projectName": "Automobile Tire Shop"
}
```
2. **Read**

Submit a **GET** query to **http://localhost:8080/project/read/2** from Postman, where 2 is the id of the project. If the project exists, its details will be returned. Example response:
```
Project name: Online Banking, id: 2
```

3. **Update**

Submit a **PUT** query to **localhost:8080/project/update/2** from Postman with the project name, where 2 is the projectId.

Example of valid JSON data:
```
{
    "projectName": "Innovative Banking System"
}
```
Example response after the update:
```
Successfully updated the project name to 'Innovative Banking System' for id: 32.
```

4. **Delete**

Submit a **DELETE** query to **http://localhost:8080/project/delete/2** from Postman, where **2** is the projectId.

If the project exists and is not linked to any records from the **employee_project** table, the project will be deleted.

## Implementation and solution

The application uses a MySQL database and therefore it saves the data after each query automatically.

The project implements Maven and Spring functionality, along with a few external libraries.

### Finding the pair with the most overlapping hours for common projects

In order to find the pair of employees with the most hours worked together, we have to go through all of them and check employee pairs one by one. Doing so would be extremely inefficient and time consuming, so the current algorithm uses sorting and a variation of the "Two Pointers" technique.

#### How does it work

First, we group the current records in the **employee_project** table by projectId. Second, we sort them by dateFrom, then by dateTo and by employeeId. Finally, we go through every project and check employee pairs for overlapping shifts.

#### Efficiency of the resolution
1. **Grouping records by project**: This allows us to skip projects with only 1 employee, without getting into the complicated logic of checking for overlapping. If the project has only one employee, there is obviously no overlap.
2. **Sorting by dateFrom**: This allows us to skip **employee_project** records, without even knowing what is in the record. 

**Example**: We have the current records in the table below, sorted by dateFrom from Project #1:
```
Employee1 - from 01.12 to 05.12
Employee2 - from 02.12 to 10.12
Employee3 - from 06.12 to 10.12
Employee4 - from 08.12 to 12.12
Employee5 - from 10.12 to 15.12
```
The implemented logic will firstly check employee1 and employee2 for overlapping dates. There is clearly an overlap, so we are going to the next one - employee1 and employee3. Since they do not overlap, we know for a fact that employee1 does not overlap with employees 4 and 5 as well, without even knowing anything about their start dates. We are sure that employee3's dateFrom is after employee1's dateTo, and since all the dateFroms are sorted, we know that all the other employees' dateFroms are also after employee1's dateTo. This allows the program to break the loop and go to the next pair - employee2 and employee3, skipping unnecessary checks.

To add some more efficiency into the mix, the result of the looping through the projects is stored into a Map with a key structured in the format **"firstEmployeeId, secondEmployeeId, projectId"**. This allows us to access the information stored faster, as searching by the key of a Map has a complexity of O(1).

Once all of the information is analyzed and stored, the only thing left is to check for the max value of all the employee pairs with their combined hours and print the result.

## License
This project serves as the graduation task for the Sirma Academy (September-December 2023).