package service;

import configuration.OidConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.VariableBinding;
import ui.AlarmWindow;

import java.util.List;

import static util.Constants.FAIL_STATUS;
import static util.Constants.alarmColor;

public class MvAlarmProcessor implements VarBindProcessor {

    private final Logger logger = LoggerFactory.getLogger(IQHCOAlarmProcessor.class);
    private final AlarmWindow ui;

    private final int alarmVarBind = 2;

    public MvAlarmProcessor(AlarmWindow ui) {
        this.ui = ui;
    }

    @Override
    public void processVarBinds(List<? extends VariableBinding> varBinds, OidConfiguration conf, String ip) {
        String oidMessage = varBinds.get(alarmVarBind).toString();
        logger.debug("Получен OID: {}", oidMessage);
        for (String oid : conf.getAlarmOids().keySet()) {
            if (oidMessage.contains(oid) && oidMessage.contains(FAIL_STATUS)) {
                logger.debug("OID совпал с {}", oid);
                setAlarmState(conf, ip, oid, oidMessage);
            }
        }
    }

    private void setAlarmState(OidConfiguration conf, String ip, String oid, String oidMessage) {
        for (String input : conf.getInputs(ip).keySet()) {
            if (oidMessage.startsWith(input, oid.length())) {
                ui.setAlarmState(alarmColor, conf.getInputs(ip).get(input), conf.getAlarmOids().get(oid));
            }
        }
    }
}
