package service;

import configuration.OidConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.VariableBinding;
import ui.AlarmWindow;

import java.util.List;

import static util.AlarmStatus.FAIL;
import static util.AlarmStatus.OK;

/**
 * Обработчик трапов от GrassValley MV-821.
 */
public class MvAlarmProcessor implements VarBindProcessor {

    private final Logger logger = LoggerFactory.getLogger(IQHCOAlarmProcessor.class);
    private final AlarmWindow ui;

    private final int alarmVarBind = 2;

    public MvAlarmProcessor(AlarmWindow ui) {
        this.ui = ui;
    }

    @Override
    public void processVarBinds(final List<? extends VariableBinding> varBinds, final OidConfiguration conf,
                                final String ip) {
        final String oidMessage = varBinds.get(alarmVarBind).toString();
        logger.debug("Получен OID: {}", oidMessage);
        for (String oid : conf.getAlarmOids().keySet()) {
            if (oidMessage.contains(oid) && !oidMessage.contains(OK.getName())) {
                logger.debug("OID совпал с {}", oid);
                setAlarmState(conf, ip, oid, oidMessage);
            }
        }
    }

    private void setAlarmState(final OidConfiguration conf, final String ip, final String oid, final String oidMessage) {
        for (String input : conf.getInputs(ip).keySet()) {
            if (oidMessage.startsWith(input, oid.length())) {
                ui.setAlarmState(FAIL.getColor(), conf.getInputs(ip).get(input), conf.getAlarmOids().get(oid));
            }
        }
    }
}
