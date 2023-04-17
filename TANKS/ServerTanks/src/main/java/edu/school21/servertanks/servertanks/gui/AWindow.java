package edu.school21.servertanks.servertanks.gui;

import javax.swing.*;

public class AWindow {
    public JFrame jFrame;

    public AWindow(JFrame frame, int width, int height) {
        this.jFrame = frame;
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    public void setVisible(boolean visible) {
        jFrame.setVisible(visible);
    }
}
