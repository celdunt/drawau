package com.example.drawau;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

class DrawauFileManagerTest
{

    File file;
    Pane workSpace = new Pane();
    List<DrawauFigureClass> figures = new ArrayList<>();
    List<DrawauArrow> arrows = new ArrayList<>();

    DrawauFigureClass figure1;
    DrawauFigureClass figure2;

    DrawauArrow relationship;

    @BeforeEach
    void setUp()
    {
        figure1 = new DrawauFigureClassBuilder().addName("Фигура №1").addLayoutX(100).addLayoutY(100).build();
        figure1.locate(workSpace);
        figure1.draw();

        figure2 = new DrawauFigureClassBuilder().addName("Фигура №2").addLayoutX(300).addLayoutY(100).build();
        figure2.locate(workSpace);
        figure2.draw();

        relationship = new DrawauInheritArrow();
        figure1.subscribe(relationship, 0, DrawauFigureClass.SUBSCRIBE_OUT);
        figure2.subscribe(relationship, 0, DrawauFigureClass.SUBSCRIBE_IN);
        relationship.locate(workSpace);
        relationship.draw();

        figures.add(figure1);
        figures.add(figure2);

        arrows.add(relationship);

        file = new File("C:\\Users\\Public\\Pictures\\file.drawau");
    }

    @Test
    void testSaveDrawauFile()
    {
        DrawauFileManager.saveDrawauFile(figures, arrows, file);
    }

    @Test
    void testOpenDrawauFile()
    {
        figures.clear();
        arrows.clear();

        DrawauFileManager.openDrawauFile(figures, arrows, file, workSpace);

        Assertions.assertEquals(figure1.getName(), figures.get(0).getName());
        Assertions.assertEquals(figure2.getName(), figures.get(1).getName());
    }
}