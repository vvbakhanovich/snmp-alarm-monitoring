package util;

import java.awt.*;
import java.io.File;
import java.time.format.DateTimeFormatter;

public final class Constants {
    private Constants() {

    }

    public static final String FAIL_STATUS = "FAIL";
    public static final String WARN_STATUS = "WARN";
    public static final String OK_STATUS = "OK";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static final Color ALARM_COLOR = Color.RED;
    public static final Color OK_COLOR = Color.LIGHT_GRAY;
    public static final String CONFIG_DIR = new File(System.getProperty("user.dir"), "/config").toString();
}
