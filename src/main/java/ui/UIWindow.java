package ui;


import alarm.Alarm;
import alarm.AudioAlarm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.Constants.formatter;

public class UIWindow extends JFrame implements ActionListener {

    private static final Logger log = LoggerFactory.getLogger(UIWindow.class);

    private final Alarm alarm;
    private final JTextArea logArea;
    private final Map<String, JButton> buttons = new HashMap<>();

    public UIWindow(Collection<String> buttonNames) {
        alarm = new AudioAlarm();
        JPanel buttonsPanel = new JPanel();
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

        JScrollPane scrollPane = new JScrollPane(logArea);
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
        pack();
    }

    public void setAlarmState(Color color, String name, String message) {
        alarm.playAudio();
        JButton alarmButton = buttons.get(name);
        alarmButton.setBackground(color);
        logArea.append(message + " на " + name + " в " + LocalDateTime.now().format(formatter));
        log.info(message + " на " + name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton pressedButton = (JButton) e.getSource();
            pressedButton.setBackground(Color.LIGHT_GRAY);
            logArea.append(String.format("%s - Ошибка на канале %s подтверждена\n",
                    LocalDateTime.now().format(formatter), pressedButton.getText()));
            log.info("Ошибка на канале {} подтверждена", pressedButton.getText());
        }
    }

    public static void main(String[] args) {

        new UIWindow(List.of("Test +1", "Test +2", "Test MSK", "TEST TEST +4", "5"));
        System.out.println(System.getProperty("user.dir"));
    }

}
