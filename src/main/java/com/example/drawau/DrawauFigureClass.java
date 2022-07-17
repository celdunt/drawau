package com.example.drawau;

import javafx.scene.layout.VBox;

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


    public void setType(String value) { type = value; }
    public String getType() { return type; }

    public void setName(String value) { name = value; }
    public String getName() { return name; }

    public void setFields(FieldProperty... values) {
        fields.clear();
        fields.addAll(List.of(values));
    }
    public List<FieldProperty> getFields() { return fields; }

    public void setMethods(FieldProperty... values) {
        methods.clear();
        methods.addAll(List.of(values));
    }
    public List<FieldProperty> getMethods() { return methods; }

    public void setSize(double hvalue, double vvalue) { container.setPrefSize(hvalue, vvalue); }
    public double getWidth() { return container.getWidth(); }
    public double getHeight() { return container.getHeight(); }


    @Override
    public void locate() {

    }

    @Override
    public void draw() {

    }
}
