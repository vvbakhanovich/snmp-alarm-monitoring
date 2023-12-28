package util;

import java.awt.*;

/**
 * Contains information on status name and corresponding background color for the ui buttons for different alarm levels.
 */
public enum AlarmStatus {
    OK("OK", Color.LIGHT_GRAY),
    WARNING("WARN", Color.YELLOW),
    FAIL("FAIL", Color.RED);

    private final String status;
    private final Color color;

    AlarmStatus(String status, Color color) {
        this.status = status;
        this.color = color;
    }

    public String getName() {
        return status;
    }

    public Color getColor() {
        return color;
    }
}
