package com.schedulers_algorithms.HardwareStatusBar;

import com.schedulers_algorithms.CPU.CPUState;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class HardwareStatusBar extends HBox {

    Label cpuStatusValueLabel = new Label();
    Label waitingProcessesCountValueLabel = new Label();

    public void place(VBox layout) {
        setAlignment(Pos.CENTER);

        Label cpuStatusLabel = new Label("CPU Status:");
        HBox.setMargin(cpuStatusLabel, new javafx.geometry.Insets(0, 5, 0, 10));
        getChildren().add(cpuStatusLabel);
        HBox.setMargin(cpuStatusValueLabel, new javafx.geometry.Insets(0, 10, 0, 5));
        getChildren().add(cpuStatusValueLabel);

        Label waitingProcessesCountLabel = new Label("Processes Waiting:");
        HBox.setMargin(waitingProcessesCountLabel, new javafx.geometry.Insets(0, 5, 0, 10));
        getChildren().add(waitingProcessesCountLabel);
        HBox.setMargin(waitingProcessesCountValueLabel, new javafx.geometry.Insets(0, 10, 0, 5));
        getChildren().add(waitingProcessesCountValueLabel);


        layout.getChildren().add(this);
    }

    public void updateCPUStatus(CPUState state) {
        switch (state) {
            case BUZY:
                cpuStatusValueLabel.setText("BUZY");
                cpuStatusValueLabel.setTextFill(Color.RED);
                break;
            case IDLE:
                cpuStatusValueLabel.setText("IDLE");
                cpuStatusValueLabel.setTextFill(Color.GREEN);
                break;
            default:
                cpuStatusValueLabel.setText("UNKNOWN");
                cpuStatusValueLabel.setTextFill(Color.BLACK);
                break;
        }
    }

    public void updateProcessCount(int processCount) {
        cpuStatusValueLabel.setText(Integer.toString(processCount));
        if (processCount > 10) {
            cpuStatusValueLabel.setTextFill(Color.RED);
        }
        else if (processCount > 5) {
            cpuStatusValueLabel.setTextFill(Color.YELLOWGREEN);
        }
        else {
            cpuStatusValueLabel.setTextFill(Color.GREEN);
        }
    }
    
}
