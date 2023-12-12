package service;

import configuration.OidConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.VariableBinding;
import ui.AlarmWindow;
import util.Constants;

import java.util.List;

public class IQHCOAlarmProcessor implements VarBindProcessor {
    private final Logger logger = LoggerFactory.getLogger(IQHCOAlarmProcessor.class);
    private final AlarmWindow ui;
    private final int alarmVarBind = 7;

    public IQHCOAlarmProcessor(AlarmWindow ui) {
        this.ui = ui;
    }

    @Override
    public void processVarBinds(List<? extends VariableBinding> varBinds, OidConfiguration conf, String ip) {
        String oidMessage = varBinds.get(alarmVarBind).toString();
        logger.debug("Получен OID: {}", oidMessage);
        for (String oid : conf.getAlarmOids().keySet()) {
            if (oidMessage.contains(oid)) {
                logger.debug("OID совпал с {}", oid);
                for (String input : conf.getInputs(ip).keySet()) {
                    if (oidMessage.startsWith(input, oid.length())) {
                        ui.setAlarmState(Constants.alarmColor, conf.getInputs(ip).get(input), conf.getAlarmOids().get(oid));
                    }
                }
            }
        }
    }
}
