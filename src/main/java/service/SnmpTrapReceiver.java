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

import java.io.IOException;
import java.util.List;

public class SnmpTrapReceiver implements CommandResponder {

    private Snmp snmp;
    private final OidConfiguration conf;
    private final VarBindProcessor processor;


    public SnmpTrapReceiver(OidConfiguration conf, VarBindProcessor processor) {
        this.conf = conf;
        this.processor = processor;
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
        List<? extends VariableBinding> varBinds = event.getPDU()
                .getVariableBindings();
        if (varBinds != null && !varBinds.isEmpty()) {
            processor.processVarBinds(varBinds, conf);
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
