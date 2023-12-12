package util;

import java.awt.*;
import java.time.format.DateTimeFormatter;

public final class Constants {
    private Constants() {

    }

    public static final String FAIL_STATUS = "FAIL";
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static final Color alarmColor = Color.RED;
}
