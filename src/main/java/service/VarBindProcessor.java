package service;

import configuration.OidConfiguration;
import org.snmp4j.smi.VariableBinding;

import java.util.List;

/**
 * Интерфейс для обработки variable bindings в SNMP trap.
 */
public interface VarBindProcessor {

    /**
     * Метод обработки variable bindings в SNMP trap.
     * @param varBinds список variable bindings.
     * @param conf конфигурация, реализующая OidConfiguration
     * @param ip ip-адрес устройства, с которого ожидается trap
     */
    void processVarBinds(List<? extends VariableBinding> varBinds, OidConfiguration conf, String ip);
}
