package com.example.drawau;

import javafx.scene.Group;
import javafx.scene.layout.Pane;

import java.lang.reflect.Type;

public abstract class DrawauArrow implements DrawauIFigure, DrawauIObserver
{

    public DrawauArrow()
    {
        G.onMouseClickedProperty().set(action ->
        {
            if (DrawauController.ACTION.equals(DrawauFigureClass.DELETE)
                && linkWorkSpace != null)
            {
                linkWorkSpace.getChildren().remove(G);
                DrawauController.ACTION = "null";
                DrawauController.subscribeStatusText.setValue("");
            }
        });
    }

    transient Group G = new Group();

    protected double startX;
    protected double startSelfPosX;
    protected double startY;

    protected double startHeightContainer;
    protected double endHeightContainer;

    protected double startWidthContainer;
    protected double endWidthContainer;

    protected double endX;
    protected double endSelfPosX;
    protected double endY;

    transient protected Pane linkWorkSpace;

    protected Type castType;

    protected final double conditionChangePosition = 150;


    public Type getCastType()
    {
        return castType;
    }

    public void setCastType(Type castType)
    {
        this.castType = castType;
    }

    public void setStartX(double value)
    {
        startX = value;
    }

    public void setStartSelfPosX(double value)
    {
        startSelfPosX = value;
    }

    public void setStartY(double value)
    {
        startY = value;
    }

    public double getStartX()
    {
        return startX;
    }

    public double getStartSelfPosX()
    {
        return startSelfPosX;
    }

    public double getStartY()
    {
        return startY;
    }

    public void setEndX(double value)
    {
        endX = value;
    }

    public void setEndSelfPosX(double value)
    {
        endSelfPosX = value;
    }

    public void setEndY(double value)
    {
        endY = value;
    }

    public double getEndX()
    {
        return endX;
    }

    public double getEndSelfPosX()
    {
        return endSelfPosX;
    }

    public double getEndY()
    {
        return endY;
    }

    public void setStartHeightContainer(double value)
    {
        startHeightContainer = value;
    }

    public void setEndHeightContainer(double value)
    {
        endHeightContainer = value;
    }

    public double getStartHeightContainer()
    {
        return startHeightContainer;
    }

    public double getEndHeightContainer()
    {
        return endHeightContainer;
    }

    public double getStartWidthContainer()
    {
        return startWidthContainer;
    }

    public void setStartWidthContainer(double startWidthContainer)
    {
        this.startWidthContainer = startWidthContainer;
    }

    public double getEndWidthContainer()
    {
        return endWidthContainer;
    }

    public void setEndWidthContainer(double endWidthContainer)
    {
        this.endWidthContainer = endWidthContainer;
    }

    public Group getG()
    {
        return G;
    }

    public void setG(Group g)
    {
        G = g;
    }

    public void initialize()
    {
        G = new Group();
    }

    @Override
    public void update(double sX, double sY, double eX, double eY)
    {
        startX = sX;
        startY = sY;
        endX = eX;
        endY = eY;

        draw();
    }
}
