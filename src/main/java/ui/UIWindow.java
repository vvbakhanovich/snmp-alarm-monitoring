package ui;


import alarm.Alarm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static configuration.OidConfiguration.*;
import static util.AlarmStatus.*;

/**
 * Класс представляет собой JFrame, состоящий из двух областей. В левой части находится JPanel с кнопками, в правой
 * поле для отображения событий.
 */
public class UIWindow extends JFrame implements ActionListener, AlarmWindow {

    private static final Logger log = LoggerFactory.getLogger(UIWindow.class);

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final Alarm alarm;
    private final JTextArea logArea;
    private final Map<String, JButton> buttons = new HashMap<>();

    /**
     * В конструкторе настраиваются параметры отображения пользовательского интерфейса. Количество кнопок в интерфейсе
     * соответствует размеру списка buttonNames. Подписи на кнопках также соответствуют значениям в списке buttonNames.
     * @param buttonNames список отображаемых имен для кнопок
     * @param alarm реализация интерфейса Alarm
     */
    public UIWindow(final Collection<String> buttonNames, final Alarm alarm) {
        this.alarm = alarm;
        final JPanel buttonsPanel = new JPanel();
        buttonsPanel.setPreferredSize(new Dimension(250, 500));
        buttonsPanel.setLayout(new GridLayout(buttonNames.size(), 1, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (String buttonName : buttonNames) {
            JButton button = new JButton(buttonName);
            button.addActionListener(this);
            button.setBackground(Color.LIGHT_GRAY);
            button.setFocusable(false);
            button.setFont(new Font("Comic Sans", Font.BOLD, 25));
            button.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            buttonsPanel.add(button);
            buttons.put(buttonName, button);
        }

        logArea = new JTextArea(35, 30);
        logArea.setEditable(false);
        logArea.setAutoscrolls(true);
        logArea.setVisible(true);
        logArea.setLineWrap(true);
        logArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        final JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(416, 600));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setTitle("SNMP Alarm Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(670, 700));
        setLayout(new BorderLayout(20, 20));
        add(buttonsPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.EAST);
        setVisible(true);
        setBackground(Color.LIGHT_GRAY);
        setResizable(false);
        final ImageIcon icon = new ImageIcon(CONFIG_DIR + "/icon.jpg");
        setIconImage(icon.getImage());
        pack();
    }

    /**
     * Метод воспроизводит звуковой сигнал, а также поджигает аварийную индикацию на соответствующей кнопке.
     * @param buttonColor цвет кнопки для аварийного статуса
     * @param buttonName имя кнопки
     * @param alarmMessage сообщение ошибки
     */
    @Override
    public void setAlarmState(final Color buttonColor, final String buttonName, final String alarmMessage) {
        alarm.playAudio();
        final JButton alarmButton = buttons.get(buttonName);
        alarmButton.setBackground(buttonColor);
        logArea.append(String.format("%s - %s на %s\n", LocalDateTime.now().format(formatter), alarmMessage, buttonName));
        log.info(alarmMessage + " на " + buttonName);
    }

    /**
     * Если кнопка находится в аварийном статусе, то при нажатии на нее останавливается воспроизведение звукового
     * сигнала и цвет подсветки кнопки меняется на дефолтный.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            final JButton pressedButton = (JButton) e.getSource();
            if (pressedButton.getBackground().equals(FAIL.getColor())) {
                pressedButton.setBackground(OK.getColor());
                alarm.stopAudio();
                logArea.append(String.format("%s - Ошибка на канале %s подтверждена\n",
                        LocalDateTime.now().format(formatter), pressedButton.getText()));
                log.info("Ошибка на канале {} подтверждена", pressedButton.getText());
            }
        }
    }
}
