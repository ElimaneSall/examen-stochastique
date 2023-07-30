import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tester {

    public static void main(String[] args) {
        String dateTimeString = "2019-01-20 08:10:11";
        String timeString = getTimeFromDateTime(dateTimeString);
        System.out.println(timeString);
    }

    public static String getTimeFromDateTime(String dateTimeString) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String timeString = dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return timeString;
    }
}
