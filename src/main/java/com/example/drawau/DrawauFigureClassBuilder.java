package com.example.drawau;

import java.util.ArrayList;
import java.util.List;

public class DrawauFigureClassBuilder {
    protected double width = 100;
    protected double height = 100;
    protected String style = "-fx-background-color: #635380";
    protected String type = DrawauFigureClass.TYPE_SIMPLE;
    protected String name = "myClass";
    protected List<DrawauFigureClass.FieldProperty> fields = new ArrayList<>();
    protected List<DrawauFigureClass.FieldProperty> methods = new ArrayList<>();

    public DrawauFigureClassBuilder addSize(double hvalue, double vvalue) {
        width = hvalue;
        height = vvalue;

        return this;
    }

    public DrawauFigureClassBuilder addStyle(String value) {
        style = value;

        return this;
    }

    public DrawauFigureClassBuilder addType(String value) {
        type = value;

        return this;
    }

    public DrawauFigureClassBuilder addName(String value) {
        name = value;

        return this;
    }

    public DrawauFigureClassBuilder addFields(List<DrawauFigureClass.FieldProperty> values) {
        fields.addAll(values);

        return this;
    }

    public DrawauFigureClassBuilder addMethods(List<DrawauFigureClass.FieldProperty> values) {
        methods.addAll(values);

        return this;
    }

    public DrawauFigureClass build() {
        return new DrawauFigureClass(this);
    }
}
