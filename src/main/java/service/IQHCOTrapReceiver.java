package service;

import org.snmp4j.smi.VariableBinding;

import java.awt.*;
import java.util.List;

public class IQHCOTrapReceiver extends AbstractSnmpTrapReceiver {
    @Override
    void processVarBinds(List<? extends VariableBinding> varBinds, StringBuffer msg) {
        String oidMessage = varBinds.get(7).toString();
        for (String oid : alarmOids.keySet()) {
            if (oidMessage.contains(oid)) {
                for (String input : inputs.keySet()) {
                    if (oidMessage.startsWith(input, oid.length())) {
                        System.out.println(oid + " " + alarmOids.get(oid));
                        System.out.println(input + " " + inputs.get(input));
                        alarm.playAudio();
                        ui.setAlarmState(Color.RED, inputs.get(input));
                    }
                }
            }
        }
    }
}
