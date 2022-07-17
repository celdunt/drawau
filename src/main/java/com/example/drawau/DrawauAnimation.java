package com.example.drawau;

import javafx.animation.*;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class DrawauAnimation {


    public static void setButtonWidthAnimation(Button... buttons) {
        double basicWidth = buttons[0].getMinWidth();
        double newWidth = 120;

        for (Button b : buttons) {
            b.onMouseEnteredProperty().set(action -> {
                new Timeline(
                        new KeyFrame(Duration.seconds(0.1),
                            new KeyValue(b.minWidthProperty(), newWidth))).play();
                new Timeline(
                        new KeyFrame(Duration.seconds(0.05),
                            new KeyValue(b.textFillProperty(), Color.WHITE))).play();
            });
            b.onMouseExitedProperty().set(action -> {
                new Timeline(
                        new KeyFrame(Duration.seconds(0.15),
                            new KeyValue(b.minWidthProperty(), basicWidth))).play();
                new Timeline(
                        new KeyFrame(Duration.seconds(0.05),
                                new KeyValue(b.textFillProperty(), Color.TRANSPARENT))).play();
            });
        }
    }
    public static void setClickFiguresButtonAnimation(Button fbutton, FlowPane fpane) {
        fbutton.setOnMouseClicked(action -> {

            new Timeline(
                    new KeyFrame(Duration.seconds(0.15),
                            new KeyValue(fbutton.minWidthProperty(), 35))).play();
            new Timeline(
                    new KeyFrame(Duration.seconds(0.05),
                            new KeyValue(fbutton.textFillProperty(), Color.TRANSPARENT))).play();

            if (!fpane.visibleProperty().get()) {
                fbutton.onMouseEnteredProperty().set(nullAction -> {});

                fpane.visibleProperty().set(true);
                fpane.setTranslateX(10);
                fpane.setTranslateY(fbutton.getLayoutY()-fpane.getPrefHeight()/2+fbutton.getPrefHeight()/2);
            } else {
                fpane.visibleProperty().set(false);

                setButtonWidthAnimation(fbutton);
            }

        });
    }
}
