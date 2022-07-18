package com.example.drawau;

import javafx.geometry.Pos;
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


    private String type;
    private String name;
    private List<FieldProperty> fields = new ArrayList<>();
    private List<FieldProperty> methods = new ArrayList<>();

    private VBox container = new VBox();


    double pressContainerX = 0;
    double pressContainerY = 0;



    public DrawauFigureClass(DrawauFigureClassBuilder builder) {
        container.alignmentProperty().set(Pos.TOP_CENTER);
        container.setPrefSize(builder.width, builder.height);
        container.setStyle(builder.style);
        addDragAction();

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
        });
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
        header_part.setStyle(setMargin("0 0 10 0"));

        Text header_part_typeClass = new Text(type + " ");
        Text header_part_nameClass = new Text(name);
        header_part_nameClass.getStyleClass().add("text-style");
        header_part_typeClass.getStyleClass().add("text-style");

        header_part.getChildren().add(header_part_typeClass);
        header_part.getChildren().add(header_part_nameClass);

        container.getChildren().add(header_part);

        //Пишем окно, где будет создаваться фигура класа и определяться весь контент

        //Middle-part
        for (FieldProperty field : fields) {
            HBox middle_part = new HBox();
            middle_part.setAlignment(Pos.CENTER);

            Text middle_part_fieldName = new Text(field.getName());
            Text middle_part_fieldType = new Text(field.getType());

            middle_part.getChildren().add(middle_part_fieldName);
            middle_part.getChildren().add(middle_part_fieldType);

            container.getChildren().add(middle_part);
        }

        //Bottom-part
        for (FieldProperty method : methods) {
            HBox bottom_part = new HBox();
            bottom_part.setAlignment(Pos.CENTER);

            Text bottom_part_methodName = new Text(method.getName());
            Text bottom_part_methodType = new Text(method.getType());

            bottom_part.getChildren().add(bottom_part_methodName);
            bottom_part.getChildren().add(bottom_part_methodType);

            container.getChildren().add(bottom_part);
        }
    }
}
