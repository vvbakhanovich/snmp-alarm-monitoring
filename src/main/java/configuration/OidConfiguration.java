package configuration;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OidConfiguration {
    Map<String, String> getAlarmOids();

    Map<String, String> getInputs(String ip);

    Set<String> getIps();

    List<String> getButtonNames();
}
