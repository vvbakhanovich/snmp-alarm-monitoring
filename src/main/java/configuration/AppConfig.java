package configuration;

import alarm.Alarm;
import alarm.AudioAlarm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import service.MvAlarmProcessor;
import service.SnmpTrapReceiver;
import service.VarBindProcessor;
import ui.AlarmWindow;
import ui.UIWindow;

@Configuration
@PropertySource("classpath:snmp.properties")
public class AppConfig {

    @Bean
    public Alarm alarm() {
        return new AudioAlarm();
    }

    @Bean
    public OidConfiguration oidConfiguration() {
        return new FileOidConfiguration();
    }


    @Bean
    public AlarmWindow alarmWindow() {
        return new UIWindow(oidConfiguration().getButtonNames(), alarm());
    }

    @Bean
    public VarBindProcessor mvAlarmProcessor() {
        return new MvAlarmProcessor(alarmWindow());
    }

    @Bean
    public SnmpTrapReceiver snmpTrapReceiver() {
        return new SnmpTrapReceiver(oidConfiguration(), mvAlarmProcessor());
    }
}
