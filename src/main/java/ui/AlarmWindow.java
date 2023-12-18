package ui;

import java.awt.*;

/**
 * Интерфейс для установки аварийного статуса при возникновении ошибки.
 */
public interface AlarmWindow {
    /**
     * Устанавливает аварийный статус.
     * @param buttonColor цвет кнопки для аварийного статуса
     * @param buttonName имя кнопки
     * @param message сообщение ошибки
     */
    void setAlarmState(Color buttonColor, String buttonName, String message);
}
