package com.example.drawau;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
    public List<DrawauFigureClass.FieldProperty> fields = new ArrayList<>();
    public List<DrawauFigureClass.FieldProperty> methods = new ArrayList<>();

    public VBox classFigurePanel;
    public Button inheritArrow;
    public Button dependenceArrow;
    public Button aggregationArrow;

    List<DrawauFigureClass> flist = new ArrayList<>();
    public static List<DrawauArrow> arrows = new ArrayList<>();

    public static String ACTION = "null";

    public void initialize() {
        // new button behavior
        newButton.setOnAction(action -> {
            workSpace.getChildren().clear();
            workSpace.getChildren().addAll(
                    classFigurePanel,
                    figuresPanel
            );
        });

        // add arrow button behavior
        inheritArrow.setOnAction(action -> {
            DrawauInheritArrow ar = new DrawauInheritArrow();
            ar.locate(workSpace);
            DrawauController.arrows.add(ar);
            ACTION = DrawauFigureClass.SUBSCRIBE_OUT;


            // region месаге
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Начинаем подписывать!");

            alert.showAndWait();
            // endregion
        });

        dependenceArrow.setOnAction(action -> {
            DrawauDependenceArrow ar = new DrawauDependenceArrow();
            ar.locate(workSpace);
            DrawauController.arrows.add(ar);
            ACTION = DrawauFigureClass.SUBSCRIBE_OUT;
        });

        aggregationArrow.setOnAction(action -> {
            DrawauAggregationArrow ar = new DrawauAggregationArrow();
            ar.locate(workSpace);
            DrawauController.arrows.add(ar);
            ACTION = DrawauFigureClass.SUBSCRIBE_OUT;
        });

        // add class figure panel behavior
        AtomicReference<Double> pressContainerX = new AtomicReference<>((double) 0);
        AtomicReference<Double> pressContainerY = new AtomicReference<>((double) 0);
        classFigurePanel.onMousePressedProperty().set(action -> {
            pressContainerX.set(action.getX());
            pressContainerY.set(action.getY());
        });
        classFigurePanel.setOnMouseDragged(action -> {
            classFigurePanel.setLayoutX(action.getSceneX() - pressContainerX.get() - DrawauController.SIDE_PANEL_WIDTH);
            classFigurePanel.setLayoutY(action.getSceneY() - pressContainerY.get());
        });

        // add class figure panel content
        addClassFigurePanelContent(classFigurePanel);

        // add listener for button
        DrawauAnimation.setButtonWidthAnimation(
                newButton,
                openButton,
                saveButton,
                magicButton,
                figuresButton
        );
        DrawauAnimation.setClickFiguresButtonAnimation(figuresButton, figuresPanel);

        //add classfigure
        classFigure.setOnAction(action -> {
            classFigurePanel.setVisible(true);
        });
    }

    private void addClassFigurePanelContent(VBox classFigurePanel) {
        classFigurePanel.toFront();

        HBox head = new HBox();
        head.setAlignment(Pos.CENTER);

        head.setStyle(DrawauFigureClass.setMargin("10 0 0 0"));
        nameBox.setStyle(DrawauFigureClass.setMargin("0 0 0 10"));
        nameBox.setPrefSize(105, 25);
        nameBox.setPromptText("Имя класса");
        nameBox.setText("");
        choiceBox.setPrefWidth(95);

        head.getChildren().add(choiceBox);
        head.getChildren().add(nameBox);

        classFigurePanel.getChildren().add(head);

        VBox verticalFieldBox = new VBox();
        verticalFieldBox.setAlignment(Pos.TOP_CENTER);

        VBox verticalMethodBox = new VBox();
        verticalMethodBox.setAlignment(Pos.TOP_CENTER);

        Button addFieldBox = new Button("Добавить поля");
        Button addMethodBox = new Button("Добавить методы");

        addFieldBox.setStyle(DrawauFigureClass.setMargin("5 0 5 0"));
        addMethodBox.setStyle(DrawauFigureClass.setMargin("5 0 5 0"));

        addFieldBox.setMinSize(150, 40);
        addFieldBox.setMaxSize(150, 40);
        addFieldBox.setPrefSize(150, 40);

        addMethodBox.setMinSize(150, 40);
        addMethodBox.setMaxSize(150, 40);
        addMethodBox.setPrefSize(150, 40);

        addFieldBox.setOnAction(action -> {
            verticalFieldBox.getChildren().add(addFieldBox(fields));
        });

        addMethodBox.setOnAction(action -> {
            verticalMethodBox.getChildren().add(addFieldBox(methods));
        });

        classFigurePanel.getChildren().add(verticalFieldBox);
        classFigurePanel.getChildren().add(addFieldBox);

        classFigurePanel.getChildren().add(verticalMethodBox);
        classFigurePanel.getChildren().add(addMethodBox);



        verticalFieldBox.getChildren().add(addFieldBox(fields));

        verticalMethodBox.getChildren().add(addFieldBox(methods));

        Button acceptButton = new Button("Готово");

        acceptButton.setPrefSize(150, 40);
        acceptButton.setMinSize(150, 40);
        acceptButton.setMaxSize(150, 40);

        acceptButton.setStyle(DrawauFigureClass.setMargin("5 0 5 0"));

        acceptButton.setOnAction(action -> {
            DrawauFigureClass f1 =
                    new DrawauFigureClassBuilder()
                            .addName(nameBox.getText())
                            .addType(choiceBox.getValue())
                            .addFields(fields)
                            .addMethods(methods)
                            .build();
            f1.locate(workSpace);
            f1.draw();

            flist.add(f1);

            classFigurePanel.setVisible(false);
            classFigurePanel.getChildren().clear();
            fields.clear();
            methods.clear();
            addClassFigurePanelContent(classFigurePanel);
        });

        classFigurePanel.getChildren().add(acceptButton);
    }

    private HBox addFieldBox(List<DrawauFigureClass.FieldProperty> fields) {
        HBox fieldBox = new HBox();
        fieldBox.setAlignment(Pos.CENTER);
        fieldBox.setStyle(DrawauFigureClass.setMargin("10 0 0 0"));

        TextField fieldName = new TextField();
        TextField fieldType = new TextField();
        DrawauFigureClass.FieldProperty field = new DrawauFigureClass.FieldProperty("", "");

        fieldName.setStyle(DrawauFigureClass.setMargin("0 5 0 0"));
        fieldType.setStyle(DrawauFigureClass.setMargin("0 0 0 5"));

        fieldName.setPrefSize(100, 25);
        fieldName.setMinSize(100, 25);

        fieldType.setPrefSize(100, 25);
        fieldType.setMinSize(100, 25);

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