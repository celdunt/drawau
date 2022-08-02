package com.example.drawau;

import javafx.scene.Group;

public abstract class DrawauArrow implements DrawauIFigure {
    transient Group G = new Group();

    protected double startX;
    protected double startSelfPosX;
    protected double startY;

    protected double startHeightContainer;
    protected double endHeightContainer;

    protected double endX;
    protected double endSelfPosX;
    protected double endY;

    public void setStartX(double value) { startX = value; }
    public void setStartSelfPosX(double value) { startSelfPosX = value; }
    public void setStartY(double value) { startY = value; }

    public double getStartX() { return startX; }
    public double getStartSelfPosX() { return startSelfPosX; }
    public double getStartY() { return startY; }

    public void setEndX(double value) { endX = value; }
    public void setEndSelfPosX(double value) { endSelfPosX = value; }
    public void setEndY(double value) { endY = value; }

    public double getEndX() { return endX; }
    public double getEndSelfPosX() { return endSelfPosX; }
    public double getEndY() { return endY; }

    public void setStartHeightContainer(double value) { startHeightContainer = value; }
    public void setEndHeightContainer(double value) { endHeightContainer = value; }

    public double getStartHeightContainer() { return startHeightContainer; }
    public double getEndHeightContainer() { return endHeightContainer; }

    public void initialize() {
        G = new Group();
    }

    public void notifyAction(double sX, double sY, double eX, double eY) {
        startX = sX;
        startY = sY;
        endX = eX;
        endY = eY;

        draw();
    }
}
