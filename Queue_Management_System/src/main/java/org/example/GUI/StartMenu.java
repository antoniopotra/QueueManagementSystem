package org.example.GUI;

import javax.swing.*;
import java.awt.event.ActionListener;

public class StartMenu extends JFrame {
    private JPanel panel;
    private JLabel clientsLabel;
    private JLabel queuesLabel;
    private JLabel simulationTimeLabel;
    private JLabel arrivalTimeLabel;
    private JLabel serviceTimeLabel;
    private JTextField clientsText;
    private JTextField queuesText;
    private JTextField simulationTimeText;
    private JTextField minArrivalTimeText;
    private JTextField maxArrivalTimeText;
    private JTextField minServiceTimeText;
    private JTextField maxServiceTimeText;
    private JButton simulationButton;

    public StartMenu() {
        setTitle("Start Menu");
        setContentPane(panel);
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public String clients() {
        return clientsText.getText();
    }

    public String queues() {
        return queuesText.getText();
    }

    public String simulationTime() {
        return simulationTimeText.getText();
    }

    public String minArrivalTime() {
        return minArrivalTimeText.getText();
    }

    public String maxArrivalTime() {
        return maxArrivalTimeText.getText();
    }

    public String minServiceTime() {
        return minServiceTimeText.getText();
    }

    public String maxServiceTime() {
        return maxServiceTimeText.getText();
    }

    public void addSimulationListener(ActionListener actionListener) {
        simulationButton.addActionListener(actionListener);
    }

    public void showErrorPopup(String message) {
        JOptionPane.showMessageDialog(StartMenu.this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
