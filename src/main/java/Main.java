import configuration.FileOidConfiguration;
import configuration.OidConfiguration;
import service.SnmpTrapReceiver;
import ui.AlarmWindow;
import ui.UIWindow;

public class Main {
    public static void main(String[] args) {
        OidConfiguration configuration = new FileOidConfiguration();
        AlarmWindow ui = new UIWindow(configuration.getInputs().values());
        SnmpTrapReceiver trapReceiver = new SnmpTrapReceiver(configuration, ui);
    }
}