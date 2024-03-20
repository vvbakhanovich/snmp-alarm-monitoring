package service;

import configuration.OidConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.*;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Class for receiving and and processing SNMP traps. Based on org.snmp4j library.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SnmpTrapReceiver implements CommandResponder {
    private final OidConfiguration conf;
    private final VarBindProcessor processor;

    /**
     * Starts listening and processing SNMP trap messages.
     */
    public void run() {
        try {
            init();
        } catch (Exception ex) {
            log.error("Ошибка при запуске SnmpTrapReceiver. {}", ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Process an incoming request, report or notification PDU.
     *
     * @param event a CommandResponderEvent instance containing the PDU to process and some additional information
     *              returned by the message processing model that decoded the SNMP message.
     */
    @Override
    public void processPdu(final CommandResponderEvent event) {
        log.debug("Получен трап от " + event.getPeerAddress());
        for (String ip : conf.getIps()) {
            if (event.getPeerAddress().getSocketAddress().toString().contains(ip)) {
                log.debug("Адрес совпал с " + ip);
                final List<? extends VariableBinding> varBinds = event.getPDU().getVariableBindings();
                if (varBinds != null && !varBinds.isEmpty()) {
                    processor.processVarBinds(varBinds, conf, ip);
                }
            }
        }

    }

    private void init() throws IOException {
        final ThreadPool threadPool = ThreadPool.create("Trap", 10);
        final MultiThreadedMessageDispatcher dispatcher = new MultiThreadedMessageDispatcher(threadPool,
                new MessageDispatcherImpl());
        final Address listenAddress = GenericAddress.parse(System.getProperty(
                "snmp4j.listenAddress", "udp:0.0.0.0/162"));
        final TransportMapping<?> transport;

        if (listenAddress instanceof UdpAddress) {
            transport = new DefaultUdpTransportMapping((UdpAddress) listenAddress);
        } else {
            transport = new DefaultTcpTransportMapping((TcpAddress) listenAddress);
        }

        final Snmp snmp = new Snmp(dispatcher, transport);
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
        snmp.addCommandResponder(this);

        snmp.listen();
    }
}
