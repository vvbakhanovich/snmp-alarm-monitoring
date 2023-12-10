import configuration.FileOidConfiguration;
import configuration.OidConfiguration;
import service.FailAlarmVarBindProcessor;
import service.SnmpTrapReceiver;
import service.VarBindProcessor;
import ui.AlarmWindow;
import ui.UIWindow;

public class Main {
    public static void main(String[] args) {
        OidConfiguration configuration = new FileOidConfiguration();
        AlarmWindow ui = new UIWindow(configuration.getInputs().values());
        VarBindProcessor processor = new FailAlarmVarBindProcessor(ui);
        new SnmpTrapReceiver(configuration, processor).run();
    }
}