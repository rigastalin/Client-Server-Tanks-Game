package edu.school21.servertanks.servertanks.gui;

import javax.swing.*;
import java.awt.*;

public class EditConfigurationWindow extends AWindow {
    private JButton jButton;
    private JTextField textPort;

    public EditConfigurationWindow(JFrame frame, int width, int height) {
        super(frame, width, height);
        this.jFrame = frame;
        frame.getContentPane().add(init());
        frame.setVisible(true);
    }

    public JTextField getTextPort() {
        return textPort;
    }

    public JButton getjButton() {
        return jButton;
    }

    private JPanel init() {
        JPanel panelPort = BoxLayoutUtils.createVerticalPanel();
        JLabel panelPort1 = new JLabel("Hello, from Server");
        JLabel panelPort2 = new JLabel("Write number of the port: ");

        JPanel panelForPort = BoxLayoutUtils.createHorizontalPanel();
        JLabel labelPort = new JLabel("Port: ");
        textPort = new JTextField("8000");
        panelForPort.add(Box.createHorizontalStrut(10));
        panelForPort.add(labelPort);
        panelForPort.add(textPort);
        panelForPort.add(Box.createHorizontalStrut(10));

        JPanel panelForButton = new JPanel();
        panelForButton.setLayout(new GridLayout(1, 2));
        jButton = new JButton("OK");
        panelForButton.add(Box.createHorizontalStrut(150));
        panelForButton.add(jButton);

        panelPort.add(Box.createVerticalStrut(20));
        panelPort.add(panelPort1);
        panelPort.add(panelPort2);
        panelPort1.setHorizontalAlignment(JLabel.CENTER);
        panelPort2.setHorizontalAlignment(JLabel.CENTER);

        panelPort.add(panelForPort);
        panelPort.add(Box.createVerticalStrut(10));
        panelPort.add(panelForButton);
        panelPort.add(Box.createVerticalStrut(10));

        return  panelPort;
    }

    private class BoxLayoutUtils {
        public static JPanel createVerticalPanel() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            return panel;
        }

        public static JPanel createHorizontalPanel() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            return panel;
        }
    }
}
