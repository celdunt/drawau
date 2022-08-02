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

    public static void doMagic() throws IOException {
        //Открываем директорию с классами
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(null);

        //Записываем все пути к классам в лист
        List<Path> classes =
                Files.walk(Paths.get(file.getPath()))
                        .filter(Files::isRegularFile)
                        .collect(Collectors.toList());

        //!!Одна и та же стрелка подписывается на два объекта!!
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        for (Path p : classes) {

            try(InputStream in = Files.newInputStream(p);
                BufferedReader bReader = new BufferedReader(new InputStreamReader(in))) {
                String line = null;
                while ((line = bReader.readLine()) != null) {
                    alert.setContentText(line);
                    alert.showAndWait();
                }

            }

        }
    }
}
