package edu.school21.servertanks.servertanks.gui;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ViewGuiClient {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 250;
    private static int port = 0;

    public static int createConfiguration() {
        EditConfigurationWindow editConfigurationWindow = new EditConfigurationWindow(new JFrame("CONFIGURATION"), WIDTH, HEIGHT);
        JTextField textPort = editConfigurationWindow.getTextPort();
        JButton jButton = editConfigurationWindow.getjButton();

        jButton.addActionListener(actionEvent -> {
            try {
                port = Integer.parseInt(textPort.getText());
            } catch (Exception e) {
                port = 0;
            }
            editConfigurationWindow.setVisible(false);
        });

        editConfigurationWindow.jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                editConfigurationWindow.jFrame.dispose();
            }
        });

        editConfigurationWindow.jFrame.pack();
        editConfigurationWindow.jFrame.setVisible(true);

        while(editConfigurationWindow.jFrame.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return port;
    }
}
