package com.example.drawau;

import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class DrawauController {
    public Button figuresButton;
    public Button magicButton;
    public Button saveButton;
    public Button openButton;
    public Button newButton;
    public FlowPane figuresPanel;
    public Pane workSpace;
    public Button classFigure;

    public static final double SIDE_PANEL_WIDTH = 40;

    public void initialize() {
        // add listener for button
        DrawauAnimation.setButtonWidthAnimation(
                newButton,
                openButton,
                saveButton,
                magicButton,
                figuresButton
        );
        DrawauAnimation.setClickFiguresButtonAnimation(figuresButton, figuresPanel);

        classFigure.setOnAction(action -> {
            DrawauFigureClass f1 =
                    new DrawauFigureClassBuilder()
                            .addName("Тест")
                            .addType(DrawauFigureClass.TYPE_INTERFACE)
                            .build();
            f1.locate(workSpace);
            f1.draw();
        });
    }

}