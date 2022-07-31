package com.example.drawau;

import javafx.css.converter.PaintConverter;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;


public class DrawauInheritArrow extends DrawauArrow{
    Group G = new Group();

    public DrawauInheritArrow() {}

    @Override
    public void locate(Pane location) {
        location.getChildren().add(G);
    }

    @Override
    public void draw() {
        G.getChildren().clear();

        Polygon PL = new Polygon();
        Line line = new Line();

        if (startY < endY) {
            double sY = startY + startHeightContainer;

            line.setStartX(startX);
            line.setStartY(sY);
            line.setEndX(endX);
            line.setEndY(endY - 10);

            PL.getPoints().addAll(
                    endX, endY - 10,
                    endX - 5, endY - 10,
                    endX, endY,
                    endX + 5, endY - 10
            );
            PL.setFill(Color.BLACK);
            PL.setStroke(Color.BLACK);
        } else if (startY > endY) {
            double eY = endY + endHeightContainer;

            PL.getPoints().addAll(
                    endX, eY,
                    endX - 5, eY + 10,
                    endX + 5, eY + 10
            );
            PL.setFill(Color.BLACK);
            PL.setStroke(Color.BLACK);

            line.setStartX(startX);
            line.setStartY(startY);
            line.setEndX(endX);
            line.setEndY(eY + 10);
        }

        G.getChildren().addAll(PL, line);
    }

}
