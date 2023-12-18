package configuration;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Интерфейс поле, содержащее путь хранения конфигурационного файла. А также:
 * -
 * - список входов, относящихся к конкретному ip адресу
 * - список ip адресов устройств, которые требуется мониторить
 * - список для именования кнопок в ui
 */
public interface OidConfiguration {
    String CONFIG_DIR = new File(System.getProperty("user.dir"), "/config").toString();

    /**
     * Возвращает мапу, в которой хранится пара oid - наименование ошибки.
     *
     * @return мапа, в которой хранится пара oid - наименование ошибки.
     */
    Map<String, String> getAlarmOids();

    /**
     * Возвращает список мониторящихся входов и их имен для конкретного ip адреса.
     *
     * @param ip адрес устройства.
     * @return список мониторящихся входов и их имен для конкретного ip адреса.
     */
    Map<String, String> getInputs(String ip);

    /**
     * Возвращает сэт ip адресов устройств, которые подлежат мониторингу.
     *
     * @return сэт ip адресов устройств, которые подлежат мониторингу.
     */
    Set<String> getIps();

    /**
     * Возвращает список отображаемых названий для кнопок в ui.
     *
     * @return список отображаемых названий для кнопок в ui.
     */
    List<String> getButtonNames();
}
