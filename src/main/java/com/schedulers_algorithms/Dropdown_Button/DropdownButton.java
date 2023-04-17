package com.schedulers_algorithms.Dropdown_Button;

import com.schedulers_algorithms.App.SchedulerAlgorithm;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class DropdownButton extends ComboBox<SchedulerAlgorithm> {

    public void place(Pane layout) {

        getItems().addAll(SchedulerAlgorithm.values());

        setValue(SchedulerAlgorithm.NONE);

        layout.getChildren().add(this);
    }
}
