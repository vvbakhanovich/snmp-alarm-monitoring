package ui;


import alarm.Alarm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

import static configuration.OidConfiguration.CONFIG_DIR;
import static util.AlarmStatus.FAIL;
import static util.AlarmStatus.OK;

/**
 * This JFrame id divided in two logical areas. Left area contains JPanel with buttons and area to the right is event
 * log list.
 */
@Component
@Slf4j
public class UIWindow extends JFrame implements ActionListener, AlarmWindow {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final Alarm alarm;
    private final JTextArea logArea;
    private final Map<String, JButton> buttons = new HashMap<>();

    /**
     * Initializes JFrame and populates it with buttons according to buttonNames.
     * @param buttonNames list of visible names for buttons
     * @param alarm implementation of Alarm interface for playing alarm.
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
     * Play alarm and set alarm background for corresponding button
     * @param buttonColor button background color
     * @param buttonName button name, matches input name
     * @param alarmMessage alarm message that should be displayed in event log area
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
     * Stop alarm sound playback and set default (OK) button background color.
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
