package com.example.drawau;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class DrawauFigureClass implements DrawauIFigure {

    public static class FieldProperty {
        public FieldProperty(String vname, String vtype) {
            name = vname;
            type = vtype;
        }

        private String name;
        private String type;

        public void setName(String value) {name = value;}
        public String getName() {return name;}

        public void setType(String value) {type = value;}
        public String getType() {return type;}
    }


    public static final String TYPE_ABSTRACT = "abstract class";
    public static final String TYPE_SIMPLE = "class";
    public static final String TYPE_INTERFACE = "interface";

    public static final String SUBSCRIBE_IN = "sub in";
    public static final String SUBSCRIBE_OUT = "sub out";

    private String type;
    private String name;
    private List<FieldProperty> fields = new ArrayList<>();
    private List<FieldProperty> methods = new ArrayList<>();

    private List<DrawauArrow> subscribersIn = new ArrayList<>();
    private List<DrawauArrow> subscribersOut = new ArrayList<>();

    private VBox container = new VBox();


    double pressContainerX = 0;
    double pressContainerY = 0;



    public DrawauFigureClass(DrawauFigureClassBuilder builder) {
        container.alignmentProperty().set(Pos.TOP_CENTER);
        container.setPrefSize(builder.width, builder.height);
        container.setStyle(builder.style);
        addDragAction();
        addClickAction();

        type = builder.type;
        name = builder.name;
        fields = builder.fields;
        methods = builder.methods;


    }



    public void setType(String value) { type = value; }
    public String getType() { return type; }

    public void setName(String value) { name = value; }
    public String getName() { return name; }

    public List<FieldProperty> getFields() { return fields; }

    public List<FieldProperty> getMethods() { return methods; }

    public void setSize(double hvalue, double vvalue) { container.setPrefSize(hvalue, vvalue); }
    public void setStyle(String value) { container.setStyle(value); }
    public double getWidth() { return container.getWidth(); }
    public double getHeight() { return container.getHeight(); }

    public static String setMargin(String marginValue) {
        return "-fx-padding: " + marginValue +
                "; -fx-border-insets: " + marginValue +
                "; -fx-background-insets: " + marginValue + ";";
    }

    private void addDragAction() {
        container.onMousePressedProperty().set(action -> {
            pressContainerX = action.getX();
            pressContainerY = action.getY();
        });
        container.setOnMouseDragged(action -> {
            container.setLayoutX(action.getSceneX() - pressContainerX - DrawauController.SIDE_PANEL_WIDTH);
            container.setLayoutY(action.getSceneY() - pressContainerY);

            for (DrawauArrow arrow : subscribersIn) {
                arrow.setEndX(container.getLayoutX()  + container.getPrefWidth()/2);
                arrow.setEndY(container.getLayoutY());

                arrow.draw();
            }

            for (DrawauArrow arrow : subscribersOut) {
                arrow.setStartX(container.getLayoutX() + container.getPrefWidth()/2);
                arrow.setStartY(container.getLayoutY() + container.getPrefHeight());

                arrow.draw();
            }
        });
    }

    private void addClickAction() {
        container.onMouseClickedProperty().set(action -> {
            if (DrawauController.arrows.stream().count() != 0 && DrawauController.ACTION != "null") {
                subscribe(DrawauController.arrows.get((int)DrawauController.arrows.stream().count()-1),
                        DrawauController.ACTION);
            }
        });
    }

    public void subscribe(DrawauArrow sub, String mode) {
        switch (mode) {
            case SUBSCRIBE_IN -> {
                sub.setEndX(container.getLayoutX() + container.getPrefWidth()/2);
                sub.setEndY(container.getLayoutY());
                subscribersIn.add(sub);
                sub.draw();

                DrawauController.ACTION = "null";

                // region месаге
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Подписываем входца!");

                alert.showAndWait();
                // endregion
            }
            case SUBSCRIBE_OUT -> {
                sub.setStartX(container.getLayoutX() + container.getPrefWidth()/2);
                sub.setStartY(container.getLayoutY() + container.getPrefHeight());
                subscribersOut.add(sub);

                DrawauController.ACTION = SUBSCRIBE_IN;

                // region месаге
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Подписываем выходца!");

                alert.showAndWait();
                // endregion
            }
        }
    }

    @Override
    public void locate(Pane location) {
        location.getChildren().add(container);
    }

    @Override
    public void draw() {
        //Header-part
        HBox header_part = new HBox();
        header_part.alignmentProperty().set(Pos.CENTER);
        header_part.setStyle(setMargin("0 10 10 10"));

        Text header_part_typeClass = new Text(type + " ");
        Text header_part_nameClass = new Text(name);
        header_part_nameClass.getStyleClass().add("text-style");
        header_part_typeClass.getStyleClass().add("text-style");

        header_part.getChildren().add(header_part_typeClass);
        header_part.getChildren().add(header_part_nameClass);

        container.getChildren().add(header_part);

        //Middle-part
        for (FieldProperty field : fields) {
            HBox middle_part = new HBox();
            middle_part.setAlignment(Pos.BASELINE_CENTER);

            Text middle_part_fieldName = new Text("-" + field.getName());
            Text middle_part_fieldType = new Text("  " + field.getType());

            middle_part_fieldName.getStyleClass().add("text-style");
            middle_part_fieldType.getStyleClass().add("text-style");

            middle_part.getChildren().add(middle_part_fieldName);
            middle_part.getChildren().add(middle_part_fieldType);

            container.getChildren().add(middle_part);
        }

        container.getChildren().get((int)container.getChildren().stream().count()-1).setStyle(setMargin("0 0 10 0"));

        //Bottom-part
        for (FieldProperty method : methods) {
            HBox bottom_part = new HBox();
            bottom_part.setAlignment(Pos.BASELINE_CENTER);

            Text bottom_part_methodName = new Text("+" + method.getName());
            Text bottom_part_methodType = new Text("  " + method.getType());

            bottom_part_methodName.getStyleClass().add("text-style");
            bottom_part_methodType.getStyleClass().add("text-style");

            bottom_part.getChildren().add(bottom_part_methodName);
            bottom_part.getChildren().add(bottom_part_methodType);

            container.getChildren().add(bottom_part);
        }
    }

}
