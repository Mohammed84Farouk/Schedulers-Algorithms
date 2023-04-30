package com.schedulers_algorithms.Add_Process_Dialog;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.CheckBox;

import com.schedulers_algorithms.App.SchedulerAlgorithm;
import com.schedulers_algorithms.Utils.Process;
import com.schedulers_algorithms.Utils.ProcessColor;

public class AddProcessDialog extends Stage {
    private final TextField processPriorityField = new TextField();
    private final TextField processBurstField = new TextField();
    private final ColorPicker processColorPicker = new ColorPicker(Color.RED);
    private final TextField processArrivalField = new TextField();

    StringBuilder processPriority;
    StringBuilder processBurst;
    ProcessColor processColor;
    StringBuilder processArrival;

    public AddProcessDialog(StringBuilder processPriority, StringBuilder processBurst, ProcessColor processColor, StringBuilder processArrival) {
        this.processPriority = processPriority;
        this.processBurst = processBurst;
        this.processColor = processColor;
        this.processArrival = processArrival;
    }

    private void handleSaveButtonPress(MouseEvent event) {

        processPriority.append(processPriorityField.getText());
        processBurst.append(processBurstField.getText());
        processColor.setColor(processColorPicker.getValue());
        processArrival.append(processArrivalField.getText());

        close();
    }

    public void showDialog(SchedulerAlgorithm algorithm) {
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Add Process");

        VBox mainLayout = new VBox();
        mainLayout.setSpacing(10);

        if (algorithm == SchedulerAlgorithm.PREEMPTIVE_PRIORITY
                || algorithm == SchedulerAlgorithm.NON_PREEMPTIVE_PRIORITY) {
            HBox processPriority = new HBox();
            processPriority.setSpacing(20);
            VBox.setMargin(processPriority, new javafx.geometry.Insets(10, 10, 0, 10));
            Label processPriorityLabel = new Label("Process Priority:");
            processPriorityLabel.setTextAlignment(TextAlignment.CENTER);
            processPriority.getChildren().addAll(processPriorityLabel, processPriorityField);

            mainLayout.getChildren().add(processPriority);
        }

        HBox processBurst = new HBox();
        processBurst.setSpacing(20);
        if (algorithm == SchedulerAlgorithm.PREEMPTIVE_PRIORITY
                || algorithm == SchedulerAlgorithm.NON_PREEMPTIVE_PRIORITY)
            VBox.setMargin(processBurst, new javafx.geometry.Insets(0, 10, 0, 10));
        else
            VBox.setMargin(processBurst, new javafx.geometry.Insets(10, 10, 0, 10));
        Label processBurstLabel = new Label("Process Burst:");
        processBurstLabel.setTextAlignment(TextAlignment.CENTER);
        processBurst.getChildren().addAll(processBurstLabel, processBurstField);

        HBox processColor = new HBox();
        processColor.setSpacing(20);
        VBox.setMargin(processColor, new javafx.geometry.Insets(0, 10, 20, 10));
        Label processColorLabel = new Label("Process Color:");
        processColorLabel.setTextAlignment(TextAlignment.CENTER);
        processColor.getChildren().addAll(processColorLabel, processColorPicker);

        HBox processArrival = new HBox();
        processArrival.setSpacing(20);
        VBox.setMargin(processArrival, new javafx.geometry.Insets(0, 10, 20, 10));
        Label processArrivalLabel = new Label("Process Arrival:");
        processArrivalLabel.setTextAlignment(TextAlignment.CENTER);

        processArrivalLabel.setDisable(true);
        processArrivalField.setDisable(true);

        CheckBox checkBox = new CheckBox();

        
        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                processArrivalLabel.setDisable(false);
                processArrivalField.setDisable(false);
            } else {
                processArrivalLabel.setDisable(true);
                processArrivalField.setDisable(true);
            }
        });

        processArrival.getChildren().addAll(processArrivalLabel, processArrivalField, checkBox);

        Button saveButton = new Button("Save");
        saveButton.setOnMousePressed(this::handleSaveButtonPress);

        mainLayout.getChildren().addAll(processBurst, processColor, processArrival, saveButton);

        Scene addProcessDialogScene = new Scene(mainLayout, 320, 240);

        setScene(addProcessDialogScene);

        showAndWait();
    }

}
