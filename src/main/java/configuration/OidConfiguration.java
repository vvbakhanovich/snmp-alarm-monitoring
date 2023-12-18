package configuration;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OidConfiguration {
    String CONFIG_DIR = new File(System.getProperty("user.dir"), "/config").toString();

    Map<String, String> getAlarmOids();

    Map<String, String> getInputs(String ip);

    Set<String> getIps();

    List<String> getButtonNames();
}
