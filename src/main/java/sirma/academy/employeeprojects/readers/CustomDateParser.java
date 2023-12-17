package sirma.academy.employeeprojects.readers;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

//TODO: Fix the error message when one or more records could not be parsed
public class CustomDateParser {
    private static final List<String> DATE_FORMATS = Arrays.asList(
            "yyyy-MM-dd",
            "yyyy/MM/dd",
            "dd-MM-yyyy",
            "MM-dd-yyyy"
    );

    public static LocalDate parseDate(String input) throws ParseException {
        for (String pattern : DATE_FORMATS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return LocalDate.parse(input, formatter);
            } catch (Exception e) {
                System.out.println("Error parsing date with pattern " + pattern + ": " + e.getMessage());
            }
        }
        throw new ParseException("Unable to parse date: " + input, 0);
    }
}