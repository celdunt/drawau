package com.example.drawau;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class DrawauDependenceArrow extends DrawauArrow {

    Group G = new Group();

    @Override
    public void locate(Pane location) {
        location.getChildren().add(G);
    }

    @Override
    public void draw() {
        G.getChildren().clear();

        Polygon PL = new Polygon();
        PL.getPoints().addAll(
                startX, startY,
                startX - 5, startY + 10,
                startX + 5, startY + 10
        );
        PL.setFill(Color.BLACK);
        PL.setStroke(Color.BLACK);

        Line line = new Line();
        line.setStartX(startX);
        line.setStartY(startY + 10);
        line.setEndX(endX);
        line.setEndY(endY);
        line.setStroke(Color.BLACK);
        line.getStrokeDashArray().addAll(8d, 4d);

        G.getChildren().addAll(PL, line);
    }
}
