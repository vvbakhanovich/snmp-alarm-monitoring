package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIWindow extends JFrame implements ActionListener {

    JButton button;
    Map<String, JButton> buttons = new HashMap<>();
    public UIWindow(Collection<String> buttonNames) {

//       GridLayout layout = new GridLayout(4, 2);
//       JLabel audioLoss = new JLabel("Audio Loss");
//       JLabel audioLevel = new JLabel("Audio Level");
//       JLabel black = new JLabel("Black");
//       audioLoss.setVerticalTextPosition(JLabel.TOP);
//       audioLevel.setVerticalTextPosition(JLabel.BOTTOM);
//       black.setVerticalTextPosition(JLabel.CENTER);
//       black.setFont(new Font("MV Boli", Font.PLAIN, 20));
//       black.setBounds(100,100,100,100);


//       getContentPane().add(audioLoss);
//       getContentPane().add(audioLevel);
//       getContentPane().add(black);
//       getContentPane().add(new JLabel("FREEZE"));

//        button = new JButton();
//        button.setBounds(200, 100, 400, 200);
////        button.setSize(400, 200);
//        button.setText("Test");
//        button.setFocusable(false);
//
//        setTitle("MultiViewer Alarm Receiver");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setPreferredSize(new Dimension(1200, 600));
////       setSize(500,500);
////       setResizable(true);
//        setVisible(true);
//        add(button);
////       add(black);
//       setLayout(null);
//        pack();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500);
        setLayout(new GridLayout(buttonNames.size(), 1, 0, 10));

        for (String buttonName : buttonNames) {
            button = new JButton(buttonName);
            button.addActionListener(this);
            add(button);
            buttons.put(buttonName, button);
        }

        setVisible(true);
    }

    public void setAlarmState(Color color, String name) {
        JButton mybutton = buttons.get(name);
        mybutton.setBackground(color);
    }

    public static void main(String[] args) {

        new UIWindow(List.of("1", "2", "3", "4", "5"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton o = (JButton) e.getSource();
            o.setBackground(null);
            System.out.println(o.getText());
        }
    }

}
