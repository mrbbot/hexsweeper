package com.mrbbot.hexsweeper;

import java.awt.*;

class Constants {
    static final int HONEYCOMB_SIZE = 10;
    static final double PERCENT_FILLED = 20; //25

    @SuppressWarnings("CanBeFinal")
    static boolean DEBUG_CONNECTIONS = false;
    @SuppressWarnings("CanBeFinal")
    static boolean DEBUG_ALWAYS_SHOW_NUMBER = false;

    static final double HEX_RADIUS = 30;
    static final double FLAG_RADIUS = 12;
    static final double HEX_WIDTH = Math.sqrt(3) / 2.0 * HEX_RADIUS;

    static final Font NUMBER_FONT = new Font("Segoe UI", Font.BOLD, 24);

    static final Color[] NUMBER_COLOURS = {
            new Color(63, 81, 181),
            new Color(76, 175, 80),
            new Color(244, 67, 54),
            new Color(103, 58, 183),
            new Color(3, 169, 244),
            new Color(171, 71, 188)
    };
    static final Color[] HEX_COLOURS = {
            Color.GRAY.darker().darker(),
            Color.GRAY.darker().darker().darker(),
            Color.GRAY.darker().darker().darker().darker()
    };

    static final Color UNCOVERED_COLOUR = Color.LIGHT_GRAY;
    static final Color HOVER_COLOUR = Color.LIGHT_GRAY.brighter();
    static final Color FLAG_COLOUR = new Color(255, 193, 7);
}
