package com.example.drawau;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DrawauController {

    public Button figuresButton;
    public Button magicButton;
    public Button saveButton;
    public Button openButton;
    public Button newButton;
    public Button classFigure;

    public Button inheritArrow;
    public Button dependenceArrow;
    public Button aggregationArrow;

    public FlowPane figuresPanel;
    public Pane workSpace;
    public VBox classFigurePanel;
    public HBox mainControl;


    public static final double SIDE_PANEL_WIDTH = 40;

    public ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(
            DrawauFigureClass.TYPE_INTERFACE,
            DrawauFigureClass.TYPE_SIMPLE,
            DrawauFigureClass.TYPE_ABSTRACT));
    public TextField nameBox = new TextField();
    public List<DrawauFigureClass.FieldProperty> fields = new ArrayList<>();
    public List<DrawauFigureClass.FieldProperty> methods = new ArrayList<>();

    public Label subscribeArrowStatus;


    List<DrawauFigureClass> figures = new ArrayList<>();
    public static List<DrawauArrow> arrows = new ArrayList<>();

    public static String ACTION = "null";
    public static StringProperty subscribeStatusText = new SimpleStringProperty();


    public void initialize() {
        bindSubscribeStatusLabel();

        setNewButtonBehavior();

        setOpenButtonBehavior();

        setSaveButtonBehavior();

        setMagicButtonBehavior();

        setArrowButtonBehavior();

        setClassFigurePanelBehavior();

        addClassFigurePanelContent(classFigurePanel);

        addAnimationsForButtons();

        setClassFigureButtonBehavior();
    }

    public void bindSubscribeStatusLabel() {
        subscribeStatusText.bindBidirectional(subscribeArrowStatus.textProperty());
    }

    public void setNewButtonBehavior() {
        newButton.setOnAction(action -> {
            workSpace.getChildren().clear();
            workSpace.getChildren().addAll(
                    classFigurePanel,
                    figuresPanel
            );
        });
    }

    public void setOpenButtonBehavior() {
        openButton.setOnAction(action -> {
            FileChooser fileChooser = new FileChooser();

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("drawau files (*.drawau)", "*.drawau"));

            File file = fileChooser.showOpenDialog(null);

            if (file != null)
                DrawauFileManager.openDrawauFile(figures, arrows, file, workSpace);
        });
    }

    public void setSaveButtonBehavior() {
        saveButton.setOnAction(action -> {
            FileChooser fileChooser = new FileChooser();

            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("png files (*.png)", "*.png"),
                    new FileChooser.ExtensionFilter("drawau files (*.drawau)", "*.drawau"));

            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                if (file.getName().endsWith(".png"))
                    DrawauFileManager.saveImageFile(workSpace, file);
                else if (file.getName().endsWith(".drawau"))
                    DrawauFileManager.saveDrawauFile(figures, arrows, file);
            }
        });
    }

    public void setMagicButtonBehavior() {
        magicButton.setOnAction(action -> {
            try {
                DrawauFileManager.doMagic();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setArrowButtonBehavior() {
        inheritArrow.setOnAction(action -> {
            DrawauInheritArrow ar = new DrawauInheritArrow();
            ar.locate(workSpace);
            DrawauController.arrows.add(ar);
            ACTION = DrawauFigureClass.SUBSCRIBE_OUT;

            subscribeStatusText.setValue("Отметьте начальную точку");
        });

        dependenceArrow.setOnAction(action -> {
            DrawauDependenceArrow ar = new DrawauDependenceArrow();
            ar.locate(workSpace);
            DrawauController.arrows.add(ar);
            ACTION = DrawauFigureClass.SUBSCRIBE_OUT;

            subscribeStatusText.setValue("Отметьте начальную точку");
        });

        aggregationArrow.setOnAction(action -> {
            DrawauAggregationArrow ar = new DrawauAggregationArrow();
            ar.locate(workSpace);
            DrawauController.arrows.add(ar);
            ACTION = DrawauFigureClass.SUBSCRIBE_OUT;

            subscribeStatusText.setValue("Отметьте начальную точку");
        });
    }

    public void setClassFigurePanelBehavior() {
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
    }

    public void addAnimationsForButtons() {
        DrawauAnimation.setButtonWidthAnimation(
                newButton,
                openButton,
                saveButton,
                magicButton,
                figuresButton
        );
        DrawauAnimation.setClickFiguresButtonAnimation(figuresButton, figuresPanel);
    }

    public void setClassFigureButtonBehavior() {
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

            figures.add(f1);

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