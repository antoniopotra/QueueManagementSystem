package org.example.GUI;

import javax.swing.*;

public class Simulation extends JFrame {
    private JPanel panel;
    private JLabel statusLabel;
    private JScrollPane logScrollPane;
    private final JTextArea textArea1 = new JTextArea();

    public Simulation() {
    }

    public void start() {
        setTitle("Start Menu");
        setContentPane(panel);
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        logScrollPane.getViewport().add(textArea1);
    }

    public void finish() {
        statusLabel.setText("Simulation completed.");
    }

    public void updateLog(String message) {
        textArea1.append(message);
        textArea1.setCaretPosition(textArea1.getDocument().getLength());
    }

    public void showErrorPopup(String message) {
        JOptionPane.showMessageDialog(Simulation.this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
