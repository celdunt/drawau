package com.example.drawau;

import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

public class DrawauController {
    public Button figuresButton;
    public Button magicButton;
    public Button saveButton;
    public Button openButton;
    public Button newButton;
    public FlowPane figuresPanel;

    public void initialize() {
        DrawauAnimation.setButtonWidthAnimation(
                newButton,
                openButton,
                saveButton,
                magicButton,
                figuresButton
        );
        DrawauAnimation.setClickFiguresButtonAnimation(figuresButton, figuresPanel);
    }

}