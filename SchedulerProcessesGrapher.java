package com.example;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SchedulerProcessesGrapher extends Pane {
    public void paint(Pane layout) {
        Canvas schedulerProcessesGrapherCanvas = new Canvas(400, 400);
        
        GraphicsContext gc = schedulerProcessesGrapherCanvas.getGraphicsContext2D();
        
        getChildren().add(schedulerProcessesGrapherCanvas);

        layout.getChildren().add(this);

        schedulerProcessesGrapherCanvas = null;
    }
}
