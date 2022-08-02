package com.example.drawau;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class DrawauDependenceArrow extends DrawauArrow {
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

        line.getStrokeDashArray().addAll(8d, 4d);

        G.getChildren().addAll(PL, line);
    }
}
