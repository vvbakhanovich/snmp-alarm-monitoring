package configuration;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Configuration interface that must return information regarding monitored devices.
 */
public interface OidConfiguration {
    /**
     * Configuration directory
     */
    String CONFIG_DIR = new File(System.getProperty("user.dir"), "/config").toString();

    /**
     * @return map, containing OID as key and alarm message as value.
     */
    Map<String, String> getAlarmOids();

    /**
     * @param ip ip address of monitored device
     * @return map, containing input number as key and corresponding input name as value.
     */
    Map<String, String> getInputs(String ip);

    /**
     * @return set of ip addresses of the devices being monitored
     */
    Set<String> getIps();

    /**
     * @return list of buttons names for the ui.
     */
    List<String> getButtonNames();
}
