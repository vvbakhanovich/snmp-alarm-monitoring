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
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static final Color alarmColor = Color.RED;
    public static final String CONFIG_DIR = new File(System.getProperty("user.dir"), "/config").toString();
}
