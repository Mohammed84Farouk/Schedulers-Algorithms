package com.schedulers_algorithms.RRQuantumSpinBox;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class RRQuantumSpinBox extends Spinner<Integer> {

    public void place(HBox layout) {
        SpinnerValueFactory<Integer> rrQuantumTime = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 2, 1);
        setValueFactory(rrQuantumTime);

        layout.getChildren().add(this);
    }
    
}
