package com.example.drawau;

import javafx.scene.layout.Pane;

import java.io.Serializable;

public interface DrawauIFigure extends Serializable {
    void locate(Pane location);
    void draw();
}
