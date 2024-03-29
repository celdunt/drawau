package com.example.drawau;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DrawauFigureClass implements DrawauIFigure, DrawauIObservable
{

    public static class FieldProperty implements Serializable
    {
        public FieldProperty(String vname, String vtype)
        {
            name = vname;
            type = vtype;
        }

        private String name;
        private String type;

        public void setName(String value)
        {
            name = value;
        }

        public String getName()
        {
            return name;
        }

        public void setType(String value)
        {
            type = value;
        }

        public String getType()
        {
            return type;
        }
    }

    public static final String TYPE_ABSTRACT = "abstract class";
    public static final String TYPE_SIMPLE = "class";
    public static final String TYPE_INTERFACE = "interface";

    public static final String SUBSCRIBE_IN = "sub in";
    public static final String SUBSCRIBE_OUT = "sub out";
    public static final String DELETE = "delete";

    private String parent = "";

    private String type;
    private String name;
    private List<FieldProperty> fields = new ArrayList<>();
    private List<FieldProperty> methods = new ArrayList<>();

    private final List<DrawauArrow> subscribersIn = new ArrayList<>();
    private final List<DrawauArrow> subscribersOut = new ArrayList<>();

    private double layoutX;
    private double layoutY;

    transient private VBox container = new VBox();

    double pressContainerX = 0;
    double pressContainerY = 0;

    transient Pane linkWorkSpace;

    public DrawauFigureClass(DrawauFigureClassBuilder builder)
    {
        container.alignmentProperty().set(Pos.TOP_CENTER);
        container.setPrefSize(builder.width, builder.height);
        container.setStyle(builder.style);
        container.setLayoutX(builder.layoutX);
        container.setLayoutY(builder.layoutY);
        addDragAction();
        addClickAction();

        type = builder.type;
        name = builder.name;
        fields = builder.fields;
        methods = builder.methods;
        layoutX = builder.layoutX;
        layoutY = builder.layoutY;
    }


    public List<DrawauArrow> getSubscribersIn()
    {
        return subscribersIn;
    }

    public List<DrawauArrow> getSubscribersOut()
    {
        return subscribersOut;
    }

    public DrawauArrow lastSubscribeOut()
    {
        if (subscribersOut.size() > 0)
            return subscribersOut.get(subscribersOut.size() - 1);
        else return new DrawauInheritArrow();
    }

    public DrawauArrow lastSubscribeIn()
    {
        if (subscribersIn.size() > 0)
            return subscribersIn.get(subscribersIn.size() - 1);
        else return new DrawauInheritArrow();
    }

    public void setParent(String value)
    {
        parent = value;
    }

    public String getParent()
    {
        return parent;
    }

    public void setType(String value)
    {
        type = value;
    }

    public String getType()
    {
        return type;
    }

    public void setName(String value)
    {
        name = value;
    }

    public String getName()
    {
        return name;
    }

    public void setFieldValue(FieldProperty value)
    {
        fields.add(value);
    }

    public void setMethodValue(FieldProperty value)
    {
        methods.add(value);
    }

    public List<FieldProperty> getFields()
    {
        return fields;
    }

    public List<FieldProperty> getMethods()
    {
        return methods;
    }

    public void setSize(double hvalue, double vvalue)
    {
        container.setPrefSize(hvalue, vvalue);
    }

    public void setStyle(String value)
    {
        container.setStyle(value);
    }

    public double getWidth()
    {
        return Math.max(container.getWidth(), container.getPrefWidth());
    }

    public VBox getContainer()
    {
        return container;
    }


    public double getHeight()
    {
        return Math.max(container.getHeight(), container.getPrefHeight());
    }

    public void setLayoutX(double value)
    {
        layoutX = value;
        container.setLayoutX(layoutX);
    }

    public void setLayoutY(double value)
    {
        layoutY = value;
        container.setLayoutY(layoutY);
    }

    public double getLayoutX()
    {
        return layoutX;
    }

    public double getLayoutY()
    {
        return layoutY;
    }

    public static String setMargin(String marginValue)
    {
        return "-fx-padding: " + marginValue +
                "; -fx-border-insets: " + marginValue +
                "; -fx-background-insets: " + marginValue + ";";
    }

    public void initialize()
    {
        container = new VBox();

        container.alignmentProperty().set(Pos.TOP_CENTER);
        container.setPrefSize(new DrawauFigureClassBuilder().width, new DrawauFigureClassBuilder().height);
        container.setStyle(new DrawauFigureClassBuilder().style);
        container.setLayoutX(layoutX);
        container.setLayoutY(layoutY);

        addDragAction();
        addClickAction();
    }

    private void addDragAction()
    {
        container.onMousePressedProperty().set(action ->
        {
            pressContainerX = action.getX();
            pressContainerY = action.getY();
        });
        container.setOnMouseDragged(action ->
        {
            layoutX = action.getSceneX() - pressContainerX;
            container.setLayoutX(layoutX);
            layoutY = action.getSceneY() - pressContainerY - DrawauController.SIDE_PANEL_WIDTH;
            container.setLayoutY(layoutY);

            notifySubscribers();
        });
    }

    @Override
    public void notifySubscribers()
    {
        for (DrawauArrow arrow : subscribersIn)
            arrow.update(arrow.getStartX(), arrow.getStartY(),
                    container.getLayoutX() + arrow.getEndSelfPosX(), container.getLayoutY());

        for (DrawauArrow arrow : subscribersOut)
            arrow.update(container.getLayoutX() + arrow.getStartSelfPosX(), container.getLayoutY(),
                    arrow.getEndX(), arrow.getEndY());
    }

    private void addClickAction()
    {
        container.onMouseClickedProperty().set(action ->
        {
            if (DrawauController.ACTION.equals(DELETE))
            {
                if (linkWorkSpace != null)
                {
                    linkWorkSpace.getChildren().remove(container);

                    for (DrawauArrow arrow : subscribersIn)
                    {

                        linkWorkSpace.getChildren().remove(arrow.getG());
                    }

                    for (DrawauArrow arrow : subscribersOut)
                    {
                        linkWorkSpace.getChildren().remove(arrow.getG());
                    }

                    DrawauController.subscribeStatusText.setValue("");
                }

                DrawauController.ACTION = "null";
            }
            if ((long) DrawauController.arrows.size() != 0 && !Objects.equals(DrawauController.ACTION, "null"))
            {
                double selfValue;
                if (action.getX() >= container.getPrefWidth() - 20
                        || action.getX() <= 20)
                    selfValue = action.getY();
                else selfValue = action.getX();

                subscribe(DrawauController.arrows.get(DrawauController.arrows.size() - 1),
                        selfValue, DrawauController.ACTION);
            }
        });
    }

    @Override
    public void subscribe(DrawauArrow sub, double selfX, String mode)
    {
        switch (mode)
        {
            case SUBSCRIBE_IN -> {
                sub.setEndX(container.getLayoutX() + selfX);
                sub.setEndSelfPosX(selfX);
                if (container.getHeight() > 1)
                    sub.setEndHeightContainer(container.getHeight());
                else sub.setEndHeightContainer(container.getPrefHeight());
                if (container.getWidth() > 1)
                    sub.setEndWidthContainer(container.getWidth());
                else sub.setEndWidthContainer(container.getPrefWidth());

                container.widthProperty().addListener(action ->
                {
                    sub.setEndWidthContainer(getWidth());
                });
                container.heightProperty().addListener(action ->
                {
                    sub.setEndHeightContainer(getHeight());
                });

                sub.setEndY(container.getLayoutY());
                subscribersIn.add(sub);
                sub.draw();

                DrawauController.ACTION = "null";

                DrawauController.subscribeStatusText.setValue("");
            }
            case SUBSCRIBE_OUT -> {
                sub.setStartX(container.getLayoutX() + selfX);
                sub.setStartSelfPosX(selfX);
                if (container.getHeight() > 1)
                    sub.setStartHeightContainer(container.getHeight());
                else sub.setStartHeightContainer(container.getPrefHeight());
                if (container.getWidth() > 1)
                    sub.setStartWidthContainer(container.getWidth());
                else sub.setStartWidthContainer(container.getPrefWidth());

                container.widthProperty().addListener(action ->
                {
                    sub.setStartWidthContainer(getWidth());
                });
                container.heightProperty().addListener(action ->
                {
                    sub.setStartHeightContainer(getHeight());
                });

                sub.setStartY(container.getLayoutY());
                subscribersOut.add(sub);

                DrawauController.ACTION = SUBSCRIBE_IN;

                DrawauController.subscribeStatusText.setValue("Отметьте конечную точку.");
            }
        }
    }

    @Override
    public void locate(Pane location)
    {
        linkWorkSpace = location;
        location.getChildren().add(container);
    }

    @Override
    public void draw()
    {
        container.getChildren().clear();
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
        for (FieldProperty field : fields)
        {
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


        Rectangle separator = new Rectangle();
        separator.setFill(Color.WHITE);
        separator.setStroke(Color.WHITE);
        separator.setWidth(getWidth() - 10);

        container.widthProperty().addListener(action ->
        {
            separator.setWidth(getWidth() - 14);
        });

        separator.setHeight(0.01);


        container.getChildren().get(container.getChildren().size() - 1).setStyle(setMargin("0 0 5 0"));
        container.getChildren().add(separator);
        container.getChildren().get(container.getChildren().size() - 1).setStyle(setMargin("0 0 5 0"));

        //Bottom-part
        for (FieldProperty method : methods)
        {
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
