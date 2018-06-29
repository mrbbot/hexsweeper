package com.mrbbot.hexsweeper;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

@SuppressWarnings({"WeakerAccess", "unused"})
public class RenderedPolygon {
    private double centerX, centerY, radius, angleOffset;
    private int sides;
    private Path2D.Double backgroundPath, path, innerPath;

    public RenderedPolygon(double centerX, double centerY, double radius, int sides) {
        this(centerX, centerY, radius, sides, 0);
    }

    public RenderedPolygon(double centerX, double centerY, double radius, int sides, double angleOffset) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.sides = sides;
        this.angleOffset = angleOffset;

        makePolygons();
    }

    private Path2D.Double makePolygon(double radius) {
        Path2D.Double path = new Path2D.Double();

        for (int i = 0; i < sides; i++) {
            double angle = (((2.0 * Math.PI) / sides) * (i + 0.5)) + angleOffset;

            double pathX = (Math.cos(angle) * radius) + centerX;
            double pathY = (Math.sin(angle) * radius) + centerY;

            if (i == 0) {
                path.moveTo(pathX, pathY);
            } else {
                path.lineTo(pathX, pathY);
            }
        }
        path.closePath();

        return path;
    }

    private void makePolygons() {
        backgroundPath = makePolygon(radius - 1);
        path = makePolygon(radius);
        innerPath = makePolygon(radius - 6);
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
        makePolygons();
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
        makePolygons();
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        makePolygons();
    }

    public boolean contains(int x, int y) {
        return path.contains(x, y);
    }

    public void draw(Graphics2D g) {
        Color start = g.getColor();
        g.setColor(start.darker());
        g.fill(backgroundPath);
        g.setColor(start);
        g.fill(innerPath);
    }
}
