package com.mrbbot.hexsweeper;

import java.awt.*;
import java.awt.geom.Rectangle2D;

class Cell {
    private final Color colour;
    RenderedPolygon renderedPolygon;
    RenderedPolygon flagRenderedPolygon;

    int nearbyBombs = 0;

    private boolean uncovered = false;
    private boolean flagged = false;

    Cell(Color colour) {
        this.colour = colour;
    }

    boolean isBomb() {
        return nearbyBombs == -1;
    }

    boolean uncover() {
        if (uncovered)
            return false;
        uncovered = true;
        flagged = false;
        return true;
    }

    boolean isUncovered() {
        return uncovered;
    }

    void toggleFlag() {
        flagged = !flagged;
    }

    boolean hasFlag() {
        return flagged;
    }

    void draw(Graphics2D g, int mouseX, int mouseY) {
        g.setColor(uncovered ? Constants.UNCOVERED_COLOUR : (renderedPolygon.contains(mouseX, mouseY) ? Constants.HOVER_COLOUR : colour));
        renderedPolygon.draw(g);

        if (flagged) {
            g.setColor(Constants.FLAG_COLOUR);
            flagRenderedPolygon.draw(g);
        }

        g.setFont(Constants.NUMBER_FONT);
        FontMetrics fontMetrics = g.getFontMetrics();

        if (uncovered || Constants.DEBUG_ALWAYS_SHOW_NUMBER) {
            String text = "";
            Color colour = Color.WHITE;

            if (nearbyBombs >= 1) {
                text = String.valueOf(nearbyBombs);
                colour = Constants.NUMBER_COLOURS[nearbyBombs - 1];
            } else if (isBomb()) {
                text = "@";
            }

            if (!text.equals("")) {
                g.setColor(colour);
                Rectangle2D bounds = fontMetrics.getStringBounds(text, g);
                g.drawString(text, (int) (renderedPolygon.getCenterX() - (bounds.getWidth() / 2)), (int) (renderedPolygon.getCenterY() + 9));
            }
        }
    }
}
