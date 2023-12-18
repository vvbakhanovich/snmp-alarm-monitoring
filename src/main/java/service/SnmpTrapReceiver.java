package service;

import configuration.OidConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.*;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

import java.io.IOException;
import java.util.List;

/**
 * Класс для приема и первичной обработке SNMP trap'ов. Для реализации приема SNMP trap используется библиотека snmp4j.
 * Мониторятся все сетевые интерфейсы по порту 162.
 */
public class SnmpTrapReceiver implements CommandResponder {

    protected final Logger log = LoggerFactory.getLogger(SnmpTrapReceiver.class);
    private Snmp snmp;
    private final OidConfiguration conf;
    private final VarBindProcessor processor;


    public SnmpTrapReceiver(final OidConfiguration conf, final VarBindProcessor processor) {
        this.conf = conf;
        this.processor = processor;
    }

    /**
     * Метод для запуска прослушивания SNMP trap'ов на всех сетевых интерфейсах по порту 162.
     */
    public void run() {
        try {
            init();
            snmp.addCommandResponder(this);
        } catch (Exception ex) {
            log.error("Ошибка при запуске SnmpTrapReceiver. {}", ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

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

        snmp = new Snmp(dispatcher, transport);
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());

        snmp.listen();
    }
}
