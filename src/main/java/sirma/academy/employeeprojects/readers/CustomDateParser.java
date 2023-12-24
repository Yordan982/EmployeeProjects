package sirma.academy.employeeprojects.readers;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static sirma.academy.employeeprojects.constants.Constants.*;
@Component
public class CustomDateParser {
    private static final List<String> DATE_FORMATS = Arrays.asList(
            "yyyy-MM-dd",
            "yyyy/MM/dd",
            "dd-MM-yyyy",
            "MM-dd-yyyy"
    );

    public LocalDate parseDate(String input) throws ParseException {
        for (String pattern : DATE_FORMATS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return LocalDate.parse(input, formatter);
            } catch (Exception e) {
                System.out.printf((DATE_PATTERN_ERROR), pattern);
            }
        }
        throw new ParseException(UNABLE_TO_PARSE_DATE + input, 0);
    }
}