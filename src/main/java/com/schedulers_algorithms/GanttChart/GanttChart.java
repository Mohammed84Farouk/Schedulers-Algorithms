package com.schedulers_algorithms.GanttChart;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GanttChart extends HBox {

    private ScrollPane scrollPane = new ScrollPane();

    public void place(Pane layout) {
        setSpacing(5);

        scrollPane.setPrefHeight(200);
        VBox.setMargin(scrollPane, new javafx.geometry.Insets(0, 50, 20, 50));
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setContent(this);

        layout.getChildren().add(scrollPane);
    }

    public void adjustView() {
        scrollPane.setHvalue(1.0);
    }
    
}
