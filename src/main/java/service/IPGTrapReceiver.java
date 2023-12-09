package service;

import org.snmp4j.smi.VariableBinding;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class IPGTrapReceiver extends AbstractSnmpTrapReceiver{

    Map<String, String> alarmOids = new HashMap<>();
    Map<Integer, String> inputs = new HashMap<>();

    public IPGTrapReceiver() {
        alarmOids.put("Audio loss", "1.3.6.1.4.1.6419.1.1.5.2.2.1.1.75.");
        alarmOids.put("Low audio level", "1.3.6.1.4.1.6419.1.1.5.2.2.1.1.27.");
        alarmOids.put("Black", "1.3.6.1.4.1.6419.1.1.5.2.2.1.1.2.");
        alarmOids.put("Freeze", "1.3.6.1.4.1.6419.1.1.5.2.2.1.1.3.");

    }
    @Override
    void processVarBinds(List<? extends VariableBinding> varBinds, StringBuffer msg) {
        if (varBinds.get(2).toString().contains("IPG")) {
            System.out.println("====================");
            System.out.println("Список VarBinds");
            for (int i = 0; i < varBinds.size(); i++) {
                System.out.println(varBinds.get(i));
            }
            System.out.println("====================");

            Iterator<? extends VariableBinding> varIter = varBinds.iterator();
            while (varIter.hasNext()) {
                VariableBinding var = varIter.next();
                msg.append(var.toString()).append(";");
            }
        }
    }
}
