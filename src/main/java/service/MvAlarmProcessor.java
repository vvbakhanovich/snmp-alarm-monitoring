package service;

import configuration.OidConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.smi.VariableBinding;
import org.springframework.stereotype.Service;
import ui.AlarmWindow;

import java.util.List;

import static util.AlarmStatus.FAIL;
import static util.AlarmStatus.OK;

/**
 * Snmp trap processor for GrassValley MV-821.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MvAlarmProcessor implements VarBindProcessor {

    private final AlarmWindow ui;

    @Override
    public void processVarBinds(final List<? extends VariableBinding> varBinds, final OidConfiguration conf,
                                final String ip) {
        int alarmVarBind = 2;
        final String oidMessage = varBinds.get(alarmVarBind).toString();
        log.debug("Получен OID: {}", oidMessage);
        for (String oid : conf.getAlarmOids().keySet()) {
            if (oidMessage.contains(oid) && !oidMessage.contains(OK.getStatus())) {
                log.debug("OID совпал с {}", oid);
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
