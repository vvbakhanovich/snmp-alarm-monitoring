package configuration;

import java.util.Map;

public interface OidConfiguration {
    Map<String, String> getAlarmOids();

    Map<String, String> getInputs();
}
