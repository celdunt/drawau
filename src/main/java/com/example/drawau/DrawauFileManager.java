package com.example.drawau;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class DrawauFileManager {
    public static void saveImageFile(Pane pane, File file) {
        try {
              WritableImage writableImage = new WritableImage(
                      (int)pane.getWidth() + 20, (int)pane.getHeight() + 20
              );
              pane.snapshot(null, writableImage);
              ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null),
                      "png", file);
        } catch (IOException ex) { ex.printStackTrace(); }
    }

    public static void saveDrawauFile(List<DrawauFigureClass> figures, List<DrawauArrow> arrows, File file) {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(figures);
            oos.writeObject(arrows);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void openDrawauFile(List<DrawauFigureClass> figures, List<DrawauArrow> arrows, File file, Pane location) {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<DrawauFigureClass> tfigures = (List<DrawauFigureClass>) ois.readObject();
            List<DrawauArrow> tarrows = (List<DrawauArrow>) ois.readObject();

            for (DrawauFigureClass tf : tfigures) {
                DrawauFigureClass f = tf;
                f.initialize();
                f.locate(location);
                f.draw();
                figures.add(f);
            }

            for(DrawauArrow ta : tarrows) {
                DrawauArrow a = ta;
                a.initialize();
                a.locate(location);
                a.draw();
                arrows.add(a);
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static List<DrawauFigureClass> doMagic(Pane location) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(null);

        if (file == null) return new ArrayList<>();

        List<Path> classes =
                Files.walk(Paths.get(file.getPath()))
                        .filter(Files::isRegularFile)
                        .collect(Collectors.toList());

        List<DrawauFigureClass> figures = new ArrayList<>();
        List<DrawauArrow> arrows = new ArrayList<>();

        for (Path p : classes) {
            try(InputStream in = Files.newInputStream(p);
                BufferedReader bReader = new BufferedReader(new InputStreamReader(in))) {
                String line = "";
                String readLine = "";

                int openedBrace = 0;




                bReader.mark(9999);

                while ((line = bReader.readLine()) != null) {
                    if (line.contains("class")
                        || line.contains("abstract class")
                        || line.contains("interface")) {

                        DrawauFigureClass dfc = new DrawauFigureClassBuilder().build();
                        String[] splitLine = line.split(" ");
                        for (int i = 0; i < splitLine.length; i++)
                        {
                            if (splitLine[i].equals("class")
                                || splitLine[i].equals("interface")) {
                                dfc.setName(splitLine[i+1]);
                                dfc.setType(splitLine[i]);
                            } else if (splitLine[i].equals("abstract")) {
                                dfc.setName(splitLine[i+2]);
                                dfc.setType(DrawauFigureClass.TYPE_ABSTRACT);
                            }

                            if (splitLine[i].equals("implements")
                                || splitLine[i].equals("extends"))
                                dfc.setParent(splitLine[i] + " " + splitLine[i+1]);
                        }

                        bReader.reset();

                        /*while ((line = bReader.readLine()) != null) {
                            if (line.contains("{")) openedBrace++;
                            if (line.contains("}")) openedBrace--;

                            if ((line.contains("=")
                                || line.contains(";"))
                                && (!line.contains("{")
                                && !line.contains("}"))
                                && openedBrace == 1) {

                                splitLine = line.split(" ");
                                DrawauFigureClass.FieldProperty fp = new DrawauFigureClass.FieldProperty("", "");
                                for (int i = 0; i < splitLine.length; i++) {
                                    if (splitLine[i].equals("=")) {
                                        fp.setName(splitLine[i-1]);
                                        fp.setType(splitLine[i-2]);
                                        dfc.setFieldValue(fp);
                                        break;
                                    }
                                    if (splitLine[i].contains(";")) {
                                        fp.setName(splitLine[i].replace(";", ""));
                                        fp.setType(splitLine[i-1]);
                                        dfc.setFieldValue(fp);
                                        break;
                                    }
                                }

                            }

                            if ((line.contains("(")
                                && line.contains(")")
                                && !line.contains("=")
                                && !line.contains(";")
                                && !line.contains("if")
                                && !line.contains("while")
                                && openedBrace <= 2)

                                || (line.contains("(")
                                && line.contains(")")
                                && line.contains("{")
                                && line.contains("}")
                                && openedBrace == 1)) {

                                splitLine = line.split(" ");
                                DrawauFigureClass.FieldProperty fp = new DrawauFigureClass.FieldProperty("", "");
                                for (int i = 0; i < splitLine.length; i++) {
                                    if (splitLine[i].contains("(")) {

                                        fp.setType(splitLine[i-1]);
                                        if (!splitLine[i].contains(")"))
                                            fp.setName(splitLine[i]+")");
                                        else fp.setName(splitLine[i]);

                                        dfc.setMethodValue(fp);

                                        break;
                                    }
                                }

                            }

                        }*/
                        dfc.locate(location);
                        dfc.draw();
                        figures.add(dfc);
                        break;
                    }
                }
            }

        }

        for (int i = 0; i < figures.size(); i++) {
            for (int j = 0; j < figures.size(); j++) {
                if (i != j && figures.get(j).getParent().contains(figures.get(i).getName())) {
                    if (figures.get(j).getParent().contains("implements")) {
                        DrawauDependenceArrow ar = new DrawauDependenceArrow();
                        ar.locate(location);
                        DrawauController.arrows.add(ar);
                        DrawauController.ACTION = DrawauFigureClass.SUBSCRIBE_OUT;
                    }
                    if (figures.get(j).getParent().contains("extends")) {
                        DrawauInheritArrow ar = new DrawauInheritArrow();
                        ar.locate(location);
                        DrawauController.arrows.add(ar);
                        DrawauController.ACTION = DrawauFigureClass.SUBSCRIBE_OUT;
                    }

                    figures.get(j).subscribe(DrawauController.arrows.get((int)DrawauController.arrows.stream().count()-1),
                            figures.get(j).getWidth()/2, DrawauController.ACTION);
                    DrawauController.ACTION = DrawauFigureClass.SUBSCRIBE_IN;
                    figures.get(i).subscribe(DrawauController.arrows.get((int)DrawauController.arrows.stream().count()-1),
                            figures.get(i).getWidth()/2, DrawauController.ACTION);
                    DrawauController.ACTION = "null";

                    figures.get(j).draw();
                    figures.get(i).draw();
                    DrawauController.arrows.get((int)DrawauController.arrows.stream().count()-1).draw();
                }
            }
        }

        List<DrawauFigureClass> linkedClasses = new ArrayList<>();

        double kingX = 100;
        double kingY = 10;
        double knightX = 100;
        double knightY = 300;
        double servantX = 10;
        double servantY = 500;


        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        for (int i = 0; i < figures.size(); i++) {
            for (int j = 0; j < figures.size()-1; j++) {
                if (figures.get(j).getSubscribersOut().size() < figures.get(j + 1).getSubscribersOut().size()) {
                    DrawauFigureClass temp = figures.get(j);
                    figures.set(j, figures.get(j + 1));
                    figures.set(j + 1, temp);
                }
            }
        }

        for (int i = 0; i < figures.size(); i++) {
            if (figures.get(i).getSubscribersIn().size() > 0
                    && figures.get(i).getSubscribersOut().size() == 0) {
                figures.get(i).setLayoutX(kingX);
                figures.get(i).setLayoutY(kingY);
                kingX += figures.get(i).getWidth() + 100;
                if (kingX > 900) {
                    kingX = 100;
                    kingY += 130;
                }
            }
            if (figures.get(i).getSubscribersIn().size() > 0
                    && figures.get(i).getSubscribersOut().size() > 0) {
                figures.get(i).setLayoutX(knightX);
                figures.get(i).setLayoutY(knightY);
                knightX += figures.get(i).getWidth() + 100;
                if (knightX > 900) {
                    knightX = 100;
                    knightY += 130;
                }
            }
            if (figures.get(i).getSubscribersIn().size() == 0
                    && figures.get(i).getSubscribersOut().size() >= 0) {
                figures.get(i).setLayoutX(servantX);
                figures.get(i).setLayoutY(servantY);
                servantX += figures.get(i).getWidth() + 100;
                if (servantX > 900) {
                    servantX = 10;
                    servantY += 130;
                }
            }
            figures.get(i).draw();
            figures.get(i).notifySubscribers();
        }


        return figures;
    }
}
