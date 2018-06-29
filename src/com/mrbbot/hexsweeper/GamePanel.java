package com.mrbbot.hexsweeper;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

class GamePanel extends JPanel {
    private final Hexsweeper hexsweeper;
    private int mouseX = Integer.MIN_VALUE, mouseY = Integer.MIN_VALUE;

    GamePanel() {
        hexsweeper = new Hexsweeper(Constants.HONEYCOMB_SIZE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                boolean won = hexsweeper.onClick(e.getButton() == MouseEvent.BUTTON1, mouseX, mouseY);
                repaint();
                if (won) {
                    JOptionPane.showMessageDialog(null, "You won!", "Yay!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                repaint();
            }
        });
    }

    private int lastWidth = -1, lastHeight = -1;

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.clearRect(0, 0, getWidth(), getHeight());

        int width = getWidth();
        int height = getHeight();
        if (width != lastWidth || height != lastHeight) {
            lastWidth = width;
            lastHeight = height;
            hexsweeper.generateRenderedPolygons(width / 2, height / 2);
        }

        hexsweeper.draw(g, mouseX, mouseY);
    }
}
