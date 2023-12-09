package util;

import java.time.format.DateTimeFormatter;

public final class Constants {
    private Constants() {

    }
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
}
