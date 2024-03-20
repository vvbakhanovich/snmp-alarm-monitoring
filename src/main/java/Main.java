import configuration.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.SnmpTrapReceiver;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        SnmpTrapReceiver snmpTrapReceiver = context.getBean("snmpTrapReceiver", SnmpTrapReceiver.class);
        snmpTrapReceiver.run();
    }
}