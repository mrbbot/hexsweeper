package com.mrbbot.hexsweeper;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

class Hexsweeper {
    private final Random random;
    private final ArrayList<Point> cellPoints;
    private final Cell[][] cells;
    private boolean filled;
    private boolean playing;

    @SuppressWarnings("SameParameterValue")
    Hexsweeper(int radius) {
        random = new Random();
        cellPoints = new ArrayList<>();
        filled = false;
        playing = true;

        int diameter = (radius * 2) - 1;
        cells = new Cell[diameter][diameter];

        int length = radius;
        for (int i = 0; i < diameter; i++) {
            int start = radius - ((length + 1) / 2);

            for (int x = 0; x < length; x++) {
                Point point = new Point(x + start, i);
                cells[point.y][point.x] = new Cell(Constants.HEX_COLOURS[(int) (Math.random() * 3)]);
                cellPoints.add(point);
            }

            length += i < (radius - 1) ? 1 : -1;
        }
    }

    void generateRenderedPolygons(int centerX, int centerY) {
        for (int y = 0; y < cells.length; y++) {
            int yFromMiddle = y - ((cells.length) / 2);
            double offsetX = (Math.abs(yFromMiddle) % 2 == 1) ? -Constants.HEX_WIDTH : 0;

            for (int x = 0; x < cells[y].length; x++) {
                if (cells[y][x] != null) {
                    int xFromMiddle = x - ((cells[y].length) / 2);

                    double hexX = Math.round(centerX + offsetX + (2.0 * Constants.HEX_WIDTH * xFromMiddle));
                    double hexY = (double) centerY + Math.round(yFromMiddle * 1.5 * Constants.HEX_RADIUS);

                    cells[y][x].renderedPolygon = new RenderedPolygon(hexX, hexY, Constants.HEX_RADIUS, 6);
                    cells[y][x].flagRenderedPolygon = new RenderedPolygon(hexX, hexY, Constants.FLAG_RADIUS, 3, Math.PI / 2);
                }
            }
        }
    }

