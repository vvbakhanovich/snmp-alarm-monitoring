package configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class FileOidConfiguration implements OidConfiguration {

    private final Map<String, Map<String, String>> mvInputs = new HashMap<>();

    private final Map<String, String> alarmOids = new HashMap<>();

    private final List<String> buttonNames = new ArrayList<>();

    public FileOidConfiguration() {
        loadConfigFromFile();
        generateButtonNames();
    }

    @Override
    public Map<String, String> getAlarmOids() {
        return alarmOids;
    }

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
            throw new RuntimeException(e);
        }
    }

    private void generateButtonNames() {
        for (Map<String, String> value : mvInputs.values()) {
            buttonNames.addAll(value.values());
        }
    }
}

