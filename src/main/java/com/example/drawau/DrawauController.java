package com.example.drawau;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

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
    public ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(
            DrawauFigureClass.TYPE_INTERFACE,
            DrawauFigureClass.TYPE_SIMPLE,
            DrawauFigureClass.TYPE_ABSTRACT));
    public TextField nameBox = new TextField();
    public List<HBox> fieldsBox = new ArrayList<>();
    public List<DrawauFigureClass.FieldProperty> fields = new ArrayList<>();
    public List<DrawauFigureClass.FieldProperty> methods = new ArrayList<>();

    public VBox classFigurePanel;

    public void initialize() {
        // add class figure panel content
        HBox head = new HBox();
        head.setAlignment(Pos.CENTER);

        head.setStyle(DrawauFigureClass.setMargin("10 0 0 0"));
        nameBox.setStyle(DrawauFigureClass.setMargin("0 0 0 10"));
        nameBox.setPrefSize(100, 25);
        choiceBox.setPrefWidth(100);

        head.getChildren().add(choiceBox);
        head.getChildren().add(nameBox);

        classFigurePanel.getChildren().add(head);
        classFigurePanel.getChildren().add(addFieldBox());


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

    private HBox addFieldBox() {
        HBox fieldBox = new HBox();
        fieldBox.setAlignment(Pos.CENTER);
        fieldBox.setStyle(DrawauFigureClass.setMargin("10 0 0 0"));

        TextField fieldName = new TextField();
        TextField fieldType = new TextField();
        DrawauFigureClass.FieldProperty field = new DrawauFigureClass.FieldProperty("", "");

        fieldName.setStyle(DrawauFigureClass.setMargin("0 5 0 0"));
        fieldType.setStyle(DrawauFigureClass.setMargin("0 0 0 5"));

        fieldName.setPrefSize(100, 25);
        fieldType.setPrefSize(100, 25);

        fieldName.setPromptText("Имя");
        fieldType.setPromptText("Тип");


        fieldName.textProperty().addListener(change -> {
            field.setName(fieldName.getText());
        });
        fieldType.textProperty().addListener(change -> {
            field.setType(fieldType.getText());
        });

        fields.add(field);

        fieldBox.getChildren().add(fieldName);
        fieldBox.getChildren().add(fieldType);

        return fieldBox;
    }
}