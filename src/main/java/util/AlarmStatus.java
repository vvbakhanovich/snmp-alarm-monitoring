package util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.awt.*;

/**
 * Contains information on status name and corresponding background color for the ui buttons for different alarm levels.
 */
@Component
@Getter
@RequiredArgsConstructor
public enum AlarmStatus {
    OK("OK", Color.LIGHT_GRAY),
    WARNING("WARN", Color.YELLOW),
    FAIL("FAIL", Color.RED);

    private final String status;
    private final Color color;
}
