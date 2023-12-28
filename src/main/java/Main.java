import alarm.Alarm;
import alarm.AudioAlarm;
import configuration.FileOidConfiguration;
import configuration.OidConfiguration;
import service.MvAlarmProcessor;
import service.SnmpTrapReceiver;
import service.VarBindProcessor;
import ui.AlarmWindow;
import ui.UIWindow;

public class Main {
    public static void main(String[] args) {
        OidConfiguration configuration = new FileOidConfiguration();
        Alarm alarm = new AudioAlarm("alarm/alarm.wav");
        AlarmWindow ui = new UIWindow(configuration.getButtonNames(), alarm);
        VarBindProcessor processor = new MvAlarmProcessor(ui);
        new SnmpTrapReceiver(configuration, processor).run();

    }
}