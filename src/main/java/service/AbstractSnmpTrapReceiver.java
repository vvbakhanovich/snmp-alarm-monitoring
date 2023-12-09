package service;

import alarm.Alarm;
import alarm.AudioAlarm;
import org.snmp4j.*;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
import ui.UIWindow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractSnmpTrapReceiver implements CommandResponder {

    UIWindow ui;
    private Snmp snmp;
    protected final Map<String, String> alarmOids;
    protected final Map<String, String> inputs;
    protected final Alarm alarm;


    public AbstractSnmpTrapReceiver() {
        alarm = new AudioAlarm();
        alarmOids = new HashMap<>();
        inputs = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/configuration.txt"))) {
            String line = reader.readLine();
            while (!line.isEmpty()) {
                String[] alarm = line.split("=");
                alarmOids.put(alarm[0], alarm[1]);
                line = reader.readLine();
            }

            String inputLine = reader.readLine();
            while(inputLine != null) {
                String[] input =  inputLine.split("=");
                inputs.put(input[0], input[1]);
                inputLine = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            init();
            snmp.addCommandResponder(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void init() throws IOException {
        ThreadPool threadPool = ThreadPool.create("Trap", 10);
        MultiThreadedMessageDispatcher dispatcher = new MultiThreadedMessageDispatcher(threadPool,
                new MessageDispatcherImpl());
        Address listenAddress = GenericAddress.parse(System.getProperty(
                "snmp4j.listenAddress", "udp:0.0.0.0/162"));
        TransportMapping<?> transport;

        if (listenAddress instanceof UdpAddress) {
            transport = new DefaultUdpTransportMapping((UdpAddress) listenAddress);
        } else {
            transport = new DefaultTcpTransportMapping((TcpAddress) listenAddress);
        }

        snmp = new Snmp(dispatcher, transport);
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());

        snmp.listen();

        ui = new UIWindow(inputs.values());
    }

    public void processPdu(CommandResponderEvent event) {
        StringBuffer msg = new StringBuffer();
        msg.append(event.toString());
        List<? extends VariableBinding> varBinds = event.getPDU()
                .getVariableBindings();
        if (varBinds != null && !varBinds.isEmpty()) {
            processVarBinds(varBinds, msg);
        }
    }

    abstract void processVarBinds(List<? extends VariableBinding> varBinds, StringBuffer msg);
}
