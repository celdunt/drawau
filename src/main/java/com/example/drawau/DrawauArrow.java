package com.example.drawau;

public abstract class DrawauArrow implements DrawauIFigure {
    protected double startX;
    protected double startY;
    protected double endX;
    protected double endY;

    public void setStartX(double value) { startX = value; }
    public void setStartY(double value) { startY = value; }

    public double getStartX() { return startX; }
    public double getStartY() { return startY; }

    public void setEndX(double value) { endX = value; }
    public void setEndY(double value) { endY = value; }

    public double getEndX() { return endX; }
    public double getEndY() { return endY; }
}
