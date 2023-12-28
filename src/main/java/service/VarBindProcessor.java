package service;

import configuration.OidConfiguration;
import org.snmp4j.smi.VariableBinding;

import java.util.List;

/**
 * Variable binding processor.
 */
public interface VarBindProcessor {

    /**
     * @param varBinds list of variable bindings.
     * @param conf OidConfiguration implementation.
     * @param ip monitored device ip address.
     */
    void processVarBinds(List<? extends VariableBinding> varBinds, OidConfiguration conf, String ip);
}
