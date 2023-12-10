package service;

import configuration.OidConfiguration;
import org.snmp4j.smi.VariableBinding;

import java.util.List;

public interface VarBindProcessor {

    void processVarBinds(List<? extends VariableBinding> varBinds, OidConfiguration conf);
}
