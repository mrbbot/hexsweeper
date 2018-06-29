package com.mrbbot.hexsweeper;

import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame {
    private GameFrame() {
        super("GamePanel");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        /*JPanel overlay = new JPanel();
        overlay.setBorder(new TitledBorder("Debug"));
        overlay.setLayout(new GridLayout(2, 1));

        JCheckBox connectionsCheckBox = new JCheckBox("Show Connections");
        JCheckBox numbersCheckBox = new JCheckBox("Show Numbers");

        overlay.add(connectionsCheckBox);
        overlay.add(numbersCheckBox);*/

        add(new GamePanel(), BorderLayout.CENTER);

        setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameFrame::new);
    }
}