    private Point findPointAt(int screenX, int screenY) {
        for (int y = 0; y < cells.length; y++) {
            for (int x = 0; x < cells[y].length; x++) {
                if (cells[y][x] != null && cells[y][x].renderedPolygon.contains(screenX, screenY)) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    private ArrayList<Point> findNeighbours(int x, int y) {
        ArrayList<Point> neighbours = new ArrayList<>();

        int yFromMiddle = y - ((cells.length) / 2);
        boolean offset = yFromMiddle % 2 == 0;

        boolean canGoLeft = x > 0;
        boolean canGoRight = x < cells[0].length - 1;
        boolean canGoUp = y > 0;
        boolean canGoDown = y < cells.length - 1;

        // Horizontal
        if (canGoLeft && cells[y][x - 1] != null)
            neighbours.add(new Point(x - 1, y));
        if (canGoRight && cells[y][x + 1] != null)
            neighbours.add(new Point(x + 1, y));

        // Top
        if (!offset && canGoLeft && canGoUp && cells[y - 1][x - 1] != null)
            neighbours.add(new Point(x - 1, y - 1));
        if (canGoUp && cells[y - 1][x] != null)
            neighbours.add(new Point(x, y - 1));
        if (offset && canGoRight && canGoUp && cells[y - 1][x + 1] != null)
            neighbours.add(new Point(x + 1, y - 1));

        // Bottom
        if (!offset && canGoLeft && canGoDown && cells[y + 1][x - 1] != null)
            neighbours.add(new Point(x - 1, y + 1));
        if (canGoDown && cells[y + 1][x] != null)
            neighbours.add(new Point(x, y + 1));
        if (offset && canGoRight && canGoDown && cells[y + 1][x + 1] != null)
            neighbours.add(new Point(x + 1, y + 1));

        return neighbours;
    }

    private void fillCells(int notX, int notY) {
        cellPoints.removeIf((point -> point.x == notX && point.y == notY));
        int total = (int) (cellPoints.size() * (Constants.PERCENT_FILLED / 100.0));
        for (int i = 0; i < total; i++) {
            Point point = cellPoints.remove(random.nextInt(cellPoints.size()));
            cells[point.y][point.x].nearbyBombs = -1;
        }

        for (int y = 0; y < cells.length; y++) {
            for (int x = 0; x < cells[y].length; x++) {
                if (cells[y][x] != null && !cells[y][x].isBomb()) {
                    cells[y][x].nearbyBombs = findNeighbours(x, y).stream()
                            .mapToInt(p -> cells[p.y][p.x].nearbyBombs == -1 ? 1 : 0)
                            .sum();
                }
            }
        }
    }

    private void uncover(int x, int y) {
        if (cells[y][x].uncover() && cells[y][x].nearbyBombs == 0) {
            findNeighbours(x, y).stream()
                    .filter(p -> !cells[p.y][p.x].isBomb())
                    .forEach(p -> uncover(p.x, p.y));
        }
    }

    private void uncoverBombs() {
        for (Cell[] column : cells) {
            for (Cell cell : column) {
                if (cell != null && cell.isBomb()) cell.uncover();
            }
        }
    }

    private boolean hasWon() {
        for (Cell[] column : cells) {
            for (Cell cell : column) {
                if (cell == null) continue;
                if (cell.isBomb() && !cell.hasFlag()) return false;
                if (!cell.isBomb() && !cell.isUncovered()) return false;
            }
        }
        return true;
    }

    private void win() {
        for (Cell[] column : cells) {
            for (Cell cell : column) {
                if(cell != null) {
                    if(cell.isBomb() && !cell.hasFlag()) cell.toggleFlag();
                    if(!cell.isBomb() && !cell.isUncovered()) cell.uncover();
                }
            }
        }
    }

    boolean onClick(boolean leftClick, int x, int y) {
        if (!playing) return false;

        Point point = findPointAt(x, y);
        if (point != null) {
            if (!filled) {
                filled = true;
                fillCells(point.x, point.y);
                uncover(point.x, point.y);
            } else {
                if (leftClick) {
                    if (!cells[point.y][point.x].hasFlag()) {
                        uncover(point.x, point.y);
                        if (cells[point.y][point.x].isBomb()) {
                            playing = false;
                            uncoverBombs();
                        }
                    }
                } else if (!cells[point.y][point.x].isUncovered()) {
                    cells[point.y][point.x].toggleFlag();
                }
            }

            if (hasWon()) {
                playing = false;
                return true;
            }
        }

        return false;
    }

    void draw(Graphics2D g, int mouseX, int mouseY) {
        for (Cell[] column : cells) {
            for (Cell cell : column) {
                if (cell != null) {
                    cell.draw(g, playing ? mouseX : Integer.MIN_VALUE, playing ? mouseY : Integer.MIN_VALUE);
                }
            }
        }

        if (Constants.DEBUG_CONNECTIONS)
            drawConnections(g, mouseX, mouseY);
    }

    private void drawConnections(Graphics2D g, int mouseX, int mouseY) {
        for (int y = 0; y < cells.length; y++) {
            for (int x = 0; x < cells[y].length; x++) {
                Cell cell = cells[y][x];
                if (cell != null && cells[y][x].renderedPolygon.contains(mouseX, mouseY)) {
                    for (Point p : findNeighbours(x, y)) {
                        Cell c = cells[p.y][p.x];
                        g.setColor(Color.RED);
                        g.drawLine(
                                (int) cell.renderedPolygon.getCenterX(),
                                (int) cell.renderedPolygon.getCenterY(),
                                (int) c.renderedPolygon.getCenterX(),
                                (int) c.renderedPolygon.getCenterY()
                        );
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int y = 0; y < cells.length; y++) {
            for (int x = 0; x < cells[y].length; x++) {
                s.append(cells[y][x] != null ? '#' : ' ');
                if (x < cells[y].length - 1) s.append(" ");
            }
            if (y < cells.length - 1) s.append("\n");
        }
        return s.toString();
    }
}
