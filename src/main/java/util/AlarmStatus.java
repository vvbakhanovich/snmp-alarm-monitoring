package util;

import java.awt.*;

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
