package service;

import configuration.OidConfiguration;
import org.snmp4j.smi.VariableBinding;
import ui.AlarmWindow;

import java.util.List;

public class FailAlarmVarBindProcessor implements VarBindProcessor {
    private final AlarmWindow ui;

    public FailAlarmVarBindProcessor(AlarmWindow ui) {
        this.ui = ui;
    }

    @Override
    public void processVarBinds(List<? extends VariableBinding> varBinds, OidConfiguration conf) {
        String oidMessage = varBinds.get(7).toString();
        for (String oid : conf.getAlarmOids().keySet()) {
            if (oidMessage.contains(oid)) {
                for (String input : conf.getInputs().keySet()) {
                    if (oidMessage.startsWith(input, oid.length())) {
                        System.out.println(oid + " " + conf.getAlarmOids().get(oid));
                        System.out.println(input + " " + conf.getInputs().get(input));
                    }
                }
            }
        }
    }
}
