package configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * Reads configuration from file.
 */
@Component
@Slf4j
public class FileOidConfiguration implements OidConfiguration {

    /**
     * Contains monitored device ip address as key and as value map of input number - input name of corresponding device.
     */
    private final Map<String, Map<String, String>> mvInputs = new HashMap<>();

    /**
     * Contains OID as key and alarm message as value.
     */
    private final Map<String, String> alarmOids = new HashMap<>();

    /**
     * List of button names for the ui.
     */
    private final List<String> buttonNames = new ArrayList<>();

    /**
     * Reads configuration from file, populates according fields with values from file.
     */
    @PostConstruct
    public void init() {
        loadConfigFromFile();
        generateButtonNames();
    }

    @Override
    public Map<String, String> getAlarmOids() {
        return alarmOids;
    }

    @Override
    public List<String> getButtonNames() {
        return buttonNames;
    }

    @Override
    public Map<String, String> getInputs(String ip) {
        return mvInputs.get(ip);
    }

    @Override
    public Set<String> getIps() {
        return mvInputs.keySet();
    }

    private void loadConfigFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(CONFIG_DIR, "/configuration.txt")))) {
            String line = reader.readLine();
            while (!line.isEmpty()) {
                String[] alarm = line.split("=");
                alarmOids.put(alarm[0], alarm[1]);
                line = reader.readLine();
            }


            String inputLine = reader.readLine();
            Map<String, String> inputIp = new HashMap<>();
            while (inputLine != null) {
                if (inputLine.isBlank()) {
                    inputLine = reader.readLine();
                }
                String[] ipAddr = inputLine.split("=");
                String ip = ipAddr[1];
                inputLine = reader.readLine();
                while (inputLine != null && !inputLine.isBlank()) {
                    String[] input = inputLine.split("=");
                    inputIp.put(input[0], input[1]);
                    inputLine = reader.readLine();
                }
                mvInputs.put(ip, inputIp);
                inputIp = new HashMap<>();
            }
        } catch (Exception e) {
            log.error("Ошибка при чтении конфигурационного файла. {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void generateButtonNames() {
        for (Map<String, String> value : mvInputs.values()) {
            buttonNames.addAll(value.values());
        }
    }
}

