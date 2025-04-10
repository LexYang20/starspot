package starspot.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);

    /**
     * Parse certain String to Date，eg: "Jan 3-4, 2026"，
     * return ZonedDateTime
     * @param dateRangeStr  "MMM d-d, yyyy"
     * @return List ）
     */
    public static List<ZonedDateTime> parseDateRangeAsUTC(String dateRangeStr) {
        //   ([A-Za-z]+)   : match month（如 Jan）
        //   \\s+          : space
        //   (\\d+)        : date
        //   -             :
        //   (\\d+)        :
        //   ,\\s*         :
        //   (\\d+)        : year
        Pattern pattern = Pattern.compile("([A-Za-z]+)\\s+(\\d+)-(\\d+),\\s*(\\d+)");
        Matcher matcher = pattern.matcher(dateRangeStr);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Wrong format，should be like: Jan 3-4, 2026");
        }

        // parse data
        String month = matcher.group(1);
        String dayStart = matcher.group(2);
        String dayEnd = matcher.group(3);
        String year = matcher.group(4);

        String dateStr1 = month + " " + dayStart + ", " + year;
        String dateStr2 = month + " " + dayEnd + ", " + year;

        List<ZonedDateTime> dates = new ArrayList<>();
        dates.add(parseDateAsUTC(dateStr1));
        dates.add(parseDateAsUTC(dateStr2));
        return dates;
    }

    /**
     * @param dateStr like "Jan 3, 2026"
     * @return  ZonedDateTime
     * @throws IllegalArgumentException
     */
    public static ZonedDateTime parseDateAsUTC(String dateStr) {
        try {
            // parse to LocalDate（
            LocalDate localDate = LocalDate.parse(dateStr, DATE_FORMATTER);
            // convert to ZonedDateTime
            return localDate.atStartOfDay(ZoneOffset.UTC);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Cannot parse data: " + dateStr, e);
        }
    }
    public static String getFormattedStartDate(Instant instant) {
        //TODO: need to check if we need to convert it to local time or UTC time
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        // date format
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(dateFormatter);
    }

    public static String getFormattedEventTime(Instant instant) {
        //TODO: need to check if we need to convert it to local time or UTC time
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        // date format
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
       return localDateTime.format(timeFormatter);
    }

}

