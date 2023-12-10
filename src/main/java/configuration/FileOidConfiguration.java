package configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class FileOidConfiguration implements OidConfiguration {

    private final Map<String, String> alarmOids = new HashMap<>();
    private final Map<String, String> inputs = new HashMap<>();

    public FileOidConfiguration() {
        loadConfigFromFile();
    }

    @Override
    public Map<String, String> getAlarmOids() {
        return alarmOids;
    }

    @Override
    public Map<String, String> getInputs() {
        return inputs;
    }

    private void loadConfigFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(System.getProperty("user.dir"),
                "config/configuration.txt")))) {
            String line = reader.readLine();
            while (!line.isEmpty()) {
                String[] alarm = line.split("=");
                alarmOids.put(alarm[0], alarm[1]);
                line = reader.readLine();
            }

            String inputLine = reader.readLine();
            while (inputLine != null) {
                String[] input = inputLine.split("=");
                inputs.put(input[0], input[1]);
                inputLine = reader.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

