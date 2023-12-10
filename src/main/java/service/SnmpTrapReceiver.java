package service;

import configuration.OidConfiguration;
import org.snmp4j.*;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
import ui.AlarmWindow;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class SnmpTrapReceiver implements CommandResponder {

    private final AlarmWindow ui;
    private Snmp snmp;
    protected final OidConfiguration conf;


    public SnmpTrapReceiver(OidConfiguration conf, AlarmWindow ui) {
        this.conf = conf;
        this.ui = ui;
    }

    public void run() {
        try {
            init();
            snmp.addCommandResponder(this);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void processPdu(CommandResponderEvent event) {
        StringBuffer msg = new StringBuffer();
        msg.append(event.toString());
        List<? extends VariableBinding> varBinds = event.getPDU()
                .getVariableBindings();
        if (varBinds != null && !varBinds.isEmpty()) {
            processVarBinds(varBinds, msg);
        }
    }

    private void processVarBinds(List<? extends VariableBinding> varBinds, StringBuffer msg) {
        String oidMessage = varBinds.get(7).toString();
        for (String oid : conf.getAlarmOids().keySet()) {
            if (oidMessage.contains(oid)) {
                for (String input : conf.getInputs().keySet()) {
                    if (oidMessage.startsWith(input, oid.length())) {
                        System.out.println(oid + " " + conf.getAlarmOids().get(oid));
                        System.out.println(input + " " + conf.getInputs().get(input));
                        ui.setAlarmState(Color.RED, conf.getInputs().get(input), conf.getAlarmOids().get(oid));
                    }
                }
            }
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
    }
}
